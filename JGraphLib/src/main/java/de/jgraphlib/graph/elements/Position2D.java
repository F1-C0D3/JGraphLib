package de.jgraphlib.graph.elements;

public class Position2D {

	private final double x;
	private final double y;

	public Position2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}

	@Override
	public String toString() {
		return String.format("x=%f,y=%f", x, y);
	}
}
