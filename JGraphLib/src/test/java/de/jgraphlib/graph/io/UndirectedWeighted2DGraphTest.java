package de.jgraphlib.graph.io;

import java.io.IOException;

import org.junit.Test;

import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.NetworkGraphProperties;
import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;

public class UndirectedWeighted2DGraphTest {

	@Test
	public void exportGraph() {

		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier(), new RandomNumbers());

		NetworkGraphProperties properties = new NetworkGraphProperties(/* playground width */ 1024,
				/* playground height */ 768, /* number of vertices */ new IntRange(150, 150),
				/* distance between vertices */ new DoubleRange(50d, 100d),
				/* edge distance */ new DoubleRange(100d, 100d));

		generator.generate(properties);

		XMLExporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance> exporter = new XMLExporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new VertextPosition2DMapper());

		exporter.exportGraph(String.format("%s.xml", this.getClass().getName()));
	}

	@Test
	public void importGraph() throws IOException {

		// Empty graph
		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance> importer = new XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new VertextPosition2DMapper());

		importer.importGraph(String.format("%s.xml", this.getClass().getName()));

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> app = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph, null);	
		
		System.in.read();
	}
}
