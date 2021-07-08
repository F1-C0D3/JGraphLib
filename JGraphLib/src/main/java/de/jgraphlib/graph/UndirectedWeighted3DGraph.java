package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.util.Tuple;

public class UndirectedWeighted3DGraph<V extends Vertex<Position3D>, E extends WeightedEdge<W>, W>
		extends Weighted3DGraph<V, E, W> {

	public UndirectedWeighted3DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);
	}

	public UndirectedWeighted3DGraph(UndirectedWeighted3DGraph<V, E, W> graph) {
		super(graph);
	}

	@Override
	public UndirectedWeighted3DGraph<V, E, W> copy() {
		return new UndirectedWeighted3DGraph<V, E, W>(this);
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

	/*
	 * For Genetic Algorithm network representation Phenotype -> Genotype
	 */
	public List<ArrayList<Tuple<Integer, Integer>>> getVertexAdjacencies() {
		return this.vertexAdjacencies;
	}
}
