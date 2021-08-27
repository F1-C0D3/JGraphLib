package de.jgraphlib.graph.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.util.RandomNumbers;

public class RandomClusterGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public RandomClusterGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, RandomNumbers random) {
		super(graph, random);

	}

	class Cluster extends ArrayList<V> {
	}

	public void generate(ClusterGraphProperties properties) {

		List<Cluster> clusters = new ArrayList<Cluster>();
		int clusterVertexQuantity = random.getRandom(properties.getVertexCount().min, properties.getVertexCount().max)
				/ properties.getClusterQuantity();

		for (int i = 0; i < properties.getClusterQuantity(); i++) {

			System.out.println(String.format("cluster %d", i));

			
			V currentVertex;
			int vertexCount = 0;
			int attempts = 0;
			clusters.add(new Cluster());

			if (i != 0) {
				// Generate a random position in clusterEdgeDistance for the first vertex of
				// cluster i (pointing from a random vertex of cluster i-1 in
				// clusterEdgeDistance)
				Position2D position = getRandomPosition(
						clusters.get(i - 1).get(random.getRandom(0, clusters.get(i - 1).size() - 1)).getPosition(),
						properties.getClusterEdgeDistance().max);

				while (!properties.isInside(position.x(), position.y())
						&& graph.vertexInRadius(position, properties.getClusterEdgeDistance().max)) {
					
					position = getRandomPosition(
							clusters.get(i - 1).get(random.getRandom(0, clusters.get(i - 1).size() - 1)).getPosition(),
							properties.getClusterEdgeDistance().max);
					attempts++;
				}

				if(attempts >= 100)
					return;
				else {
					currentVertex = graph.addVertex(position);
					clusters.get(i).add(currentVertex);
					System.out.println(currentVertex);
				}
				
			} else {
				// Generate a random position anywhere in the playground for the first vertex of
				// the first cluster
				currentVertex = graph.addVertex(random.getRandom(properties.getWidth().min, properties.getWidth().max),
						random.getRandom(properties.getHeight().min, properties.getHeight().max));
				clusters.get(i).add(currentVertex);
			}

			// Generate the cluster i
			while (vertexCount < clusterVertexQuantity && attempts < 1000) {

				Position2D position = getRandomPosition(currentVertex.getPosition(), properties.getVertexDistance());

				if (properties.isInside(position.x(), position.y())
						&& !graph.vertexInRadius(position, properties.getVertexDistance().max)
						&& !graph.vertexInRadius(position, properties.getClusterEdgeDistance().min, clusters.get(i))) {
					
					V newVertex = graph.addVertex(position.x(), position.y());
					clusters.get(i).add(newVertex);
					vertexCount++;
					connectVerticesInRadius(newVertex, properties.getEdgeDistance().max);
					// connectVerticesInRadius(newVertex, properties.getClusterEdgeDistance(),
					// clusters.get(i));
					currentVertex = newVertex;

				} else
					currentVertex = clusters.get(i).get(random.getRandom(0, clusters.get(i).size() - 1));

				attempts++;
			}
			
			if(attempts == 100)
				System.out.println(String.format("cluster %d failed", i));
		}
	}
	
	public boolean generateCluster(Position2D startPosition, ClusterGraphProperties properties){
		
		Cluster cluster = new Cluster();
		int attempts = 100;
		
		Position2D position = getRandomPosition(startPosition, properties.getVertexDistance());
		
		while(cluster.size() < properties. && attempts > 0) {
			
			// Generate a random position in radial distance to startPosition
			position = getRandomPosition(startPosition, properties.getVertexDistance());

			// Check if position lies inside playground
			boolean isInside = properties.isInside(position.x(), position.y());
			
			// Check if there is no other position in distance
			boolean vertexInRadius = graph.vertexInRadius(position, properties.getVertexDistance().min);
			
			if(isInside && !vertexInRadius) {
				positions.add(position);
			}
			else { 
				position = positions.get(random.getRandom(0, positions.size()-1));
				attempts--;		
			}
		}
		
		return positions;
	}
}
