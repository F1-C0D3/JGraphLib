package de.jgraphlib.graph.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;
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

	public Path<V, E, W> compute(V source, V target, Function<W, Double> metric) {
		return compute(source, target, metric, null);
	}

	public Path<V, E, W> compute(V source, V target, Function<W, Double> metric, List<E> exclusionList) {

		/* Initializaton */
		V current = source;
		Path<V, E, W> sp = new Path<V, E, W>(source, target);
		List<Integer> vertices = new ArrayList<Integer>();
		TreeMap<Integer, Tuple<Integer, Double>> predDist = new TreeMap<Integer, Tuple<Integer, Double>>();

		for (V n : graph.getVertices()) {
			vertices.add(n.getID());

			if (n.getID() == current.getID()) {
				predDist.put(n.getID(), new Tuple<Integer, Double>(null, 0d));
			} else {
				predDist.put(n.getID(), new Tuple<Integer, Double>(null, Double.POSITIVE_INFINITY));
			}
		}

		while (!vertices.isEmpty()) {
			Integer nID = minDistance(predDist, vertices);
			if(nID==-1)
				return sp;
			vertices.remove(nID);
			current = graph.getVertex(nID);

			if (current.getID() == target.getID()) {
				return generateSP(predDist, sp);
			}

			for (V neig : graph.getNextHopsOf(current)) {

				E currentLink = graph.getEdge(current, neig);
				double edgeDist = metric.apply(graph.getEdge(current, neig).getWeight());

				double oldPahtDist = predDist.get(neig.getID()).getSecond();
				double altPathDist = edgeDist + predDist.get(current.getID()).getSecond();

				if (altPathDist < oldPahtDist) {
					predDist.get(neig.getID()).setFirst(current.getID());
					predDist.get(neig.getID()).setSecond(altPathDist);
				}
			}
		}
		sp.clear();
		return sp;
	}

	protected Path<V, E, W> generateSP(TreeMap<Integer, Tuple<Integer, Double>> predDist, Path<V, E, W> sp) {
		V t = sp.getTarget();
		List<Tuple<E, V>> copy = new ArrayList<Tuple<E, V>>();

		do {
			Integer pred = predDist.get(t.getID()).getFirst();

			if (pred == null) {
				return sp;
			}

			// Dieser Fix hat mich locker 2 Stunden gekostet xD
			// copy.add(0, new Tuple<E, V>(graph.getEdge(t, pred), t));
			V predV = graph.getVertex(pred);
			copy.add(0, new Tuple<E, V>(graph.getEdge(predV, t), t));

			t = predV;
		} while (t.getID() != sp.getSource().getID());

		sp.addAll(copy);

		return sp;
	}

	protected Integer minDistance(TreeMap<Integer, Tuple<Integer, Double>> predT, List<Integer> v) {
		int id = -1;
		double result = Double.POSITIVE_INFINITY;

		for (Integer vID : predT.keySet()) {
			
			Double distance = predT.get(vID).getSecond();
			if (v.contains(vID) && distance < result) {
				
				result = distance;
				id = vID;
			}
		}
		return id;
	}
}
