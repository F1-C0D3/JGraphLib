package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.util.Tuple;

public abstract class WeightedGraph<V extends Vertex<P>, P, E extends WeightedEdge<W>, W> {

	protected int vertexCount;
	protected int edgeCount;
	protected ArrayList<V> vertices;
	protected ArrayList<E> edges;
	protected Supplier<V> vertexSupplier;
	protected Supplier<E> edgeSupplier;
	protected ArrayList<ArrayList<Tuple<Integer, Integer>>> vertexAdjacencies;
	protected ArrayList<Tuple<Integer, Integer>> edgeAdjacencies;

	public WeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.vertices = new ArrayList<V>();
		this.edges = new ArrayList<E>();
		this.vertexAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();
		this.edgeAdjacencies = new ArrayList<Tuple<Integer, Integer>>();
	}
	
	public WeightedGraph(WeightedGraph<V,P,E,W> graph) {
		this.vertexSupplier = graph.vertexSupplier;
		this.edgeSupplier = graph.edgeSupplier;
		this.vertices = graph.vertices;
		this.edges = graph.edges;
		this.vertexAdjacencies = graph.vertexAdjacencies;
		this.edgeAdjacencies = graph.edgeAdjacencies;
	}
				
	public V addVertex(P position) {
		V v = vertexSupplier.get();
		v.setID(vertexCount++);
		v.setPosition(position);
		vertices.add(v);
		vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
		return v;
	}

	public boolean addVertex(V v) {
		if (v.getPosition() != null) {
			v.setID(vertexCount++);
			vertices.add(v);
			vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
			return true;
		}
		return false;
	}

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

	public V getVertex(V v) {
		return this.vertices.get(v.getID());
	}

	public V getVertex(int ID) {
		return this.vertices.get(ID);
	}

	public E getEdge(E e) {
		return edges.get(e.getID());
	}

	public E getEdge(int ID) {
		return edges.get(ID);
	}

	public Tuple<V, V> getVerticesOf(E edge) {
		Tuple<Integer, Integer> VIDs = this.edgeAdjacencies.get(edge.getID());
		return new Tuple<V, V>(this.vertices.get(VIDs.getFirst()), this.vertices.get(VIDs.getSecond()));
	}

	public E getEdge(V source, V target) {
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
			if (adjacency.getSecond() == target.getID())
				return this.edges.get(adjacency.getFirst());
		return null;
	}

	public boolean containsEdge(V source, V target) {
		if (source.getID() < vertexAdjacencies.size())
			for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
				if (adjacency.getSecond() == target.getID())
					return true;
		return false;
	}

	public E addEdge(V source, V target) {
		return this.addEdge(source, target, null);
	}

	public abstract E addEdge(V source, V target, W weight);

	public abstract List<E> getEdgesOf(V V);

	public V getTargetOf(V vertex, E edge) {
		Tuple<Integer, Integer> VIDs = this.edgeAdjacencies.get(edge.getID());
		if (vertex.getID() == VIDs.getFirst())
			return this.vertices.get(VIDs.getSecond());
		else if (vertex.getID() == VIDs.getSecond())
			return this.vertices.get(VIDs.getFirst());
		return null;
	}

	public List<V> getNextHopsOf(V v) {
		List<V> nextHops = new ArrayList<V>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(v.getID()))
			nextHops.add(vertices.get(adjacency.getSecond()));
		return nextHops;
	}

	public List<Tuple<E, V>> getNextPathsOf(V v) {
		List<Tuple<E, V>> nextPaths = new ArrayList<Tuple<E, V>>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(v.getID()))
			nextPaths.add(new Tuple<E, V>(edges.get(adjacency.getSecond()), vertices.get(adjacency.getFirst())));
		return nextPaths;
	}

	public Iterator<V> VIterator() {
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
		this.vertexAdjacencies.clear();
		this.edgeAdjacencies.clear();
	}
}