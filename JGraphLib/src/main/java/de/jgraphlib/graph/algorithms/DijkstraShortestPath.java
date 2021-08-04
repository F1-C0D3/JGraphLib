package de.jgraphlib.graph.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Path2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class DijkstraShortestPath<V extends Vertex<?>, E extends WeightedEdge<W>, W> extends Path<V, E, W> {

	public DijkstraShortestPath(V source, V target) {
		super(source, target);
	}
	
	public DijkstraShortestPath() {}
	
	public void /*Path<V, E, W>*/ computeShortestPath(WeightedGraph<V, ?, E, ?, ?> graph, Function<W, Double> metric) {

		/* Initialization */
		V current = source;
		//Path<V, E, W> sp = new Path<V, E, W>(source, target);
				
		List<Integer> vertices = new ArrayList<Integer>();
		List<Tuple<V, Double>	> predDist = new ArrayList<Tuple<V, Double>>();

		for (V n : graph.getVertices()) {
			vertices.add(n.getID());

			if (n.getID() == current.getID()) 
				predDist.add(new Tuple<V, Double>(null, 0d));
			else 
				predDist.add(new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
		}

		while (!vertices.isEmpty()) {
			Integer nId = minDistance(predDist, vertices);
			vertices.remove(nId);
			current = graph.getVertex(nId);

			if (current.getID() == target.getID()) {
				//return generateSP(predDist, sp);;		
				generateSP(graph, predDist, this);
				return;
			}

			for (V neig : graph.getNextHopsOf(current)) {

				// double edgeDist = metric.apply(new Tuple<E, V>(graph.getEdge(current, neig),
				// neig));

				double edgeDist = metric.apply(graph.getEdge(current, neig).getWeight());
				double oldPahtDist = predDist.get(neig.getID()).getSecond();
				double altPathDist = edgeDist + predDist.get(current.getID()).getSecond();

				if (altPathDist < oldPahtDist) {
					predDist.get(neig.getID()).setFirst(current);
					predDist.get(neig.getID()).setSecond(altPathDist);
				}
			}
		}
		
		//sp.clear();
		this.clear();
		
		//return sp;
	}

	protected void /*Path<V, E, W>*/ generateSP(WeightedGraph<V, ?, E, ?, ?> graph, List<Tuple<V, Double>> predDist, Path<V, E, W> sp) {
		V t = sp.getTarget();
		List<Tuple<E, V>> copy = new ArrayList<Tuple<E, V>>();

		do {
			V pred = predDist.get(t.getID()).getFirst();

			if (pred == null) {
				return; //sp;
			}

			copy.add(0, new Tuple<E, V>(graph.getEdge(t, pred), t));
			t = pred;
		} while (t.getID() != sp.getSource().getID());

		sp.addAll(copy);

		//return sp;
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
