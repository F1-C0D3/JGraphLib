package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public abstract class WeightedGraph<V extends Vertex<L>, L, E extends WeightedEdge<W>, W, P extends Path<V,E,W>> {
	
	protected int vertexCount;
	protected int edgeCount;
	protected TreeMap<Integer, V> vertices;
	protected TreeMap<Integer, E> edges;
	protected List<P> paths;	
	protected Supplier<V> vertexSupplier;
	protected Supplier<E> edgeSupplier;
	protected Supplier<W> edgeWeightSupplier;
	protected Supplier<P> pathSupplier;
	
	protected TreeMap</*key=sourceVertexID*/Integer, 
					  /*value=*/ArrayList<Tuple</*edgeID*/Integer,
											    /*targetVertexID*/Integer>>> sourceTargetAdjacencies;
	protected TreeMap</*key=targetVertexID*/Integer, 
					  /*value=*/ArrayList<Tuple</*edgeID*/Integer,
												/*sourceVertexID*/Integer>>> targetSourceAdjacencies;	
	protected TreeMap</*key=edgeID*/Integer,
					  /*value=*/Tuple</*sourceVertexID*/Integer,
					  				  /*targetVertexID*/Integer>> edgeAdjacencies;

	public WeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier, Supplier<P> pathSupplier) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.edgeWeightSupplier = edgeWeightSupplier;
		this.pathSupplier = pathSupplier;
		this.vertices = new TreeMap<Integer, V>();
		this.edges = new TreeMap<Integer, E>();
		this.paths = new ArrayList<P>();
		this.sourceTargetAdjacencies = new TreeMap<Integer, ArrayList<Tuple<Integer, Integer>>>();
		this.targetSourceAdjacencies = new TreeMap<Integer, ArrayList<Tuple<Integer, Integer>>>();
		this.edgeAdjacencies = new TreeMap<Integer, Tuple<Integer, Integer>>();
	}
	
	public abstract WeightedGraph<V, L, E, W, P> copy();
	
	protected List<P> copyPaths() {
		List<P> pathCopies = new ArrayList<P>();
		for (P path : paths) {
			P pathCopy = pathSupplier.get();
			pathCopy.set(getVertex(path.getSource().getID()), getVertex(path.getTarget().getID()));
			for (Tuple<E, V> edgeVertexTuple : path.subList(1, path.size()))
				pathCopy.add(new Tuple<E, V>(getEdge(edgeVertexTuple.getSecond().getID()),
						getVertex(edgeVertexTuple.getSecond().getID())));
			pathCopies.add(pathCopy);
		}
		return pathCopies;
	}

	public P copyPath(int i) {
		P pathCopy = pathSupplier.get();
		pathCopy.set(paths.get(i).getSource(), paths.get(i).getTarget());
		pathCopy.addAll(paths.get(i).subList(1, paths.get(i).size()));
		return pathCopy;
	}

	public void addPath(P path) {
		this.paths.add(path);
	}

	public P addPath(V source, V target) {
		P path = pathSupplier.get();
		path.set(source, target);
		paths.add(path);
		return path;
	}

	public void setPaths(List<P> paths) {
		this.paths = paths;
	}

	public List<P> getPaths() {
		return paths;
	}

	public P getPath(int i) {
		return this.paths.get(i);
	}
	
	public P setPath(int i, P path) {
		return this.paths.set(i, path);
	}

	public TreeMap<Integer,E> copyEdges() {	
		TreeMap<Integer, E> linkCopies = new TreeMap<Integer, E>();	
		for (E edge : getEdges()) {			
			E edgeCopy = edgeSupplier.get();		
			edgeCopy.setID(edge.getID());
			W edgeWeight = edgeWeightSupplier.get();
			edgeCopy.setWeight(edgeWeight);		
			linkCopies.put(edgeCopy.getID(), edgeCopy);
		}
		return linkCopies;
	}

	public V addVertex(L position) {
		V vertex = vertexSupplier.get();
		vertex.setID(vertexCount++);
		vertex.setPosition(position);
		vertices.put(vertex.getID(), vertex);
		sourceTargetAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
		targetSourceAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
		return vertex;
	}

	public boolean addVertex(V vertex) {
		if (vertex.getPosition() != null) {
			vertex.setID(vertexCount++);
			vertices.put(vertex.getID(), vertex);
			sourceTargetAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
			targetSourceAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
			return true;
		}
		return false;
	}
	
	public boolean removeVertices(List<V> vertices) {
		for(V vertex : vertices) removeVertex(vertex);		
		return true;
	}
	
	public V removeVertex(V vertex) {
		
		if(vertices.containsKey(vertex.getID())) {
			for(Tuple<Integer,Integer> adjacency : sourceTargetAdjacencies.remove(vertex.getID())) {	
				int edgeID = adjacency.getFirst();
				int targetVertexID = adjacency.getSecond();				
				targetSourceAdjacencies.get(targetVertexID).removeIf(a -> a.getSecond().equals(vertex.getID()));
				edgeAdjacencies.remove(edgeID);	
				edges.remove(edgeID);
			}
		
			for(Tuple<Integer,Integer> adjacency : targetSourceAdjacencies.remove(vertex.getID())) {			
				int edgeID = adjacency.getFirst();
				int sourceVertexID = adjacency.getSecond();	
				sourceTargetAdjacencies.get(sourceVertexID).removeIf(a -> a.getSecond().equals(vertex.getID()));
				edgeAdjacencies.remove(edgeID);
				edges.remove(edgeID);
			}	
			return vertices.remove(vertex.getID());	
		}		
		return null;
	}
	
	public abstract Boolean isDirected();

	public abstract E addEdge(V source, V target, W weight);

	public abstract E addEdge(V source, V target);

	public List<E> getEdges() {
		return new ArrayList<E>(edges.values());
	}

	public List<V> getVertices() {
		return new ArrayList<V>(vertices.values());
	}

	public V getFirstVertex() {
		return this.vertices.get(0);
	}

	public V getLastVertex() {
		return this.vertices.get(vertexCount - 1);
	}

	public V getVertex(V vertex) {
		return this.vertices.get(vertex.getID());
	}

	public V getVertex(int ID) {
		return this.vertices.get(ID);
	}

	public E getEdge(E edge) {
		return edges.get(edge.getID());
	}

	public E getEdge(int ID) {
		return edges.get(ID);
	}

	public Tuple<V, V> getVerticesOf(E edge) {
		if(edgeAdjacencies.containsKey(edge.getID())) {
			Tuple<Integer, Integer> vertexIDs = edgeAdjacencies.get(edge.getID());
			return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
		}
		return null;
	}

	public E getEdge(V source, V target) {
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(source.getID()))
			if (adjacency.getSecond() == target.getID())
				return this.edges.get(adjacency.getFirst());
		return null;
	}

	public abstract List<E> getOutgoingEdgesOf(V vertex);

	public abstract List<E> getEdgesOf(V vertex);
	
	public abstract List<E> getNeighboringEdgesOf(E edge);

	public boolean containsEdge(V source, V target) {
		if (source.getID() < sourceTargetAdjacencies.size())
			for (Tuple<Integer, Integer> edgeVertexTuple : sourceTargetAdjacencies.get(source.getID()))
				if (edgeVertexTuple.getSecond() == target.getID())
					return true;
		return false;
	}

	public V getTargetOf(V vertex, E edge) {
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		if (vertex.getID() == vertexIDs.getFirst())
			return this.vertices.get(vertexIDs.getSecond());
		else if (vertex.getID() == vertexIDs.getSecond())
			return this.vertices.get(vertexIDs.getFirst());
		return null;
	}

	public List<V> getNextHopsOf(V vertex) {
		List<V> nextHops = new ArrayList<V>();
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			nextHops.add(vertices.get(adjacency.getSecond()));
		return nextHops;
	}

	public List<Tuple<E, V>> getNextPathsOf(V vertex) {
		List<Tuple<E, V>> nextPaths = new ArrayList<Tuple<E, V>>();
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			nextPaths.add(new Tuple<E, V>(edges.get(adjacency.getSecond()), vertices.get(adjacency.getFirst())));
		return nextPaths;
	}

	

	public TreeMap<Integer,ArrayList<Tuple<Integer, Integer>>> getSourceTargetAdjacencies() {
		return this.sourceTargetAdjacencies;
	}
	
	public TreeMap<Integer,ArrayList<Tuple<Integer, Integer>>> getTargetSourceAdjacencies() {
		return this.sourceTargetAdjacencies;
	}
	public Iterator<V> vertexIterator() {
		Iterator<V> iterator = new Iterator<V>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < vertices.size() && vertices.get(i) != null;
			}

			@Override
			public V next() {
				return vertices.get(i++);
			}
		};
		return iterator;
	}

	public Iterator<E> edgeIterator() {
		Iterator<E> iterator = new Iterator<E>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < edges.size() && edges.get(i) != null;
			}

			@Override
			public E next() {
				return edges.get(i++);
			}
		};
		return iterator;
	}

	public void clear() {
		this.vertices.clear();
		this.vertexCount = 0;
		this.edges.clear();
		this.edgeCount = 0;
		this.sourceTargetAdjacencies.clear();
		this.targetSourceAdjacencies.clear();
		this.edgeAdjacencies.clear();
	}

	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("Vertices:\n");
		
		for (V vertex : this.getVertices()) 
			stringBuilder
				.append(Integer.toString(vertex.getID()))
				.append(": ").append(vertex.getPosition().toString())
				.append("\n");
		
		stringBuilder.append("Edges:\n");
		
		for (E edge : this.getEdges()) 
			stringBuilder
				.append(edge.getID()).append(": ")
				.append(getVerticesOf(edge).getFirst().getID()).append(" ~> ")
				.append(getVerticesOf(edge).getSecond().getID())
				.append("\n");

		return stringBuilder.toString();
	}
}