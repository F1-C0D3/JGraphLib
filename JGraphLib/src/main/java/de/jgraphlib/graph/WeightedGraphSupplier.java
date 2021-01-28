package de.jgraphlib.graph;

import java.util.function.Supplier;

public class WeightedGraphSupplier<P, W> {

	public VertexSupplier getVertexSupplier() {
		return new VertexSupplier();
	}

	public EdgeSupplier getEdgeSupplier() {
		return new EdgeSupplier();
	}

	private class VertexSupplier implements Supplier<Vertex<P>> {
		@Override
		public Vertex<P> get() {
			return new Vertex<P>();
		}
	}

	private class EdgeSupplier implements Supplier<WeightedEdge<W>> {
		@Override
		public WeightedEdge<W> get() {
			return new WeightedEdge<W>();
		}
	}
}
