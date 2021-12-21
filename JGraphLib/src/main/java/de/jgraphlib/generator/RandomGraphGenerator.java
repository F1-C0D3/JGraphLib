package de.jgraphlib.generator;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class RandomGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public RandomGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, RandomNumbers random) {
		super(graph, random);
	}

	public RandomGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, Supplier<W> edgeWeightSupplier,
			RandomNumbers random) {
		super(graph, edgeWeightSupplier, random);
	}

	public int generate(GraphProperties properties) {

		int numberOfVertices = random.getRandom(properties.getVertexCount().min, properties.getVertexCount().max);
		int vertexCount = 0;
		int attemps = 0;
		log.info(String.format("GraphProperties: (%s)", properties.toString()));

		if (properties.getVertexDistance().min > properties.getEdgeDistance().max)
			log.warning(
					"Given GraphProperties allows no edges because arguments have condition vertexDistance.min > edgeDistance.max");

		// Add first vertex at a random position within GraphProperties
		V currentVertex = graph.addVertex(random.getRandom(properties.getWidth().min, properties.getWidth().max),
				random.getRandom(properties.getHeight().min, properties.getHeight().max));
		vertexCount++;
		log.info(String.format("Added Vertex %d at %s", vertexCount, graph.getFirstVertex().getPosition().toString()));

		// As long as graph's size doesn't match GraphProperties requirements
		// (vertexCount),
		// process vertex generation process
		while (vertexCount < numberOfVertices && attemps < 100) {

			// Search a Position2D in GraphProperties which matches vertexDistance
			// requirements
			Position2D Position2D = getRandomPosition(currentVertex.getPosition(), properties.getVertexDistance());

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
					edgeCount = random.getRandom(properties.getEdgeCount().min, properties.getEdgeCount().max - 1);
				} else
					edgeCount = random.getRandom(properties.getEdgeCount().min, properties.getEdgeCount().max);

				// Update currentVertex with newVertex
				currentVertex = newVertex;

				// Generate edges
				generateEdges(newVertex, edgeCount, properties);

			} else {
				// Take a random vertex from graph
				currentVertex = graph.getVertex(random.getRandom(0, vertexCount));
			}

			attemps++;
		}

		return vertexCount;
	}

	private void generateEdges(V source, int edgeCount, GraphProperties properties) {

		// (1) Radius to gather vertices in environment, specified by a randomly chosen
		// number in interval [properties.edgeDistance.min, properties.edgeDistance.max]
		double edgeDistance = random.getRandom(properties.getEdgeDistance().min, properties.getEdgeDistance().max);

		// (2) Gather all vertices in radius, specified by a randomly chosen radius in
		// interval [properties.edgeDistance.min, properties.edgeDistance.max]
		List<V> verticesInRadius = graph.getVerticesInRadius(source, edgeDistance);

		if (verticesInRadius.size() > 0) {

			// (3) Select n vertices randomly from vertex environment
			List<V> randomVertices = random.selectNrandomOfM(verticesInRadius, edgeCount);

			// (4) Add edges
			for (V target : randomVertices)
				// Only add edge while target node's number of edges is below requirement given
				// by properties.edgeCount.max
				if (graph.getEdgesOf(target).size() < properties.getEdgeCount().max)
					graph.addEdge(source, target);
		}
	}

}
