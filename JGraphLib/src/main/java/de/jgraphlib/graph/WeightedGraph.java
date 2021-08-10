package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public abstract class WeightedGraph<V extends Vertex<L>, L, E extends WeightedEdge<W>, W, P extends Path<V, E, W>> {

	protected int vertexCount;
	protected int edgeCount;

	protected List<V> vertices;
	protected List<E> edges;
	protected List<P> paths;

	protected Supplier<V> vertexSupplier;
	protected Supplier<E> edgeSupplier;
	protected Supplier<W> edgeWeightSupplier;
	protected Supplier<P> pathSupplier;

	/* SourceVertexID i : (EdgeID, TargetVertexID), (EdgeID, TargetVertexID), ... */
	protected List<ArrayList<Tuple<Integer, Integer>>> sourceTargetAdjacencies;

	/* TargetVertexID i : (EdgeID, SourceVertexID), (EdgeID, SourceVertexID), ... */
	protected List<ArrayList<Tuple<Integer, Integer>>> targetSourceAdjacencies;

	/* edge i : (SourceVertexID, TargetVertexID) */
	protected List<Tuple<Integer, Integer>> edgeAdjacencies;

	public WeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier,
			Supplier<P> pathSupplier) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.edgeWeightSupplier = edgeWeightSupplier;
		this.pathSupplier = pathSupplier;
		this.vertices = new ArrayList<V>();
		this.edges = new ArrayList<E>();
		this.paths = new ArrayList<P>();
		this.sourceTargetAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();
		this.targetSourceAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();
		this.edgeAdjacencies = new ArrayList<Tuple<Integer, Integer>>();
	}

	public Supplier<P> getPathSupplier() {
		return this.pathSupplier;
	}

	public abstract WeightedGraph<V, L, E, W, P> copy();

	protected List<E> copyEdges() {
		List<E> edgeCopies = new ArrayList<E>();
		for (E edge : getEdges()) {
			E edgeCopy = edgeSupplier.get();
			edgeCopy.setID(edge.getID());
			W edgeWeight = edgeWeightSupplier.get();
			edgeCopy.setWeight(edgeWeight);
			edgeCopies.add(edgeCopy);
		}
		return edgeCopies;
	}

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

	public V addVertex(L position) {
		V vertex = vertexSupplier.get();
		vertex.setID(vertexCount++);
		vertex.setPosition(position);
		vertices.add(vertex);
		sourceTargetAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
		targetSourceAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
		return vertex;
	}

	public boolean addVertex(V vertex) {
		if (vertex.getPosition() != null) {
			vertex.setID(vertexCount++);
			vertices.add(vertex);
			sourceTargetAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
			targetSourceAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
			return true;
		}
		return false;
	}

	public abstract Boolean isDirected();

	public abstract E addEdge(V source, V target, W weight);

	public abstract E addEdge(V source, V target);

	public List<E> getEdges() {
		return edges;
	}

	public List<V> getVertices() {
		return vertices;
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
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
	}

	public E getEdge(V source, V target) {
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(source.getID()))
			if (adjacency.getSecond() == target.getID())
				return this.edges.get(adjacency.getFirst());
		return null;
	}

	public abstract List<E> getOutgoingEdgesOf(V vertex);

	public abstract List<E> getEdgesOf(V vertex);

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
		for (V vertex : this.getVertices()) {
			stringBuilder.append(Integer.toString(vertex.getID())).append(": ");
			stringBuilder.append(vertex.getPosition().toString()).append("\n");
		}

		stringBuilder.append("Edges:\n");
		for (E edge : this.getEdges()) {
			stringBuilder.append(edge.getID()).append(": ");

			Tuple<V, V> vertices = this.getVerticesOf(edge);
			stringBuilder.append("source: ").append(vertices.getFirst().getID()).append(", ");
			stringBuilder.append("target: ").append(vertices.getSecond().getID()).append(", ");
			stringBuilder.append("weight: ").append(edge.getWeight().toString()).append("\n");
		}

		return stringBuilder.toString();
	}
}