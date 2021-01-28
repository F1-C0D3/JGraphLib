package de.jgraphlib.graph;

import java.text.DecimalFormat;

public class EdgeDistance extends EdgeWeight {

    private double distance;
    
    public EdgeDistance() {}
    
    public EdgeDistance(double distance) {
	this.distance = distance;
    }
    
    public void setDistance(double distance) {
	this.distance = distance;
    }
    
    public double getDistance() {
	return this.distance;
    }
    
    @Override
    public String toString() {
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	return decimalFormat.format(distance).toString();
    }
}
