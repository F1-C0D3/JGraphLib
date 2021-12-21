
package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class DirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance, P extends Path<V, E, W>>
		extends Weighted2DGraph<V, E, W, P> {

	public DirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier,
			Supplier<P> pathSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier, pathSupplier);
	}

	public DirectedWeighted2DGraph(DirectedWeighted2DGraph<V, E, W, P> graph) {
		super(graph.vertexSupplier, graph.edgeSupplier, graph.edgeWeightSupplier, graph.pathSupplier);
		this.vertices = graph.copyVertices();
		this.edges = graph.copyEdges();
		this.paths = graph.copyPaths();

		this.sourceTargetAdjacencies = graph.copySourceTargetAdjacencies();
		this.targetSourceAdjacencies = graph.copyTargetSourceAdjacencies();
		this.edgeAdjacencies = graph.copyEdgeAdjacencies();
	}

	public DirectedWeighted2DGraph<V, E, W, P> copy() {
		return new DirectedWeighted2DGraph<V, E, W, P>(this);
	}

	public E addEdge(V source, V target) {

		if (containsEdge(source, target))
			return getEdge(source, target);

		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		W weight = edgeWeightSupplier.get();
		weight.setDistance(getDistance(source.getPosition(), target.getPosition()));
		edge.setWeight(weight);
		edges.put(edge.getID(), edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public E addEdge(V source, V target, W weight) {

		if (containsEdge(source, target))
			return getEdge(source, target);

		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		weight.setDistance(getDistance(source.getPosition(), target.getPosition()));
		edge.setWeight(weight);

		edges.put(edge.getID(), edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	// Returns a list of vertices that are related to a vertex via incoming or
	// outgoing edges
	public List<V> getConnectedVertices(V vertex) {
		Set<V> vertices = new HashSet<V>();

		// Gather all targets that are connected via an outgoing link with vertex
		// (vertex -> ?)
		
		if(sourceTargetAdjacencies.containsKey(vertex.getID()))
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			vertices.add(getVertex(adjacency.getSecond()));

		// Gather all vertices that are connected via an incoming link with vertex (? ->
		// vertex)
		if(targetSourceAdjacencies.containsKey(vertex.getID()));
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
		if (sourceTargetAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> edgeVertexTuple : sourceTargetAdjacencies.get(vertex.getID()))
				edges.add(super.edges.get(edgeVertexTuple.getFirst()));
		return edges;
	}

	public List<E> getIncomingEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		if (targetSourceAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
				edges.add(super.edges.get(adjacency.getFirst()));
		return edges;
	}

	public V getSourceOf(E edge) {
		if (edgeAdjacencies.containsKey(edge.getID()))
			return this.vertices.get(edgeAdjacencies.get(edge.getID()).getFirst());
		return null;
	}

	public V getTargetOf(E edge) {
		if (edgeAdjacencies.containsKey(edge.getID()))
			return this.vertices.get(edgeAdjacencies.get(edge.getID()).getSecond());
		return null;
	}

	public Boolean isDirected() {
		return true;
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

	public List<V> getVertices(List<Integer> vertexIDs) {

		List<V> vertices = new ArrayList<V>();

		for (Integer vertexID : vertexIDs)
			vertices.add(getVertex(vertexID));

		return vertices;
	}

	public P createPath(List<V> path) {

		P p = pathSupplier.get();
		p.set(path.get(0), path.get(path.size() - 1));

		// Check if graph contains edge of vertex i ~> i+1 & add edge to path
		for (int i = 0; i < path.size() - 1; i++)
			if (containsEdge(path.get(i), path.get(i + 1)))
				p.add(new Tuple<E, V>(getEdge(path.get(i), path.get(i + 1)), path.get(i + 1)));
			else
				return null;

		return p;
	}

	public void printAllPaths(V source, V target) {
		List<P> paths = getAllPaths(source, target);
		System.out.println(paths.size());
		for (P path : paths)
			System.out.println(path);
	}

	public void printAllPathsByVertexIDs(V source, V target) {
		List<List<Integer>> paths = getAllPathsByVertexIDs(source, target);
		System.out.println(paths.size());
		for (List<Integer> path : paths)
			System.out.println(path.toArray());
	}

	public List<List<Integer>> getAllPathsByVertexIDs(V source, V target) {

		List<List<Integer>> paths = new ArrayList<List<Integer>>();

		class DepthFirstTraversal {
			void recursiveCall(Integer currentID, Integer targetID, Set<Integer> visitedVertices, List<Integer> path) {

				if (currentID.equals(targetID)) {
					paths.add(path);
					return;
				}

				visitedVertices.add(currentID);

				for (V nextHop : getNextHopsOf(getVertex(currentID))) {
					if (!visitedVertices.contains(nextHop.getID())) {
						path.add(getVertex(nextHop).getID());

						List<Integer> newPath = new ArrayList<Integer>();
						newPath.addAll(path);

						recursiveCall(nextHop.getID(), target.getID(), visitedVertices, newPath);
						path.remove(path.size() - 1);
					}
				}

				visitedVertices.remove(currentID);
			}
		}

		new DepthFirstTraversal().recursiveCall(source.getID(), target.getID(), new HashSet<Integer>(),
				new ArrayList<Integer>(Arrays.asList(source.getID())));

		return paths;
	}

	public List<P> getAllPaths(V source, V target) {

		List<P> paths = new ArrayList<P>();
		P path = pathSupplier.get();
		path.set(source, target);

		class DepthFirstTraversal {
			void recursiveCall(V current, V target, Set<V> visitedVertices, P path) {

				if (current.equals(target)) {
					paths.add(path);
					return;
				}

				visitedVertices.add(current);

				for (V nextHop : getNextHopsOf(current)) {
					if (!visitedVertices.contains(nextHop)) {
						path.add(new Tuple<E, V>(getEdge(current, nextHop), nextHop));

						P newPath = pathSupplier.get();
						newPath.set(path.getSource(), path.getTarget());
						newPath.update(path);

						recursiveCall(nextHop, target, visitedVertices, newPath);

						path.removeLast();
					}
				}

				visitedVertices.remove(current);
			}
		}

		new DepthFirstTraversal().recursiveCall(source, target, new HashSet<V>(), path);

		return paths;
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
