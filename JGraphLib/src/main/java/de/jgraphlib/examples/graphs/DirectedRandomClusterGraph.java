package de.jgraphlib.examples.graphs;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.jgraphlib.generator.ClusterGraphProperties;
import de.jgraphlib.generator.RandomClusterGraphGenerator;
import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier.EdgeWeightSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.EdgeDistancePrinter;
import de.jgraphlib.util.RandomNumbers;

public class DirectedRandomClusterGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off
		
		RandomNumbers randomNumbers = new RandomNumbers(7277246775525279348L);
		//RandomNumbers randomNumbers = new RandomNumbers();

		EdgeWeightSupplier edgeWeightSupplier = new Weighted2DGraphSupplier().getEdgeWeightSupplier();
		
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		ClusterGraphProperties properties = new ClusterGraphProperties(
				/* playground width */ 		2048,
				/* playground height */ 	1024, 
				/* vertexCount */ 			new IntRange(150, 150),
				/* vertexDistance */ 		new DoubleRange(25d, 25d),
				/* edgeCount*/ 				new IntRange(1, 1), 
				/* edgeDistance*/ 			new DoubleRange(50d, 50d),
				/* edgeStyle */ 			EdgeStyle.UNIDIRECTIONAL,				
				/* clusterQuantity*/ 		5,
				/* clusterEdgeDistance*/ 	new DoubleRange(250d, 300d));

		RandomClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new RandomClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, edgeWeightSupplier, randomNumbers);
		
		generator.generate(properties);
				
		SwingUtilities.invokeAndWait(new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistancePrinter<WeightedEdge<EdgeDistance>,EdgeDistance>()));

		// @formatter:on
	}	
}
