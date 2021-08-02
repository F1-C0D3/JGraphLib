package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class UndirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance, P extends Path<V, E, W>>
		extends Weighted2DGraph<V, E, W, P> {

	public UndirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier,
			Supplier<W> edgeWeightSupplier, Supplier<P> pathSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier, pathSupplier);
	}

	public UndirectedWeighted2DGraph(UndirectedWeighted2DGraph<V, E, W, P> graph) {
		super(graph.vertexSupplier, graph.edgeSupplier, graph.edgeWeightSupplier, graph.pathSupplier);
		this.edgeWeightSupplier = graph.edgeWeightSupplier;
		this.pathSupplier = graph.pathSupplier;
		this.vertices = graph.vertices;
		this.edges = graph.copyEdges(); // deep copy edges
		this.sourceTargetAdjacencies = graph.sourceTargetAdjacencies;
		this.targetSourceAdjacencies = graph.targetSourceAdjacencies;
		this.edgeAdjacencies = graph.edgeAdjacencies;
	}

	public UndirectedWeighted2DGraph<V, E, W, P> copy() {
		return new UndirectedWeighted2DGraph<V, E, W, P>(this);
	}

	public E addEdge(V source, V target) {

		if (containsEdge(source, target))
			return null;

		E edge = edgeSupplier.get();
		edge.setID(edgeCount);
		edgeCount++;
		W weight = edgeWeightSupplier.get();
		weight.setDistance(this.getDistance(source.getPosition(), target.getPosition()));
		edge.setWeight(weight);
		edges.add(edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public E addEdge(V source, V target, W weight) {

		if (containsEdge(source, target))
			return null;

		E edge = edgeSupplier.get();
		edge.setID(edgeCount);
		edgeCount++;
		edge.setWeight(weight);
		edges.add(edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public List<E> getEdgesOf(V vertex) {
		
		Set<E> edges = new HashSet<E>();
		
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			edges.add(this.edges.get(adjacency.getFirst()));
		
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			edges.add(this.edges.get(adjacency.getFirst()));
		
		return List.copyOf(edges);
	}

	public Tuple<V, V> getVerticesOf(E edge) {
		return new Tuple<V, V>(getVertex(edgeAdjacencies.get(edge.getID()).getFirst()),
				getVertex(edgeAdjacencies.get(edge.getID()).getSecond()));
	}

	public Boolean isDirected() {
		return false;
	}
}
