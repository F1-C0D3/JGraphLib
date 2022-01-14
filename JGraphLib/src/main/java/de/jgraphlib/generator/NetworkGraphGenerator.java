package de.jgraphlib.generator;

import java.util.function.Supplier;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class NetworkGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public NetworkGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, Supplier<W> edgeWeightSupplier, RandomNumbers random) {
		super(graph, edgeWeightSupplier, random);
	}
	
	public int generate(NetworkGraphProperties properties) {

		// Generate number of vertices randomly in range of getVertexCount property
		int numberOfVertices = random.getRandom(properties.getVertexCount().min, properties.getVertexCount().max);
		
		int vertexCount = 0, attemps = 0;
		
		// Create an initial vertex
		V currentVertex = graph.addVertex(
				random.getRandom(properties.getWidth().min, properties.getWidth().max), 
				random.getRandom(properties.getHeight().min, properties.getHeight().max));

		while (vertexCount < numberOfVertices && attemps < 100) {

			// Generate a random position that is within range of vertexDistance property
			Position2D Position2D = getRandomPosition(currentVertex.getPosition(), properties.getVertexDistance());
			
			// Check if generated random position is within playground and there is no other vertex in vertexDistance.min property
			if (properties.isInside(Position2D.x(), Position2D.y())
					&& !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {

				// Add a new vertex to graph at generated random position
				V newVertex = graph.addVertex(Position2D.x(), Position2D.y());
				vertexCount++;
				attemps = 0;

				// Connect new vertex with all vertices that are within edgeDistance.max distance property
				if (properties.getVertexDistance().min <= properties.getEdgeDistance().max)
					connectVerticesInRadius(newVertex, properties.getEdgeDistance().max, properties.getEdgeStyle());
				
				// Mark new vertex
				currentVertex = newVertex;
			} else
				// Mark a random vertex of current graph
				currentVertex = graph.getVertex(random.getRandom(0, vertexCount));

			attemps++;
		}
		
		return vertexCount;
	}
}