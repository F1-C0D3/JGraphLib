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
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;

public class DirectedRandomClusterGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		ClusterGraphProperties properties = new ClusterGraphProperties(
				/* playground width */ 			3000,
				/* playground height */ 		3000, 
				/* number of vertices */ 		new IntRange(50, 50),
				/* distance between vertices */ new DoubleRange(50d, 50d),
				null, /* edge distance */ 			new DoubleRange(50d, 100d),
				/* corridorQuantity*/ 			5,
				/* corridorEdgeDistance*/ 		new DoubleRange(150d, 200d));

		RandomClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new RandomClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, new RandomNumbers());
		
		generator.generate(properties);
				
		SwingUtilities.invokeAndWait(new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistancePrinter<EdgeDistance>()));

		// @formatter:on
	}
	
}
