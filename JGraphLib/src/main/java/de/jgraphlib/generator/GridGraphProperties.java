package de.jgraphlib.generator;

public class GridGraphProperties extends GraphProperties {

	public GridGraphProperties(int width, int height, int vertexDistance, int edgeDistance, EdgeStyle edgeStyle) {
		super(width, height, null, new DoubleRange(vertexDistance, vertexDistance), null, new DoubleRange(edgeDistance, edgeDistance), edgeStyle);
	}
}
