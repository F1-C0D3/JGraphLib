package de.jgraphlib.generator;

import java.text.DecimalFormat;

public class GraphProperties {
	
	private final IntRange width;
	private final IntRange height;
	private final IntRange vertexCount;
	private final DoubleRange vertexDistance;
	private final IntRange edgeCount;
	private final DoubleRange edgeDistance;
	private final EdgeStyle edgeStyle;
		
	public GraphProperties(int width, int height, IntRange vertexCount, DoubleRange vertexDistance, IntRange edgeCount, DoubleRange edgeDistance, EdgeStyle edgeStyle) {
		this.width = new IntRange(0, width);
		this.height = new IntRange(0, height);
		this.vertexCount = vertexCount;
		this.vertexDistance = vertexDistance;
		this.edgeCount = edgeCount;
		this.edgeDistance = edgeDistance;
		this.edgeStyle = edgeStyle;
	}
		
	public boolean isInside(double x, double y) {
		return (x >= width.min && x <= width.max) && (y >= height.min && y <= height.max);
	}
	
	public EdgeStyle getEdgeStyle() {
		return edgeStyle;
	}

	public IntRange getWidth() {
		return width;
	}

	public IntRange getHeight() {
		return height;
	}

	public IntRange getVertexCount() {
		return vertexCount;
	}

	public DoubleRange getVertexDistance() {
		return vertexDistance;
	}

	public IntRange getEdgeCount() {
		return edgeCount;
	}

	public DoubleRange getEdgeDistance() {
		return edgeDistance;
	}

	public static class IntRange {
		public int min;
		public int max;

		public IntRange(int min, int max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("(min: ").append(this.min);
			stringBuilder.append(", max: ").append(this.max).append(")");
			return stringBuilder.toString();
		}
	}

	public static class DoubleRange {
		public double min;
		public double max;

		public DoubleRange(double min, double max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public String toString() {
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("(min: ").append(decimalFormat.format(this.min));
			stringBuilder.append(", max: ").append(decimalFormat.format(this.max)).append(")");
			return stringBuilder.toString();
		}
	}
	
	public enum EdgeStyle {
		UNIDIRECTIONAL, BIDIRECTIONAL;
	}

	@Override
	public String toString() {
		/*
		 * StringBuilder stringBuilder = new StringBuilder();
		 * stringBuilder.append("width: ").append(this.width.toString());
		 * stringBuilder.append(", height: ").append(this.height.toString());
		 * stringBuilder.append(", vertexCount: ").append(this.vertexCount.toString());
		 * stringBuilder.append(", vertexDistance: ").append(this.vertexDistance.
		 * toString()); stringBuilder.append(", edgeCount: ").append(this.edgeCount);
		 * stringBuilder.append(", edgeDistance: ").append(this.edgeDistance.toString())
		 * ; return stringBuilder.toString();
		 */
		return "";
	}
}