package de.jgraphlib.graph.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class DijkstraShortestPath<V extends Vertex<?>, E extends WeightedEdge<W>, W> {

	protected WeightedGraph<V, ?, E, ?, ?> graph;

	public DijkstraShortestPath(WeightedGraph<V, ?, E, ?, ?> graph) {
		this.graph = graph;
	}

	public DijkstraShortestPath() {}

	public Path<V, E, W> compute(V source, V target, Function<E, Double> metric) {

		/* Initializaton */
		V current = source;
		Path<V, E, W> sp = new Path<V, E, W>(source, target);
		List<Integer> vertices = new ArrayList<Integer>();
		List<Tuple<V, Double>> predDist = new ArrayList<Tuple<V, Double>>();

		for (V n : graph.getVertices()) {
			vertices.add(n.getID());

			if (n.getID() == current.getID()) {
				predDist.add(new Tuple<V, Double>(null, 0d));
			} else {
				predDist.add(new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
			}
		}

		while (!vertices.isEmpty()) {
			Integer nId = minDistance(predDist, vertices);
			vertices.remove(nId);
			current = graph.getVertex(nId);

			if (current.getID() == target.getID()) {
				return generateSP(predDist, sp);
			}

			for (V neig : graph.getNextHopsOf(current)) {

				double edgeDist = metric.apply(graph.getEdge(current, neig));

				double oldPahtDist = predDist.get(neig.getID()).getSecond();
				double altPathDist = edgeDist + predDist.get(current.getID()).getSecond();

				if (altPathDist < oldPahtDist) {
					predDist.get(neig.getID()).setFirst(current);
					predDist.get(neig.getID()).setSecond(altPathDist);
				}
			}
		}
		sp.clear();
		return sp;
	}

	protected Path<V, E, W> generateSP(List<Tuple<V, Double>> predDist, Path<V, E, W> sp) {
		V t = sp.getTarget();
		List<Tuple<E, V>> copy = new ArrayList<Tuple<E, V>>();

		do {
			V pred = predDist.get(t.getID()).getFirst();

			if (pred == null) {
				return sp;
			}

			// Dieser Fix hat mich locker 2 Stunden gekostet xD
			//copy.add(0, new Tuple<E, V>(graph.getEdge(t, pred), t));
			copy.add(0, new Tuple<E, V>(graph.getEdge(pred, t), t));

			t = pred;
		} while (t.getID() != sp.getSource().getID());

		sp.addAll(copy);

		return sp;
	}

	protected Integer minDistance(List<Tuple<V, Double>> predT, List<Integer> v) {
		int id = -1;
		double result = Double.POSITIVE_INFINITY;
		ListIterator<Tuple<V, Double>> it = predT.listIterator();

		while (it.hasNext()) {
			Tuple<V, Double> pred = it.next();

			if (v.contains(it.previousIndex()) && pred.getSecond() < result) {
				result = pred.getSecond();
				id = it.previousIndex();
			}
		}
		return id;
	}
}
