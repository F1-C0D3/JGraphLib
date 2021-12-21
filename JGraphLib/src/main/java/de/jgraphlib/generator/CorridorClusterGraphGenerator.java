package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class CorridorClusterGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
extends Weighted2DGraphGenerator<V, E, W> {

	public CorridorClusterGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, RandomNumbers random) {
		super(graph, random);
	}

	static class Corridor<V> {		
		public double x;
		public double y;
		public double width;
		public double height;		
		public List<V> vertices;
		
		public Corridor(int x, int y, int width, int height) {
			this.x = x; // x of top-left point
			this.y = y; // y of top-left point
			this.width = width;
			this.height = height;
			this.vertices = new ArrayList<V>();
		}
		
		public boolean isInside(double x, double y) {
			return (x >= this.x) && (x <= (this.x + this.width)) && (y <= this.y) && (y >= (this.y - this.height));
		}
		
		public String toString() {
			return String.format("x: %.2f, y: %.2f, width: %.2f, height: %.2f", x, y, width, height);
		}
	}
	
	public void generate(CorridorClusterGraphProperties properties) {
		
		// Generate corridors	
		List<Corridor<V>> corridors = new ArrayList<Corridor<V>>();
				
		switch(properties.getCorridorDirection()) {
		case HORIZONTAL:	
			
			int totalCorridorDistance = (properties.getCorridorQuantity()-1) * properties.getCorridorDistance();
			int corridorsHeight = (properties.getHeight().max - totalCorridorDistance) / properties.getCorridorQuantity();	
			
			for(int i=0; i < properties.getCorridorQuantity(); i++) {
				if(i != 0)
					corridors.add(
							new Corridor<V>(
									0, 
									corridorsHeight + i*(corridorsHeight + properties.getCorridorDistance()), 
									properties.getWidth().max, 
									corridorsHeight));
				else
					corridors.add(
							new Corridor<V>(
									0, 
									corridorsHeight, 
									properties.getWidth().max, 
									corridorsHeight));
			}
			break;
		case VERTICAL:
			break;
		default:
			break;
		}	
		
		int corridorVertexQuantity = random.getRandom(properties.getVertexCount().min, properties.getVertexCount().max) / properties.getCorridorQuantity();
			
		// Generate nodes in each corridor
		for(Corridor<V> corridor : corridors) {

			//System.out.println(String.format("corridor %s", corridor.toString()));
			
			int vertexCount = 0; int attempts = 0;
			V currentVertex = graph.addVertex(
					random.getRandom(corridor.x, corridor.x + corridor.width),
					random.getRandom(corridor.y - corridor.height, corridor.y));
			corridor.vertices.add(currentVertex);

			while(vertexCount < corridorVertexQuantity && attempts < 100) {
				
				Position2D Position2D = getRandomPosition(currentVertex.getPosition(), properties.getVertexDistance());
				
				if(corridor.isInside(Position2D.x(), Position2D.y()) && !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {		
					V newVertex = graph.addVertex(Position2D.x(), Position2D.y());
					corridor.vertices.add(newVertex);							
					vertexCount++;	
					
					// Connect new vertex with all vertices that are in edgeDistance of the same corridor
					connectVerticesInRadius(newVertex, properties.getEdgeDistance().max);	
					
					// Connect newVertex with all vertices that are in edgeCorridorDistance of foreign corridors
					connectVerticesInRadius(newVertex, properties.getCorridorEdgeDistance(), corridor.vertices);
					
					currentVertex = newVertex;
					
				} else
					currentVertex = corridor.vertices.get(random.getRandom(0, corridor.vertices.size()-1));
				
				attempts++;
			}	
			
			if(attempts >= 100)
				System.out.println("There just isnt enough space to place a new vertex! Increase the size of the playground or decrease number of vertices");
		}
	}
		
}
