package de.jgraphlib.graph.algorithms;

import java.util.List;

import de.jgraphlib.graph.Path;
import de.jgraphlib.graph.UndirectedWeightedGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class RandomPath<V extends Vertex<?>, E extends WeightedEdge<?>> {

	private UndirectedWeightedGraph<V, ?, E, ?> graph;

	public RandomPath(UndirectedWeightedGraph<V, ?, E, ?> graph) {
		this.graph = graph;
	}

	public Path<V, E> compute(V source, V target) {

		Path<V, E> randomPath = new Path<V, E>(source, target);

		while (!randomPath.isComplete()) {

			List<V> nextHops = randomPath.getUnvisitedVertices(graph.getNextHopsOf(randomPath.getLastVertex()));

			if (!nextHops.isEmpty()) {
				V nextHop = nextHops.get(RandomNumbers.getRandom(0, nextHops.size()));
				randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
			} else
				return new Path<V, E>(source, target);
		}
		return randomPath;
	}

	public Path<V, E> compute(V source, int hops) {

		Path<V, E> randomPath = new Path<V, E>(source, null);

		for (int i = 0; i < hops; i++) {

			List<V> nextHops = randomPath.getUnvisitedVertices(graph.getNextHopsOf(randomPath.getLastVertex()));

			if (!nextHops.isEmpty()) {
				V nextHop = nextHops.get(RandomNumbers.getRandom(0, nextHops.size()));
				randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
			} else
				return new Path<V, E>(source, null);
		}
		return randomPath;
	}
}
