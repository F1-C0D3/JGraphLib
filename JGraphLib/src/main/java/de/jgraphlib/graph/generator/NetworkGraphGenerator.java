package de.jgraphlib.graph.generator;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.EdgeWeightSupplier;
import de.jgraphlib.util.RandomNumbers;

public class NetworkGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public NetworkGraphGenerator(Weighted2DGraph<V, E, W> graph, RandomNumbers random) {
		super(graph, random);
	}

	public NetworkGraphGenerator(Weighted2DGraph<V, E, W> graph, EdgeWeightSupplier<W> edgeWeightSupplier, RandomNumbers random) {
		super(graph, edgeWeightSupplier, random);
	}
	

	public int generate(NetworkGraphProperties properties) {

		int numberOfVertices = random.getRandom(properties.getVertexCount().min, properties.getVertexCount().max);
		int vertexCount = 0, attemps = 0;
		V currentVertex = graph.addVertex(
				random.getRandom(properties.getWidth().min, properties.getWidth().max), 
				random.getRandom(properties.getHeight().min, properties.getHeight().max));

		while (vertexCount < numberOfVertices && attemps < 100) {

			Position2D Position2D = generateRandomPosition2D(currentVertex, properties.getVertexDistance());
			
			if (properties.isInside(Position2D.x(), Position2D.y())
					&& !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {

				V newVertex = graph.addVertex(Position2D.x(), Position2D.y());
				vertexCount++;
				attemps = 0;

				if (properties.getVertexDistance().min <= properties.getEdgeDistance().max)
					connectVerticesInRadius(newVertex, properties.getEdgeDistance().max);

				currentVertex = newVertex;
			} else
				currentVertex = graph.getVertex(random.getRandom(0, vertexCount));

			attemps++;
		}
		
		return vertexCount;
	}
}