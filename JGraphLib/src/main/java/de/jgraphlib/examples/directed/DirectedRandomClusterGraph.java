package de.jgraphlib.examples.directed;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.ClusterGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.generator.RandomClusterGraphGenerator;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.EdgeDistancePrinter;
import de.jgraphlib.util.RandomNumbers;

public class DirectedRandomClusterGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off
		RandomNumbers randomNumbers = new RandomNumbers(7277246775525279348L);
		
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		ClusterGraphProperties properties = new ClusterGraphProperties(
				/* playground width */ 			2048,
				/* playground height */ 		1024, 
				/* number of vertices */ 		new IntRange(100, 100),
				/* distance between vertices */ new DoubleRange(50d, 50d),
				null, /* edge distance */ 			new DoubleRange(50d, 75d),
				/* corridorQuantity*/ 			5,
				/* corridorEdgeDistance*/ 		new DoubleRange(250d, 300d));

		RandomClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new RandomClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, randomNumbers);
		
		generator.generate(properties);
				
		SwingUtilities.invokeAndWait(new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistancePrinter<WeightedEdge<EdgeDistance>,EdgeDistance>()));

		System.out.println(randomNumbers.getSeed());
		System.out.println(graph.toString());
	
		// @formatter:on
	}	
}
