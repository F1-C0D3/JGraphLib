package de.jgraphlib.examples.undirected;

import de.jgraphlib.graph.UndirectedWeighted2DGraph;
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
import de.jgraphlib.util.RandomNumbers;

public class UndirectedDensedGridGraph {

  public static void main(String[] args) {

    // @formatter:off

UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
		new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
				new Weighted2DGraphSupplier().getVertexSupplier(),
				new Weighted2DGraphSupplier().getEdgeSupplier(),
				new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
				new Weighted2DGraphSupplier().getPathSupplier());

    GridGraphProperties properties =
        new GridGraphProperties(
            /* playground width */ 1024,
            /* playground height */ 768, /* distance between vertices */
            100, /* length of edges */
            300);

    GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator =
        new GridGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
            graph, new EdgeDistanceSupplier(), new RandomNumbers());
    generator.generate(properties);

    VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> visualGraphApp =
        new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(graph);
    // @formatter:on
  }
}
