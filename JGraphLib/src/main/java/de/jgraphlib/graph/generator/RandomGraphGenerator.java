package de.jgraphlib.graph.generator;

import java.util.List;
import java.util.Random;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeWeightSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class RandomGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public RandomGraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph) {
		super(graph);
	}

	public RandomGraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph, EdgeWeightSupplier<W> edgeWeightSupplier) {
		super(graph, edgeWeightSupplier);
	}

	public int generate(GraphProperties properties) {

		int numberOfVertices = RandomNumbers.getRandom(properties.getVertexCount().min,
				properties.getVertexCount().max);
		int vertexCount = 0;
		int attemps = 0;
		log.info(String.format("GraphProperties: (%s)", properties.toString()));

		if (properties.getVertexDistance().min > properties.getEdgeDistance().max)
			log.warning(
					"Given GraphProperties allows no edges because arguments have condition vertexDistance.min > edgeDistance.max");

		// Add first vertex at a random position within GraphProperties
		/*
		 * Vertex<Position2D> * currentVertex =
		 * graph.addVertex(RandomNumbers.getRandom(properties.width.min,
		 * properties.width.max), RandomNumbers.getRandom(properties.height.min,
		 * properties.height.max));
		 */

		V currentVertex = graph.addVertex(0, 0);
		vertexCount++;
		log.info(String.format("Added Vertex %d at %s", vertexCount, graph.getFirstVertex().getPosition().toString()));

		// As long as graph's size doesn't match GraphProperties requirements
		// (vertexCount),
		// process vertex generation process
		while (vertexCount < numberOfVertices && attemps < 100) {

			// Search a Position2D in GraphProperties which matches vertexDistance
			// requirements
			Position2D Position2D = generateRandomPosition2D(currentVertex, properties.getVertexDistance());

			if (properties.isInside(Position2D.x(), Position2D.y())
					&& !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {

				// Add a new vertex to graph
				V newVertex = graph.addVertex(Position2D.x(), Position2D.y());
				vertexCount++;
				attemps = 0;
				this.log.info(String.format("Added Vertex %d at %s", vertexCount, newVertex.getPosition().toString()));

				// If..

				int edgeCount;

				if (properties.getVertexDistance().min <= properties.getEdgeDistance().max) {
					graph.addEdge(currentVertex, newVertex);
					edgeCount = RandomNumbers.getRandom(properties.getEdgeCount().min,
							properties.getEdgeCount().max - 1);
				} else
					edgeCount = RandomNumbers.getRandom(properties.getEdgeCount().min, properties.getEdgeCount().max);

				// Update currentVertex with newVertex
				currentVertex = newVertex;

				// Generate edges
				generateEdges(newVertex, edgeCount, properties);

			} else {
				// Take a random vertex from graph
				currentVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));
			}

			attemps++;
		}

		return vertexCount;
	}

	private void generateEdges(V source, int edgeCount, GraphProperties properties) {

		// (1) Radius to gather vertices in environment, specified by a randomly chosen
		// number in interval [properties.edgeDistance.min, properties.edgeDistance.max]
		double edgeDistance = RandomNumbers.getRandom(properties.getEdgeDistance().min,
				properties.getEdgeDistance().max);

		// (2) Gather all vertices in radius, specified by a randomly chosen radius in
		// interval [properties.edgeDistance.min, properties.edgeDistance.max]
		List<V> verticesInRadius = graph.getVerticesInRadius(source, edgeDistance);

		if (verticesInRadius.size() > 0) {

			// (3) Select n vertices randomly from vertex environment
			List<V> randomVertices = RandomNumbers.selectNrandomOfM(verticesInRadius, edgeCount, new Random());

			// (4) Add edges
			for (V target : randomVertices)
				// Only add edge while target node's number of edges is below requirement given
				// by properties.edgeCount.max
				if (graph.getEdgesOf(target).size() < properties.getEdgeCount().max)
					graph.addEdge(source, target);
		}
	}

}
