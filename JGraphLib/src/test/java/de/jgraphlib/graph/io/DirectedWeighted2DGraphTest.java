package de.jgraphlib.graph.io;

import java.io.IOException;

import org.junit.Test;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;

public class DirectedWeighted2DGraphTest {

	@Test
	public void exportGraph() {

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());

		NetworkGraphProperties properties = new NetworkGraphProperties(/* playground width */ 1024,
				/* playground height */ 768, /* number of vertices */ new IntRange(2, 2),
				/* distance between vertices */ new DoubleRange(50d, 100d),
				/* edge distance */ new DoubleRange(100d, 100d));

		generator.generate(properties);

		XMLExporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance> exporter = new XMLExporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new VertextPosition2DMapper());
	
		exporter.exportGraph(String.format("%s.xml", this.getClass().getName()));
		
		System.out.println(graph.toString());
	}

	@Test
	public void importGraph() throws IOException {

		// Empty graph
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance> importer = new XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new VertextPosition2DMapper());

		importer.importGraph(String.format("%s.xml", this.getClass().getName()));

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> app = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier());
		
		System.out.println(graph.toString());
		
		System.in.read();
	}
}
