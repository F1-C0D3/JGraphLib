package de.jgraphlib.graph;

import java.util.function.Supplier;

public abstract class EdgeWeightSupplier<W> implements Supplier<W> {

	@Override
	public W get() {
		return null;
	}
}
