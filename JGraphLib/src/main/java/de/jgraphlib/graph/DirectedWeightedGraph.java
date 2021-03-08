package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.util.Tuple;

public class DirectedWeightedGraph<V extends Vertex<P>, P, E extends WeightedEdge<W>, W>
		extends WeightedGraph<V, P, E, W> {

	public DirectedWeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);
	}

	public DirectedWeightedGraph(DirectedWeightedGraph<V, P, E, W> graph) {
		/* ATTENTION: This is a shallow copy*/
		super(graph);
	}
	
	@Override
	public DirectedWeightedGraph<V, P, E, W> copy() {
		/* ATTENTION: This is a shallow copy*/
		return new DirectedWeightedGraph<V,P,E,W>(this);
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

	public List<E> getOutgoingEdgesOf(Vertex<P> vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			edges.add(super.edges.get(adjacency.getFirst()));
		return edges;
	}

	public List<E> getIncomingEdgesOf(Vertex<P> vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			for (E edge : getOutgoingEdgesOf(vertices.get(adjacency.getSecond())))
				if (getTargetOf(vertices.get(adjacency.getSecond()), edge).equals(vertex))
					edges.add(edge);
		return edges;
	}
}