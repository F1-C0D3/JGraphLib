package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

// @formatter:off

public class RandomClusterGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	List<Cluster> clusters;
	
	public RandomClusterGraphGenerator(Weighted2DGraph<V, E, W, ?> graph, RandomNumbers random) {
		super(graph, random);
	}

	public void generate(ClusterGraphProperties properties) {

		clusters = new ArrayList<Cluster>();
		
		int clusterTargetSize = 
				random.getRandom(properties.getVertexCount().min, properties.getVertexCount().max)/properties.getClusterQuantity();
			
		for(int i=0; i < properties.getClusterQuantity(); i++) clusters.add(new Cluster(i, clusterTargetSize));
		
		clusters.get(0).setSource(graph.addVertex(
				random.getRandom(properties.getWidth().min, properties.getWidth().max),
				random.getRandom(properties.getHeight().min, properties.getHeight().max)));
		
		generateCluster(clusters.get(0), properties, 100);
		
		for (Cluster cluster : skipFirst(clusters)) {
	
			if(!generateFirstVertex(cluster, properties, 1000))
				return;
							
			if(!generateCluster(cluster, properties, 1000))
				return;
		}
	}
	
	public boolean generateFirstVertex(Cluster cluster, ClusterGraphProperties properties, int attempts) {
				
		Position2D position = getRandomPosition(
				clusters.get(cluster.ID - 1).get(random.getRandom(0, clusters.get(cluster.ID - 1).size() - 1)).getPosition(),
				properties.getClusterEdgeDistance().max);
		
		boolean requirement = 
				properties.isInside(position.x(), position.y()) &&
				!graph.vertexInRadius(position, properties.getClusterEdgeDistance().min);
			
		if(!requirement && attempts > 0)
			return generateFirstVertex(cluster, properties, attempts--);
			
		if(requirement) {
			cluster.setSource(graph.addVertex(position));
			return true;
		}
				
		return false;	
	}
	
	public boolean generateCluster(Cluster cluster, ClusterGraphProperties properties, int attempts){
		
		log.info(String.format("Generate cluster %d", cluster.ID+1));
		
		Position2D position = getRandomPosition(cluster.source.getPosition(), properties.getVertexDistance());
				
		while(cluster.size() < cluster.targetSize) {
					
			position = getRandomPosition(position, properties.getVertexDistance());	
								
			boolean clusterPositionRequirement = 
					properties.isInside(position.x(), position.y()) &&
					!graph.vertexInRadius(position, properties.getVertexDistance().min) &&
					!graph.vertexInRadius(position, properties.getClusterEdgeDistance().min, cluster);
							
			if(clusterPositionRequirement) {
				
				V newVertex = graph.addVertex(position);		
				cluster.add(newVertex);		
			
				connectVerticesInRadius(newVertex, properties.getEdgeDistance().max);	
				
				connectVerticesInRadius(newVertex, properties.getClusterEdgeDistance().max, cluster);
			}
			else { 
				position = cluster.get(random.getRandom(0, cluster.size()-1)).getPosition();		
				attempts--;	
			}
		}
		
		if(attempts <= 0) {
			graph.removeVertices(cluster);
			log.info(String.format("cluster %d failed", cluster.ID+1));
			return false;
		}
			
		return true;
	}
	
	class Cluster extends ArrayList<V> {
		
		public int ID;
		public V source;
		public int targetSize;
		
		public Cluster(int ID, int targetSize) {
			this.ID = ID;
			this.targetSize = targetSize;
		}
	
		public void setSource(V source) {
			this.source = source;
			this.add(source);
		}	
	}
	
	public static <T> List<T> initialize(int size, T val) {			
        return Stream.generate(String::new)
                    .limit(size)
                    .map(s -> val)
                    .collect(Collectors.toList());
    }
	
	public static <T> Iterable<T> skipFirst(final Iterable<T> c) {
	    return new Iterable<T>() {
	        @Override public Iterator<T> iterator() {
	            Iterator<T> i = c.iterator();
	            i.next();
	            return i;
	        }
	    };
	}
}
