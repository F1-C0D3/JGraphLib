package de.jgraphlib.graph.elements;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.Assert;
import org.junit.Test;

import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GridGraphGenerator;
import de.jgraphlib.generator.GridGraphProperties;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier.EdgeWeightSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.EdgeDistancePrinter;
import de.jgraphlib.util.RandomNumbers;

public class LinksCountTest {

	
	@Test
	public void directedLinksCountTest() throws InvocationTargetException, InterruptedException, IOException {
		

		EdgeWeightSupplier edgeWeightSupplier = new Weighted2DGraphSupplier().getEdgeWeightSupplier();
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(), 
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						edgeWeightSupplier, 
						new Weighted2DGraphSupplier().getPathSupplier());
		
		
		GridGraphProperties properties = new GridGraphProperties(/* playground width */ 200,
				/* playground height */ 200, /* distance between vertices */
				100, /* length of edges */
				100, EdgeStyle.BIDIRECTIONAL);

			GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
					graph, edgeWeightSupplier, new RandomNumbers(0));
			generator.generate(properties);
		
			Assert.assertEquals(24, graph.getEdges().size());
			
	}
	
	@Test
	public void undirectedLinksCountTest() throws InvocationTargetException, InterruptedException, IOException {
		

		EdgeWeightSupplier edgeWeightSupplier = new Weighted2DGraphSupplier().getEdgeWeightSupplier();
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(), 
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						edgeWeightSupplier, 
						new Weighted2DGraphSupplier().getPathSupplier());
		
		
		GridGraphProperties properties = new GridGraphProperties(/* playground width */ 200,
				/* playground height */ 200, /* distance between vertices */
				100, /* length of edges */
				100, EdgeStyle.UNIDIRECTIONAL);

			GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
					graph, edgeWeightSupplier, new RandomNumbers(0));
			generator.generate(properties);

			Assert.assertEquals(12, graph.getEdges().size());
			
	}
	
}
