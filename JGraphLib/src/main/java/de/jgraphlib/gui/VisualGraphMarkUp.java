package de.jgraphlib.gui;

import java.awt.Color;

public class VisualGraphMarkUp {

    /* Playground (background) mark up */
    private Color backgroundColor = Color.WHITE;

    /* VisualVertex mark up */
    private int vertexWidth = 20;
    private int vertexBorderWidth = 2;
    private Color vertexBackgroundColor = Color.LIGHT_GRAY;
    private Color vertexBorderColor = Color.BLACK;

    /* VisualEdge mark up */
    private int edgeWidth = 2;
    private Color edgeColor = Color.BLACK;

    /* VisualPath mark up */
    private int pathWidth = 4;

    /*private VisualEdgeTextBuilder<E,W> edgeTextBuilder;
    
    public VisualGraphMarkUp(VisualEdgeTextBuilder<E,W> edgeTextBuilder) {
	this.edgeTextBuilder = edgeTextBuilder;
    }*/

    public void setBackgroundColor(Color backgroundColor) {
	this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
	return this.backgroundColor;
    }

    /* Vertex mark up */

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

    /* Edge mark up */
    
    /*public VisualEdgeTextBuilder<E,W> getEdgeTextBuilder(){
	return this.edgeTextBuilder; 
    }
    
    public void setEdgeTextBuilder(VisualEdgeTextBuilder<E,W> edgeText) {
	this.edgeTextBuilder = edgeText;
    }*/

    public void setEdgeWidth(Integer edgeWidth) {
	this.edgeWidth = edgeWidth;
    }

    public Integer getEdgeWidth() {
	return this.edgeWidth;
    }

    public void setEdgeColor(Color edgeColor) {
	this.edgeColor = edgeColor;
    }

    public Color getEdgeColor() {
	return this.edgeColor;
    }

    /* VisualPath mark up */

    public void setPathWidth(Integer pathWidth) {
	this.pathWidth = pathWidth;
    }

    public Integer getPathWidth() {
	return this.pathWidth;
    }

}
