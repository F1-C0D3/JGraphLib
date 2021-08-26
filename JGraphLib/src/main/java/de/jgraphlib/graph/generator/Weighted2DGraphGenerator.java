package de.jgraphlib.graph.generator;

import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.RandomNumbers;

public abstract class Weighted2DGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

	protected Log log;
	protected Weighted2DGraph<V, E, W, ?> graph;
	protected Supplier<W> edgeWeightSupplier;
	protected RandomNumbers random;

	public Weighted2DGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, RandomNumbers random) {
		this.log = new Log();
		this.graph = graph;
		this.random = random;
	}

	public Weighted2DGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, Supplier<W> edgeWeightSupplier,
			RandomNumbers random) {
		this.log = new Log();
		this.graph = graph;
		this.edgeWeightSupplier = edgeWeightSupplier;
		this.random = random;
	}

	public Boolean edgeWeightSupplier() {
		return edgeWeightSupplier != null;
	}

	protected void connectVerticesInRadius(V vertex, double radius) {
		List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
		for (V targetVertex : verticesInRadius)
			if (edgeWeightSupplier()) {
				W edgeWeight1 = edgeWeightSupplier.get();
				W edgeWeight2 = edgeWeightSupplier.get();
				edgeWeight1.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));
				edgeWeight2.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));
				graph.addEdge(vertex, targetVertex, edgeWeight1);
				graph.addEdge(targetVertex, vertex, edgeWeight2);
			} else {
				graph.addEdge(vertex, targetVertex);
				graph.addEdge(targetVertex, vertex);
			}
	}

	protected Position2D generateRandomPosition2D(V source, DoubleRange vertexDistanceRange) {

		Position2D position2D = null;
		double angleRadians, x, y;

		double distance = random.getRandom(vertexDistanceRange.min, vertexDistanceRange.max);
		double angleDegrees = random.getRandom(0d, 360d);

		if ((angleDegrees >= 0d) && (angleDegrees < 90d)) {
			angleRadians = Math.toRadians(angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			position2D = new Position2D(source.getPosition().x() + x, source.getPosition().y() + y);
		}

		if ((angleDegrees > 90d) && (angleDegrees <= 180d)) {
			angleRadians = Math.toRadians(180 - angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			position2D = new Position2D(source.getPosition().x() - x, source.getPosition().y() + y);
		}

		if ((angleDegrees > 180d) && (angleDegrees <= 270d)) {
			angleRadians = Math.toRadians(270 - angleDegrees);
			x = distance * Math.sin(angleRadians);
			y = distance * Math.cos(angleRadians);
			position2D = new Position2D(source.getPosition().x() - x, source.getPosition().y() - y);
		}

		if ((angleDegrees > 270d) && (angleDegrees <= 360d)) {
			angleRadians = Math.toRadians(360 - angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			position2D = new Position2D(source.getPosition().x() + x, source.getPosition().y() - y);
		}

		return position2D;
	}
}
