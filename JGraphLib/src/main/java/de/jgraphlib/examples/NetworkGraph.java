package de.jgraphlib.examples;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.gui.VisualGraph;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.VisualGraphMarkUp;

public class NetworkGraph {

	public static void main(String[] args) {

		// @formatter:off	
		
		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());
	
		NetworkGraphProperties properties = new NetworkGraphProperties(
				/*playground width*/ 			1024, 
				/*playground height*/			768, 
				/*number of vertices*/			new IntRange(100, 200),
				/*distance between vertices */	new DoubleRange(50d, 100d), 
				/*edge distance*/				100);
		
		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, new EdgeDistanceSupplier());
		generator.generate(properties);

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> visualGraphApp = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, new EdgeDistanceSupplier());
		// @formatter:on
	}
}
