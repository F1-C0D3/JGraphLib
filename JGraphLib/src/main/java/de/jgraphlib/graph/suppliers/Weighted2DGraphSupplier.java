package de.jgraphlib.graph.suppliers;

import java.util.function.Supplier;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;

public class Weighted2DGraphSupplier {

	public VertexSupplier getVertexSupplier() {
		return new VertexSupplier();
	}

	public EdgeSupplier getEdgeSupplier() {
		return new EdgeSupplier();
	}
	
	public EdgeWeightSupplier getEdgeWeightSupplier() {
		return new EdgeWeightSupplier();
	}
	
	public PathSupplier getPathSupplier() {
		return new PathSupplier();
	}

	private class VertexSupplier implements Supplier<Vertex<Position2D>> {
		@Override
		public Vertex<Position2D> get() {
			return new Vertex<Position2D>();
		}
	}

	private class EdgeSupplier implements Supplier<WeightedEdge<EdgeDistance>> {
		@Override
		public WeightedEdge<EdgeDistance> get() {
			return new WeightedEdge<EdgeDistance>();
		}
	}
	
	private class EdgeWeightSupplier implements Supplier<EdgeDistance> {
		@Override
		public EdgeDistance get() {
			return new EdgeDistance();
		}
	}
	
	private class PathSupplier implements Supplier<Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> {
		@Override
		public Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> get() {
			return new Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>();
		}
	}	
}
