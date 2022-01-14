package de.jgraphlib.generator;

import java.util.function.Supplier;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class GridGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public GridGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, Supplier<W> edgeWeightSupplier,
			RandomNumbers random) {
		super(graph, edgeWeightSupplier, random);
	}

	public void generate(GridGraphProperties properties) {

		V currentVertex = graph.addVertex(0, 0);
		int vertexCount = 1;

		while ((currentVertex.getPosition().x() <= properties.getWidth().max - properties.getVertexDistance().max)) {
			if (vertexCount > 1) {
				double xOffset = random.getRandom(properties.getVertexDistance().min,
						properties.getVertexDistance().max);
				V newVertex = graph.addVertex(currentVertex.getPosition().x() + xOffset, 0);
				currentVertex = newVertex;
				vertexCount++;
				 connectVerticesInRadius(newVertex, properties.getEdgeDistance().max, properties.getEdgeStyle());
			}

			while (currentVertex.getPosition()
					.y() <= (properties.getHeight().max - properties.getVertexDistance().max)) {
				double yOffset = random.getRandom(properties.getVertexDistance().min,
						properties.getVertexDistance().max);
				V newVertex = graph.addVertex(currentVertex.getPosition().x(),
						currentVertex.getPosition().y() + yOffset);
				currentVertex = newVertex;
				vertexCount++;
				connectVerticesInRadius(newVertex, properties.getEdgeDistance().max, properties.getEdgeStyle());
			}
		}
	}
}
