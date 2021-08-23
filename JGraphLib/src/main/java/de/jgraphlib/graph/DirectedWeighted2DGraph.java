
package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class DirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance, P extends Path<V,E,W>>
		extends Weighted2DGraph<V, E, W, P> {

	public DirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier,
			Supplier<P> pathSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier, pathSupplier);
	}

	public DirectedWeighted2DGraph(DirectedWeighted2DGraph<V, E, W, P> graph) {
		super(graph.vertexSupplier, graph.edgeSupplier, graph.edgeWeightSupplier, graph.pathSupplier);
		this.vertices = graph.vertices;
		this.edges = graph.copyEdges();
		this.paths = graph.copyPaths();
		this.sourceTargetAdjacencies = graph.sourceTargetAdjacencies;
		this.targetSourceAdjacencies = graph.targetSourceAdjacencies;
		this.edgeAdjacencies = graph.edgeAdjacencies;
	}

	public DirectedWeighted2DGraph<V, E, W, P> copy() {
		return new DirectedWeighted2DGraph<V, E, W, P>(this);
	}

	public E addEdge(V source, V target) {

		if (containsEdge(source, target))
			return null;

		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		W weight = edgeWeightSupplier.get();
		weight.setDistance(this.getDistance(source.getPosition(), target.getPosition()));
		edge.setWeight(weight);
		edges.add(edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		super.edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public E addEdge(V source, V target, W weight) {

		if (containsEdge(source, target))
			return null;

		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		edge.setWeight(weight);
		edges.add(edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		super.edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	// Returns a list of vertices that are related to a vertex via incoming or
	// outgoing edges
	public List<V> getConnectedVertices(V vertex) {
		Set<V> vertices = new HashSet<V>();

		// Gather all targets that are connected via an outgoing link with vertex
		// (vertex -> ?)
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			vertices.add(getVertex(adjacency.getSecond()));

		// Gather all vertices that are connected via an incoming link with vertex (? ->
		// vertex)
		for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
			vertices.add(getVertex(adjacency.getSecond()));

		return List.copyOf(vertices);
	}

	// Returns a list of incoming and outgoing edges related to a vertex
	public List<E> getEdgesOf(V vertex) {
		Set<E> edges = new HashSet<E>();
		edges.addAll(getOutgoingEdgesOf(vertex));
		edges.addAll(getIncomingEdgesOf(vertex));
		return List.copyOf(edges);
	}

	public List<E> getOutgoingEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> edgeVertexTuple : sourceTargetAdjacencies.get(vertex.getID()))
			edges.add(super.edges.get(edgeVertexTuple.getFirst()));
		return edges;
	}

	public List<E> getIncomingEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
			edges.add(super.edges.get(adjacency.getFirst()));
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

	@Override
	public List<E> getNeighboringEdgesOf(E edge) {

		Set<E> neighboringEdges = new HashSet<E>();

		Tuple<V, V> vertices = this.getVerticesOf(edge);

		for (E e : this.getEdgesOf(vertices.getFirst()))
			neighboringEdges.add(e);

		for (E e : this.getEdgesOf(vertices.getSecond()))
			neighboringEdges.add(e);

		return neighboringEdges.stream().collect(Collectors.toList());
	}
}
