package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.util.Tuple;

public class UndirectedWeightedGraph<V extends Vertex<P>, P, E extends WeightedEdge<W>, W>
		extends WeightedGraph<V, P, E, W> {

	public UndirectedWeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);
	}
	
	public UndirectedWeightedGraph(UndirectedWeightedGraph<V,P,E,W> graph) {
		super(graph);
	}

	public E addEdge(V source, V target, W weight) {
		if (containsEdge(source, target))
			return null;
		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
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
		
	public UndirectedWeightedGraph<V,P,E,W> copy() {
	    return new UndirectedWeightedGraph<V,P,E,W>(this);
	}
}