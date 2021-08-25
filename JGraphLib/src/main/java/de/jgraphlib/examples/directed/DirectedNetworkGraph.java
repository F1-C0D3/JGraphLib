package de.jgraphlib.examples.directed;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;

public class DirectedNetworkGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		NetworkGraphProperties properties = new NetworkGraphProperties(
				/* playground width */ 1024,
				/* playground height */ 768, 
				/* number of vertices */ new IntRange(100, 200),
				/* distance between vertices */ new DoubleRange(50d, 100d),
				/* edge distance */ new DoubleRange(100, 100));

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, 
						new EdgeDistanceSupplier(), 
						new RandomNumbers());
		
		generator.generate(properties);
		
		RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath = 
				new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph);
		
		List<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> paths = new ArrayList<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>();
	
		RandomNumbers random = new RandomNumbers();
		
		for(int i=0; i<5; i++) {
			paths.add(randomPath.compute(graph.getVertex(random.getRandom(0, graph.getVertices().size()-1)), random.getRandom(0, 10)));
			System.out.println(paths.get(paths.size()-1));
		}
		
		SwingUtilities.invokeAndWait(new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new WeightedEdgeIDPrinter<WeightedEdge<EdgeDistance>, EdgeDistance>()));

		// @formatter:on
	}
}