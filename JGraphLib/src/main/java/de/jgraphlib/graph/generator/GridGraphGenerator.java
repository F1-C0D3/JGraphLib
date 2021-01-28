package de.jgraphlib.graph.generator;

import java.util.List;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeWeightSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class GridGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public GridGraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph) {
		super(graph);
	}

	public GridGraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph, EdgeWeightSupplier<W> edgeWeightSupplier) {
		super(graph, edgeWeightSupplier);
	}

	public void generate(GridGraphProperties properties) {

		V currentVertex = graph.addVertex(0, 0);
		int vertexCount = 1;

		while ((currentVertex.getPosition().x() <= properties.getWidth().max - properties.getVertexDistance().max)) {
			if (vertexCount > 1) {
				double xOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
						properties.getVertexDistance().max);
				V newVertex = graph.addVertex(currentVertex.getPosition().x() + xOffset, 0);
				currentVertex = newVertex;
				vertexCount++;
				List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
				for (V target : verticesInRadius)
					graph.addEdge(newVertex, target);
			}

			while (currentVertex.getPosition()
					.y() <= (properties.getHeight().max - properties.getVertexDistance().max)) {
				double yOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
						properties.getVertexDistance().max);
				V newVertex = graph.addVertex(currentVertex.getPosition().x(),
						currentVertex.getPosition().y() + yOffset);
				currentVertex = newVertex;
				vertexCount++;
				List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
				for (V target : verticesInRadius)
					graph.addEdge(newVertex, target);
			}
		}
	}
}
