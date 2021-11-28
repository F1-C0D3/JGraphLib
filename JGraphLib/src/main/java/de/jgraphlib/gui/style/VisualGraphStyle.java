package de.jgraphlib.gui.style;

import java.awt.Color;

public class VisualGraphStyle {

	/* Playground (background) mark up */
	private Color backgroundColor = Color.WHITE;

	/* VisualVertex mark up */
	private int vertexWidth = 20;
	private int vertexBorderWidth = 2;
	private Color vertexBackgroundColor = Color.LIGHT_GRAY;
	private Color vertexBorderColor = Color.BLACK;

	/* VisualEdge mark up */
	private int edgeLineWidth = 2;
	private Color edgeColor = Color.BLACK;

	private int visualEdgeTupleDistance = 2;
	
	/* VisualPath mark up */
	private int visualPathLineWidth = 2;
	private int visualPathLineDistance = 4;
	
	private boolean isDirected = false;

	public VisualGraphStyle() {
		this.isDirected = false;
	}
	
	public VisualGraphStyle(boolean isDirected) {
		this.isDirected = isDirected;
	}
	
	public boolean isDirected() {
		return this.isDirected;
	}
	
	public boolean isUndirected() {
		return !this.isDirected;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public void setVertexWidth(int vertexWidth) {
		this.vertexWidth = vertexWidth;
	}

	public Integer getVertexWidth() {
		return this.vertexWidth;
	}

	public void setVertexBorderWidth(int vertexBorderWidth) {
		this.vertexBorderWidth = vertexBorderWidth;
	}

	public Integer getVertexBorderWidth() {
		return this.vertexBorderWidth;
	}

	public void setVertexBackgroundColor(Color vertexBackgroundColor) {
		this.vertexBackgroundColor = vertexBackgroundColor;
	}

	public Color getVertexBackgroundColor() {
		return this.vertexBackgroundColor;
	}

	public void setVertexBorderColor(Color vertexBorderColor) {
		this.vertexBorderColor = vertexBorderColor;
	}

	public Color getVertexBorderColor() {
		return this.vertexBorderColor;
	}

	public void setEdgeLineWidth(int edgeLineWidth) {
		this.edgeLineWidth = edgeLineWidth;
	}

	public int getEdgeLineWidth() {
		return this.edgeLineWidth;
	}

	public void setEdgeColor(Color edgeColor) {
		this.edgeColor = edgeColor;
	}

	public Color getEdgeColor() {
		return this.edgeColor;
	}
	
	public int getVisualEdgeTupleDistance() {
		return visualEdgeTupleDistance;
	}
	
	public int getVisualPathLineDistance() {
		return this.visualPathLineDistance;
	}

	public void setVisualPathLineWidth(int visualPathLineWidth) {
		this.visualPathLineWidth = visualPathLineWidth;
	}

	public int getVisualPathLineWidth() {
		return this.visualPathLineWidth;
	}
}
