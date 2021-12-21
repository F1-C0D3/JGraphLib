package de.jgraphlib.graph;

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

public class UndirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance, P extends Path<V, E, W>>
		extends Weighted2DGraph<V, E, W, P> {

	public UndirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier,
			Supplier<W> edgeWeightSupplier, Supplier<P> pathSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier, pathSupplier);
	}

	public UndirectedWeighted2DGraph(UndirectedWeighted2DGraph<V, E, W, P> graph) {
		super(graph.vertexSupplier, graph.edgeSupplier, graph.edgeWeightSupplier, graph.pathSupplier);
		this.edgeWeightSupplier = graph.edgeWeightSupplier;
		this.vertices = graph.vertices;
		// this.edges = graph.copyEdges(); // deep copy edges
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
		edges.put(edge.getID(), edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public E addEdge(V source, V target, W weight) {

		if (containsEdge(source, target))
			return null;

		E edge = edgeSupplier.get();
		edge.setID(edgeCount);
		edgeCount++;
		edge.setWeight(weight);
		edges.put(edge.getID(), edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public List<E> getEdgesOf(V vertex) {

		Set<E> edges = new HashSet<E>();
		
		if (this.sourceTargetAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
				edges.add(this.edges.get(adjacency.getFirst()));

		if (targetSourceAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
				edges.add(this.edges.get(adjacency.getFirst()));

		return List.copyOf(edges);
	}

	public List<E> getOutgoingEdgesOf(V vertex) {
		return getEdgesOf(vertex);
	}

	public Tuple<V, V> getVerticesOf(E edge) {
		if(edgeAdjacencies.containsKey(edge.getID()))
		return new Tuple<V, V>(getVertex(edgeAdjacencies.get(edge.getID()).getFirst()),
				getVertex(edgeAdjacencies.get(edge.getID()).getSecond()));
		return null;
	}

	public Boolean isDirected() {
		return false;
	}

	public V getTargetOf(V vertex, E edge) {
		if (this.edgeAdjacencies.containsKey(vertex.getID())) {
			Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
			if (vertex.getID() == vertexIDs.getFirst())
				return this.vertices.get(vertexIDs.getSecond());
			else if (vertex.getID() == vertexIDs.getSecond())
				return this.vertices.get(vertexIDs.getFirst());
		}
		return null;
	}

	@Override
	public List<E> getNeighboringEdgesOf(E edge) {

		Set<E> neighboringEdges = new HashSet<E>();

		Tuple<V, V> vertices = this.getVerticesOf(edge);

		if (vertices != null) {

			for (E e : this.getEdgesOf(vertices.getFirst()))
				neighboringEdges.add(e);

			for (E e : this.getEdgesOf(vertices.getSecond()))
				neighboringEdges.add(e);
		}
		return neighboringEdges.stream().collect(Collectors.toList());
	}

	@Override
	public boolean removeEdge(E edge) {

		Tuple<V, V> sourceAndSink = this.getVerticesOf(edge);
		int sourceID = sourceAndSink.getFirst().getID();
		int sinkID = sourceAndSink.getSecond().getID();

		this.sourceTargetAdjacencies.get(sourceID).removeIf(tuple -> tuple.getSecond() == sinkID);
		this.targetSourceAdjacencies.get(sinkID).removeIf(tuple -> tuple.getSecond() == sourceID);

		if (this.sourceTargetAdjacencies.get(sourceID).isEmpty())
			this.removeVertex(sourceAndSink.getFirst());

		if (targetSourceAdjacencies.get(sinkID).isEmpty())
			this.removeVertex(sourceAndSink.getSecond());

		this.edgeAdjacencies.remove(edge.getID());
		this.edges.remove(edge.getID());

		return true;
	}

}
