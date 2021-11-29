package de.jgraphlib.graph.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class YenTopKShortestPaths<V extends Vertex<?>, E extends WeightedEdge<W>, W> {

	private List<Path<V, E, W>> candidates;
	private List<Path<V, E, W>> ksp;

	private List<Set<Integer>> removedEdgeIDs;
	private int spurPos;
	private int round;
	private int numTopK;

	private WeightedGraph<V, ?, E, ?, ?> graph;

	public YenTopKShortestPaths(WeightedGraph<V, ?, E, ?, ?> graph, int numTopK) {
		this.graph = graph;
		this.numTopK = numTopK;
		init();

	}

	/**
	 * Initiate members in the class.
	 */
	private void init() {

		this.spurPos = 0;
		this.round = 0;
		this.candidates = new ArrayList<Path<V, E, W>>();
		this.ksp = new ArrayList<Path<V, E, W>>();

		this.removedEdgeIDs = new ArrayList<Set<Integer>>();
		for (int i = 0; i < this.graph.getVertices().size(); i++) {
			removedEdgeIDs.add(new HashSet<Integer>());
		}

	}

	public List<Path<V, E, W>> compute(V source, V target, Function<W, Double> metric,
			Function<Tuple<Path<V, E, W>, Path<V, E, W>>, Double> pathComperator) {
		init();
		ksp.add(new DijkstraShortestPath<V, E, W>(graph.copy()).compute(source, target, metric, null));

		while (round < (this.numTopK - 1)) {

			Path<V, E, W> previousPath = ksp.get(round);

			while (spurPos != previousPath.size() - 1) {

				V spurNode = previousPath.get(spurPos).getSecond();

				// Root path = prefix portion of the (k-1)st path up to the spur node
				Path<V, E, W> rootPath = new Path<V, E, W>(previousPath);
				rootPath.cropUntil(spurNode);

				WeightedGraph<V, ?, E, ?, ?> graphCopy = this.graph.copy();
				/* Iterate over all of the (k-1) shortest paths */
				for (Path<V, E, W> p : ksp) {

					Path<V, E, W> stub = new Path<V, E, W>(p);

					stub.cropUntil(spurPos >= p.size() ? p.getLastVertex() : stub.getVertices().get(spurPos));
					// Check to see if this path has the same prefix/root as the (k-1)st shortest
					// path
					if (rootPath.equals(stub)) {
						/*
						 * If so, eliminate the next edge in the path from the graph (later on, this
						 * forces the spur node to connect the root path with an un-found suffix path)
						 */
						if (p.size() - 1 != spurPos) {
							V intermediateSource = spurNode;
							V nextHop = p.get(spurPos + 1).getSecond();

							List<E> outgoingEdges = graphCopy.getOutgoingEdgesOf(intermediateSource);

							for (E e : outgoingEdges) {

								V sinkOfE = graphCopy.getVerticesOf(e).getSecond();

								if (sinkOfE.getID() == nextHop.getID() && graphCopy.getEdge(e) != null) {
									graphCopy.removeEdge(e);
									break;
								}
							}

						}

						for (Tuple<E, V> pathTuple : rootPath) {

							if (pathTuple.getSecond().getID() != spurNode.getID()) {
								graphCopy.removeVertex(pathTuple.getSecond());
							}
						}
					}
				}

				Path<V, E, W> spurPath = new DijkstraShortestPath<V, E, W>(graphCopy).compute(spurNode, target, metric);

				// If a new spur path was identified...
				if (!spurPath.isEmpty()) {
					// Concatenate the root and spur paths to form the new candidate path
					Path<V, E, W> totalPath = new Path<V, E, W>(rootPath);
					totalPath.concat(spurPath);

					// If candidate path has not been generated previously, add it
					if (!candidates.contains(totalPath)) {
						candidates.add(totalPath);
					}

				}
				spurPos++;

			}

			spurPos = 0;
			if (candidates.isEmpty()) {
				return toOriginalPaths();
			}

			double currentScore = Double.MAX_VALUE;
			int candidateIndex = 0;

			for (Path<V, E, W> candidate : candidates) {

				for (Path<V, E, W> sp : ksp) {

					double score = pathComperator.apply(new Tuple<Path<V, E, W>, Path<V, E, W>>(sp, candidate));
					if (currentScore > score) {

						currentScore = score;
						candidateIndex = candidates.indexOf(candidate);
					}
				}
			}

			ksp.add(++round, candidates.remove(candidateIndex));
		}
		return toOriginalPaths();
	}

	private List<Path<V, E, W>> toOriginalPaths() {
		List<Path<V, E, W>> originalSPs = new ArrayList<Path<V,E,W>>();
		for (Path<V, E, W> sp : ksp) {

			Path<V, E, W> osp = new Path<V, E, W>(graph.getVertex(sp.getSource().getID()),
					graph.getVertex(sp.getTarget().getID()));

			for (Tuple<E, V> link : sp) {
				if (link.getFirst() != null) 
					osp.add(new Tuple<E, V>(graph.getEdge(link.getFirst().getID()),
							graph.getVertex(link.getSecond().getID())));
				
			}
			originalSPs.add(osp);
		}
		return originalSPs;
	}
}
