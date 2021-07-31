package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position3D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class UndirectedWeighted3DGraph<V extends Vertex<Position3D>, E extends WeightedEdge<W>, W, P extends Path<V,E,W>>
		extends Weighted3DGraph<V, E, W, P> {

	public UndirectedWeighted3DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier, Supplier<P> pathSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier, pathSupplier);
	}
	
	@Override
	public WeightedGraph<V, Position3D, E, W, P> copy() {
		return null;
	}

	public E addEdge(V source, V target, W weight) {
		if (containsEdge(source, target))
			return null;
		E edge = edgeSupplier.get();
		edge.setID(edgeCount);
		edgeCount++;
		edge.setWeight(weight);
		edges.add(edge);
		vertexAdjacencies.get(source.getID()).add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		vertexAdjacencies.get(target.getID()).add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));
		return edge;
	}

	public List<E> getEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			edges.add(this.edges.get(adjacency.getFirst()));
		return edges;
	}

	public List<Integer> getEdgeIdsOf(int vertexId) {
		List<Integer> edges = new ArrayList<Integer>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertexId))
			edges.add(adjacency.getFirst());
		return edges;
	}

	@Override
	public Boolean isDirected() {
		return false;
	}

	@Override
	public E addEdge(V source, V target) {
		return null;
	}

	@Override
	public List<E> getOutgoingEdgesOf(V vertex) {
		return null;
	}
}
