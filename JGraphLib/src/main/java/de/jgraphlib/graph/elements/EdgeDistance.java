package de.jgraphlib.graph.elements;

import java.text.DecimalFormat;

public class EdgeDistance extends EdgeWeight {

	private Double distance;

	public EdgeDistance() {}
	
	public EdgeDistance(double distance) {
		this.distance = distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getDistance() {
		return this.distance;
	}

	@Override
	public String toString() {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		return decimalFormat.format(distance).toString();
	}
}
