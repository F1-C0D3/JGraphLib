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

	public Weighted2DGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, Supplier<W> edgeWeightSupplier, RandomNumbers random) {
		this.log = new Log();
		this.graph = graph;
		this.edgeWeightSupplier = edgeWeightSupplier;
		this.random = random;
	}

	public Boolean edgeWeightSupplier() {
		return edgeWeightSupplier != null;
	}

	protected void connectVerticesInRadius(V vertex, double radius, List<V> blacklist) {
		List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
		verticesInRadius.removeAll(blacklist);
		connectVertexWithVertices(vertex, verticesInRadius);
	}
	
	protected void connectVerticesInRadius(V vertex, double radius) {
		List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
		connectVertexWithVertices(vertex, verticesInRadius);
	}
	
	private void connectVertexWithVertices(V vertex, List<V> vertices) {
	
		for (V targetVertex : vertices)
			if (edgeWeightSupplier()) {
				W edgeWeightEdgeAway = edgeWeightSupplier.get();
				W edgeWeightEdgeWayBack = edgeWeightSupplier.get();
				double distance = graph.getDistance(vertex.getPosition(), targetVertex.getPosition());
				edgeWeightEdgeAway.setDistance(distance);
				edgeWeightEdgeWayBack.setDistance(distance);
				graph.addEdge(vertex, targetVertex, edgeWeightEdgeAway);
				graph.addEdge(targetVertex, vertex, edgeWeightEdgeWayBack);
			} else {
				graph.addEdge(vertex, targetVertex);
				graph.addEdge(targetVertex, vertex);
			}	
	}

	protected Position2D getRandomPosition(Position2D source, double vertexDistance) {
		return getRandomPosition(source, new DoubleRange(vertexDistance, vertexDistance));
	}
	
	protected Position2D getRandomPosition(Position2D source, DoubleRange vertexDistanceRange) {

		Position2D position2D = null;
		double angleRadians, x, y;

		double distance = random.getRandom(vertexDistanceRange.min, vertexDistanceRange.max);
		double angleDegrees = random.getRandom(0d, 360d);

		if ((angleDegrees >= 0d) && (angleDegrees < 90d)) {
			angleRadians = Math.toRadians(angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			position2D = new Position2D(source.x() + x, source.y() + y);
		}

		if ((angleDegrees > 90d) && (angleDegrees <= 180d)) {
			angleRadians = Math.toRadians(180 - angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			position2D = new Position2D(source.x() - x, source.y() + y);
		}

		if ((angleDegrees > 180d) && (angleDegrees <= 270d)) {
			angleRadians = Math.toRadians(270 - angleDegrees);
			x = distance * Math.sin(angleRadians);
			y = distance * Math.cos(angleRadians);
			position2D = new Position2D(source.x() - x, source.y() - y);
		}

		if ((angleDegrees > 270d) && (angleDegrees <= 360d)) {
			angleRadians = Math.toRadians(360 - angleDegrees);
			x = distance * Math.cos(angleRadians);
			y = distance * Math.sin(angleRadians);
			position2D = new Position2D(source.x() + x, source.y() - y);
		}

		return position2D;
	}
}
