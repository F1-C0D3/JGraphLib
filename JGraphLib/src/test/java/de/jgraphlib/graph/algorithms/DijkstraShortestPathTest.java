package de.jgraphlib.graph.algorithms;

import java.util.function.Function;

import org.junit.Test;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Path;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.RandomNumbers;

public class DijkstraShortestPathTest {

	@Test
	public void dijkstraShortestPathTest() {

		// @formatter:off

		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

		NetworkGraphProperties properties = new NetworkGraphProperties(/* playground width */ 1024,
				/* playground height */ 768, /* number of vertices */ new IntRange(100, 200),
				/* distance between vertices */ new DoubleRange(50d, 100d), /* edge distance */ 100);

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());

		generator.generate(properties);

		Function<EdgeDistance, Double> metric = (EdgeDistance w) -> {
			return w.getDistance();
		};

		DijkstraShortestPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> dijkstraShortestPath = new DijkstraShortestPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph);

		Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> shortestPath = dijkstraShortestPath
				.compute(graph.getFirstVertex(), graph.getLastVertex(), metric);

		System.out.println(shortestPath.getCost(metric));
	}
}