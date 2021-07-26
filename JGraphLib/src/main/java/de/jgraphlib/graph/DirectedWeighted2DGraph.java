package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.util.Tuple;

public class DirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W>
		extends Weighted2DGraph<V, E, W> {

	public DirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);
	}

	public DirectedWeighted2DGraph(DirectedWeighted2DGraph<V, E, W> graph) {
		super(graph);
	}

	@Override
	public DirectedWeighted2DGraph<V, E, W> copy() {
		/* ATTENTION: This is a shallow copy */
		return new DirectedWeighted2DGraph<V, E, W>(this);
	}
	
	public E addEdge(V source, V target) {
		return null;
	}

	public E addEdge(V source, V target, W weight) {
		if (containsEdge(source, target))
			return null;
		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		edge.setWeight(weight);
		edges.add(edge);
		super.vertexAdjacencies.get(source.getID()).add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));
		return edge;
	}

	public List<E> getEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID())) {
			edges.add(super.edges.get(adjacency.getFirst()));
			for (E edge : getOutgoingEdgesOf(vertices.get(adjacency.getSecond())))
				if (getTargetOf(vertices.get(adjacency.getSecond()), edge).equals(vertex))
					edges.add(edge);
		}
		return edges;
	}

	public List<Integer> getEdgeIdsOf(int vertexId) {
		List<Integer> edges = new ArrayList<Integer>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertexId)) {
			edges.add(adjacency.getFirst());
			for (E edge : getOutgoingEdgesOf(vertices.get(adjacency.getSecond())))
				if (getTargetOf(vertices.get(adjacency.getSecond()), edge).equals(vertexId))
					edges.add(edge.getID());
		}
		return edges;
	}

	public List<E> getOutgoingEdgesOf(Vertex<Position2D> vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			edges.add(super.edges.get(adjacency.getFirst()));
		return edges;
	}

	public List<E> getIncomingEdgesOf(Vertex<Position2D> vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			for (E edge : getOutgoingEdgesOf(vertices.get(adjacency.getSecond())))
				if (getTargetOf(vertices.get(adjacency.getSecond()), edge).equals(vertex))
					edges.add(edge);
		return edges;
	}
	
	public V getSourceOf(E edge) {
		return this.vertices.get(edgeAdjacencies.get(edge.getID()).getFirst());
	}
	
	public V getTargetOf(E edge) {
		return this.vertices.get(edgeAdjacencies.get(edge.getID()).getSecond());
	}
	
	public Boolean isDirected() {
		return true;
	}
}
