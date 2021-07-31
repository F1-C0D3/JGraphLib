package de.jgraphlib.graph.suppliers;

import de.jgraphlib.graph.elements.EdgeDistance;

public class EdgeDistanceSupplier extends EdgeWeightSupplier<EdgeDistance> {

	@Override
	public EdgeDistance get() {
		return new EdgeDistance();
	}
}
