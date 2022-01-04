package de.jgraphlib.examples.graphs;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.NetworkGraphProperties;
import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;

public class DirectedNetworkGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off

		RandomNumbers randomNumbers = new RandomNumbers();
		
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		NetworkGraphProperties properties = new NetworkGraphProperties(
				/* playground width */ 1025,
				/* playground height */ 512, 
				/* number of vertices */ new IntRange(100, 100),
				/* distance between vertices */ new DoubleRange(50d, 100d),
				/* edge distance */ new DoubleRange(100, 100));

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, 
						new EdgeDistanceSupplier(), 
						randomNumbers);
		
		generator.generate(properties);
		
		RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath = 
				new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph);
			
		RandomNumbers random = new RandomNumbers();
		
		for(int i=0; i<5; i++) 
			graph.addPath(randomPath.compute(graph.getVertex(random.getRandom(0, graph.getVertices().size()-1)), random.getRandom(0, 10)));
		
		SwingUtilities.invokeAndWait(
				new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, new WeightedEdgeIDPrinter<WeightedEdge<EdgeDistance>, EdgeDistance>()));
			
		// @formatter:on
	}
}