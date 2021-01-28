package de.jgraphlib.graph;

public class Position3D {

    private final double x;
    private final double y;
    private final double z;

    public Position3D(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public double x() {
	return this.x;
    }

    public double y() {
	return this.y;
    }

    public double z() {
	return this.z;
    }   
}
