package de.jgraphlib.generator;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.RandomNumbers;

public abstract class Weighted2DGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

	protected Log log;
	protected Weighted2DGraph<V, E, W, ?> graph;
	protected Supplier<W> edgeWeightSupplier;
	protected RandomNumbers random;

	public Weighted2DGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, Supplier<W> edgeWeightSupplier, RandomNumbers random) {
		this.log = new Log();
		this.graph = graph;
		this.edgeWeightSupplier = edgeWeightSupplier;
		this.random = random;
	}

	public Boolean edgeWeightSupplier() {
		return edgeWeightSupplier != null;
	}

	protected void connectVerticesInRadius(V vertex, double radius, EdgeStyle edgeStyle, List<V> blacklist) {
		List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
		verticesInRadius.removeAll(blacklist);
		connectVertexWithVertices(vertex, verticesInRadius, edgeStyle);
	}

	protected void connectVerticesInRadius(V vertex, double radius, EdgeStyle edgeStyle) {
		List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
		connectVertexWithVertices(vertex, verticesInRadius, edgeStyle);
	}

	private void connectVertexWithVertices(V vertex, List<V> vertices, EdgeStyle edgeStyle) {
		
		double distance = graph.getDistance(vertex.getPosition(), vertices.get(0).getPosition());				
		W edgeWeight = edgeWeightSupplier.get();					
		edgeWeight.setDistance(distance);						
		graph.addEdge(vertices.get(0), vertex, edgeWeight);	
		
		switch(edgeStyle) {
			case BIDIRECTIONAL:				
				edgeWeight = edgeWeightSupplier.get();
				edgeWeight.setDistance(distance);
				graph.addEdge(vertex, vertices.get(0), edgeWeight);
				break;
			default: 
				break;
		}
		
		for (V targetVertex : vertices.subList(1, vertices.size())) {						
			if (!targetVertex.equals(vertex))				
				if (edgeWeightSupplier()) {																
					switch(edgeStyle) {
						case UNIDIRECTIONAL:					
							edgeWeight = edgeWeightSupplier.get();
							edgeWeight.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));							
							if (new Random().nextBoolean()) 
								graph.addEdge(vertex, targetVertex, edgeWeight);						
							else 
								graph.addEdge(targetVertex, vertex, edgeWeight);						
							break;
						case BIDIRECTIONAL:				
							edgeWeight = edgeWeightSupplier.get();
							edgeWeight.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));
							graph.addEdge(targetVertex, vertex, edgeWeight);							
							edgeWeight = edgeWeightSupplier.get();
							edgeWeight.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));
							graph.addEdge(vertex, targetVertex, edgeWeight);							
							break;
					default:
						break;
					}		
				} else {
					graph.addEdge(vertex, targetVertex);
					
					switch(edgeStyle) {
						case BIDIRECTIONAL:
							graph.addEdge(targetVertex, vertex);
							break;
					default:
						break;
					}
				}
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
