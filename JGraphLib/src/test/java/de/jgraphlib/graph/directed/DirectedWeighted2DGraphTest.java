package de.jgraphlib.graph.directed;

import org.junit.Test;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;

public class DirectedWeighted2DGraphTest {

	@Test
	public void TestFunction_getVerticesOf() {
		
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(), 
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());
		
		Vertex<Position2D> v1 = graph.addVertex(0d,0d);
		Vertex<Position2D> v2 = graph.addVertex(0d,1d);
		Vertex<Position2D> v3 = graph.addVertex(0d,2d);
		Vertex<Position2D> v4 = graph.addVertex(0d,3d);

		WeightedEdge<EdgeDistance> e1 = graph.addEdge(v1,v2);
		WeightedEdge<EdgeDistance> e2 = graph.addEdge(v2,v3);		
		WeightedEdge<EdgeDistance> e3 = graph.addEdge(v3,v4);
		
		System.out.println(graph.getVerticesOf(e2).getFirst().toString());
		System.out.println(graph.getVerticesOf(e2).getSecond().toString());
	}
	
	@Test
	public void TestFunction_getNextHopsOf() {
		
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(), 
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());
		
		Vertex<Position2D> v1 = graph.addVertex(0d,0d);
		Vertex<Position2D> v2 = graph.addVertex(0d,0d);
		Vertex<Position2D> v3 = graph.addVertex(0d,0d);
		Vertex<Position2D> v4 = graph.addVertex(0d,0d);

		WeightedEdge<EdgeDistance> v1_v2 = graph.addEdge(v1,v2);
		WeightedEdge<EdgeDistance> v2_v3 = graph.addEdge(v2,v3);		
		WeightedEdge<EdgeDistance> v3_v4 = graph.addEdge(v3,v4);
		
		System.out.println(graph.getNextHopsOf(v2));
		System.out.println(graph.getOutgoingEdgesOf(v2));
		System.out.println(graph.getIncomingEdgesOf(v2));
	}	
}
