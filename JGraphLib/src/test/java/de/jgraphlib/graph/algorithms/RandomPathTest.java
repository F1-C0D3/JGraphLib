package de.jgraphlib.graph.algorithms;

import org.junit.Test;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Path;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;

public class RandomPathTest {
	
	@Test
	public void randomPathTest() {
		
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

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, new EdgeDistanceSupplier());
		
		generator.generate(properties);
				
		RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath = 
				new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph);

		Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath5Steps = randomPath.compute(graph.getFirstVertex(), 5);		
		System.out.println(randomPath5Steps.toString());
		
		Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPathSourceTarget = randomPath.compute(graph.getFirstVertex(), graph.getLastVertex());		
		System.out.println(randomPathSourceTarget.toString());
	}
}
