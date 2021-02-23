package de.jgraphlib.graph;

public class EdgeDistanceSupplier extends EdgeWeightSupplier<EdgeDistance> {

	@Override
	public EdgeDistance get() {
		return new EdgeDistance();
	}
}
