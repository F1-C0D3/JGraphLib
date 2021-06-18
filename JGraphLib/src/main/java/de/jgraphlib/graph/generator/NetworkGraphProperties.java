package de.jgraphlib.graph.generator;

public class NetworkGraphProperties extends GraphProperties {

    public NetworkGraphProperties(int width, int height, IntRange vertexCount, DoubleRange vertexDistance,
	    DoubleRange edgeDistance) {
	super(width, height, vertexCount, vertexDistance, null, edgeDistance);
    }
}
