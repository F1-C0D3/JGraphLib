package de.jgraphlib.maths;

import java.text.DecimalFormat;

public class Point2D {

	private final double x;
	private final double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}
	
	public double getDistanceTo(Point2D p) {
		return Math.sqrt(Math.pow(this.x() - p.x(), 2) + Math.pow(this.y() - p.y(), 2));
	}

	@Override
	public String toString() {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(x: ").append(decimalFormat.format(x)).append(", y: ").append(decimalFormat.format(y))
				.append(")");
		return stringBuilder.toString();
	}
}
