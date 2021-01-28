package de.jgraphlib.graph.generator;

import java.util.List;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeWeightSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.RandomNumbers;

public abstract class Weighted2DGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

	protected Log log;
	protected UndirectedWeighted2DGraph<V, E, W> graph;
	protected EdgeWeightSupplier<W> edgeWeightSupplier;

	public Weighted2DGraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph) {
		this.log = new Log();
		this.graph = graph;
	}

	public Weighted2DGraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph,
			EdgeWeightSupplier<W> edgeWeightSupplier) {
		this.log = new Log();
		this.graph = graph;
		this.edgeWeightSupplier = edgeWeightSupplier;
	}

	public Boolean edgeWeightSupplier() {
		return edgeWeightSupplier != null;
	}

	protected void connectVerticesInRadius(V vertex, double radius) {
		List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
		for (V targetVertex : verticesInRadius)
			if (edgeWeightSupplier()) {
				W edgeWeight = edgeWeightSupplier.get();
				edgeWeight.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));
				graph.addEdge(vertex, targetVertex, edgeWeight);
			} else
				graph.addEdge(vertex, targetVertex);
	}

	protected Position2D generateRandomPosition2D(V source, DoubleRange vertexDistanceRange) {

		Position2D Position2D = null;
		double angleRadians, x, y;

		double distance = RandomNumbers.getRandom(vertexDistanceRange.min, vertexDistanceRange.max);
		double angleDegrees = RandomNumbers.getRandom(0d, 360d);

		if ((angleDegrees >= 0d) && (angleDegrees < 90d)) {
			angleRadians = Math.toRadians(angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			Position2D = new Position2D(source.getPosition().x() + x, source.getPosition().y() + y);
		}

		if ((angleDegrees > 90d) && (angleDegrees <= 180d)) {
			angleRadians = Math.toRadians(180 - angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			Position2D = new Position2D(source.getPosition().x() - x, source.getPosition().y() + y);
		}

		if ((angleDegrees > 180d) && (angleDegrees <= 270d)) {
			angleRadians = Math.toRadians(270 - angleDegrees);
			x = distance * Math.sin(angleRadians);
			y = distance * Math.cos(angleRadians);
			Position2D = new Position2D(source.getPosition().x() - x, source.getPosition().y() - y);
		}

		if ((angleDegrees > 270d) && (angleDegrees <= 360d)) {
			angleRadians = Math.toRadians(360 - angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			Position2D = new Position2D(source.getPosition().x() + x, source.getPosition().y() - y);
		}

		return Position2D;
	}
}
