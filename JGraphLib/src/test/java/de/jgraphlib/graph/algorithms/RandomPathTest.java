package de.jgraphlib.graph.algorithms;

import org.junit.Test;

import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.NetworkGraphProperties;
import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class RandomPathTest {

	@Test
	public void randomPathTest() {

		// @formatter:off

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
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

		RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath = new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph);

		Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath5Steps = randomPath
				.compute(graph.getFirstVertex(), 5);
		System.out.println(randomPath5Steps.toString());

		Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPathSourceTarget = randomPath
				.compute(graph.getFirstVertex(), graph.getLastVertex());
		System.out.println(randomPathSourceTarget.toString());
	}
}
