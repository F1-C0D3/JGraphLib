package de.jgraphlib.examples;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.gui.VisualGraphApp;

public class GridGraph {

	public static void main(String[] args) {

		// @formatter:off	
		
		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());
			
		GridGraphProperties properties = new GridGraphProperties(
				/*playground width*/ 			1024, 
				/*playground height*/			768, 
				/*distance between vertices*/	100,
				/*length of edges */			100);
		
		GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, new EdgeDistanceSupplier());
		generator.generate(properties);

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> visualGraphApp = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, new EdgeDistanceSupplier());
		// @formatter:on
	}
}
