package de.jgraphlib.examples.graphs;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.jgraphlib.generator.CorridorClusterGraphGenerator;
import de.jgraphlib.generator.CorridorClusterGraphProperties;
import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier.EdgeWeightSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;

public class DirectedCorridorClusterGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off


		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
				new Weighted2DGraphSupplier().getVertexSupplier(), new Weighted2DGraphSupplier().getEdgeSupplier(),
				new Weighted2DGraphSupplier().getEdgeWeightSupplier(), new Weighted2DGraphSupplier().getPathSupplier());

		CorridorClusterGraphProperties properties = new CorridorClusterGraphProperties(/* playground width */ 250,
				/* playground height */ 500, /* number of vertices */ new IntRange(250, 250),
				/* distance between vertices */ new DoubleRange(50d, 50d),
				/* edge distance */ new DoubleRange(50d, 100d), EdgeStyle.BIDIRECTIONAL, /* corridorQuantity */ 3,
				/* corridorDistance */ 75, /* corridorEdgeDistance */ 150,
				/* corridorAlingment */ CorridorClusterGraphProperties.CorridorDirection.HORIZONTAL);

		CorridorClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new CorridorClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new Weighted2DGraphSupplier().getEdgeWeightSupplier(), new RandomNumbers());

		generator.generate(properties);

		SwingUtilities.invokeAndWait(new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new WeightedEdgeIDPrinter<WeightedEdge<EdgeDistance>, EdgeDistance>()));

		// @formatter:on
	}
}
