package de.jgraphlib.examples.directed;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;

public class DirectedNetworkGraph {

	public static void main(String[] args) {

		// @formatter:off

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

		NetworkGraphProperties properties = new NetworkGraphProperties(
				/* playground width */ 1024,
				/* playground height */ 768, 
				/* number of vertices */ new IntRange(100, 200),
				/* distance between vertices */ new DoubleRange(50d, 100d),
				/* edge distance */ new DoubleRange(100, 100));

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());
		generator.generate(properties);
		
		graph.addEdge(graph.getVertex(1), graph.getVertex(0));
		graph.addEdge(graph.getVertex(0), graph.getVertex(1));
		
		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> visualGraphApp = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier());
				
		RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath = new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph);
			
		for (int i = 1; i <= 5; i++)
			visualGraphApp.getVisualGraphFrame().getVisualGraphPanel().getVisualGraph().addVisualPath(
					randomPath.compute(graph.getVertex(new RandomNumbers().getRandom(5, graph.getVertices().size())), 10));
				
		// @formatter:on
	}
}