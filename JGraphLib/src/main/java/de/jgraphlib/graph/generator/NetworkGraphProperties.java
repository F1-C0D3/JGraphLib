package de.jgraphlib.graph.generator;

public class NetworkGraphProperties extends GraphProperties {

    public NetworkGraphProperties(int width, int height, IntRange vertexCount, DoubleRange vertexDistance,
	    int edgeDistance) {
	super(width, height, vertexCount, vertexDistance, null, new DoubleRange(edgeDistance, edgeDistance));
    }
}
