package de.jgraphlib.graph.elements;

import org.junit.Test;

public class PathTest {

	
	@Test
	public void functionTest_cropUntil() {
		
		Vertex<Position2D> source = new Vertex<Position2D>(new Position2D(0,0));
		Vertex<Position2D> target = new Vertex<Position2D>(new Position2D(0,0));
		
		Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> path = new Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(source, target);
		
		
			
	}
	
}
