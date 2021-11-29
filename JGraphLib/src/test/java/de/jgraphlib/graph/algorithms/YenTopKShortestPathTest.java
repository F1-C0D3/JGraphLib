package de.jgraphlib.graph.algorithms;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class YenTopKShortestPathTest {

	@Test
	public void dijkstraShortestPathTest() throws InvocationTargetException, InterruptedException {

		// @formatter:off

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
				new Weighted2DGraphSupplier().getVertexSupplier(), new Weighted2DGraphSupplier().getEdgeSupplier(),
				new Weighted2DGraphSupplier().getEdgeWeightSupplier(), new Weighted2DGraphSupplier().getPathSupplier());

		GridGraphProperties properties = new GridGraphProperties(/* playground width */ 200,
				/* playground height */ 200, /* distance between vertices */
				100, /* length of edges */
				100);

		GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());
		generator.generate(properties);

		SwingUtilities
				.invokeAndWait(new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, new WeightedEdgeIDPrinter<WeightedEdge<EdgeDistance>, EdgeDistance>()));

		Function<EdgeDistance, Double> edgeMetric = (EdgeDistance w) -> {
			return w.getDistance();
		};
		Function<Tuple<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>, Double> pathMetric = (
				Tuple<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> pathTuple) -> {
					
					Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> firstPath = pathTuple.getFirst();
					Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> secondPath = pathTuple.getSecond();
					int commonLinks=0;
					for (WeightedEdge<EdgeDistance> fEdge : firstPath.getEdges()) {
						for(WeightedEdge<EdgeDistance> sEdge: secondPath.getEdges()) {
							if (fEdge.getID()==sEdge.getID())
								commonLinks++;
						}
					}
					return (double)commonLinks;
					
					
		};

		YenTopKShortestPaths<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> topKShortestPath = new YenTopKShortestPaths<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph,200);

		List<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> ksp = topKShortestPath
				.compute(graph.getFirstVertex(), graph.getLastVertex(),  edgeMetric, pathMetric);

		Set<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> uniquePaths = new HashSet<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
				ksp);
		System.out.println(String.format("Duplicates: %d ", ksp.size() - uniquePaths.size()));

	}
}