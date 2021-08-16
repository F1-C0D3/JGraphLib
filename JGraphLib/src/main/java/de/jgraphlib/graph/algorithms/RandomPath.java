package de.jgraphlib.graph.algorithms;

import java.util.List;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class RandomPath<V extends Vertex<?>, E extends WeightedEdge<W>, W> {

	private WeightedGraph<V, ?, E, W, ?> graph;

	public RandomPath(WeightedGraph<V, ?, E, W, ?> graph) {
		this.graph = graph;
	}

	public Path<V, E, W> compute(V source, V target) {

		Path<V, E, W> randomPath = new Path<V, E, W>(source, target);

		while (!randomPath.isComplete()) {

			List<V> nextHops = randomPath.getUnvisitedVerticesOf(graph.getNextHopsOf(randomPath.getLastVertex()));

			if (!nextHops.isEmpty()) {
				V nextHop = nextHops.get(RandomNumbers.getInstance(-1).getRandom(0, nextHops.size()));
				randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
			} else
				return new Path<V, E, W>(source, target);
		}
		return randomPath;
	}

	public Path<V, E, W> compute(V source, int hops) {

		Path<V, E, W> randomPath = new Path<V, E, W>(source, null);

		for (int i = 0; i < hops; i++) {

			List<V> nextHops = randomPath.getUnvisitedVerticesOf(graph.getNextHopsOf(randomPath.getLastVertex()));

			if (!nextHops.isEmpty()) {
				V nextHop = nextHops.get(new RandomNumbers().getRandom(0, nextHops.size()));
				randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
			} else
				return new Path<V, E, W>(source, null);
		}
		return randomPath;
	}
}
