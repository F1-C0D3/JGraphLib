package de.jgraphlib.examples;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;

public class SimpleGraph {

	public static void main(String[] args) {
		
		// @formatter:off

		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = 
				new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
						new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

		graph.addVertex(5d, 5d);
		graph.addVertex(5d, 6d);
		graph.addVertex(6d, 5d);
		graph.addVertex(6d, 6d);

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> visualGraphApp = 
				new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, 
						new EdgeDistanceSupplier());
		// @formatter:on
		
	}
	
}
