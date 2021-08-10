package de.jgraphlib.graph.algorithms;

import java.util.function.Function;

import org.junit.Test;

import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.RandomNumbers;

public class DijkstraShortestPathTest {

	@Test
	public void dijkstraShortestPathTest() {

		// @formatter:off

		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		NetworkGraphProperties properties = new NetworkGraphProperties(/* playground width */ 1024,
				/* playground height */ 768, /* number of vertices */ new IntRange(100, 200),
				/* distance between vertices */ new DoubleRange(50d, 100d), /* edge distance */ new DoubleRange(100,100));

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());

		generator.generate(properties);

		Function<EdgeDistance, Double> metric = (EdgeDistance w) -> {
			return w.getDistance();
		};

		DijkstraShortestPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> dijkstraShortestPath = 
				new DijkstraShortestPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph);

		dijkstraShortestPath.compute(graph.getFirstVertex(), graph.getLastVertex(), metric);

		System.out.println(dijkstraShortestPath.toString());

	}
}