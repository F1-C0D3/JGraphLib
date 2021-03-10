package de.jgraphlib.examples;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.generator.GraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.generator.RandomGraphGenerator;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;

public class LimitedEdgedRandomGraph {

	public static void main(String[] args) {

		// @formatter:off

		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

		GraphProperties properties = new GraphProperties(/* playground width */ 1024, /* playground height */ 768,
				/* number of vertices */ new IntRange(100, 200),
				/* distance between vertices */ new DoubleRange(50d, 100d), /* connectivity */ new IntRange(1, 2),
				/* edge distance */ new DoubleRange(50d, 100d));

		RandomGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new RandomGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());
		generator.generate(properties);

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> visualGraphApp = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier());
		// @formatter:on
	}
}
