package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.Position2D;

public class VisualVertex {

	private final Position2D position;
	private Color backgroundColor;
	private Color borderColor;
	private String text;
	private List<VisualPath> visualPaths;

	public VisualVertex(Position2D position, Color backgroundColor, Color borderColor, String text) {
		this.position = position;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.text = text;
		this.visualPaths = new ArrayList<VisualPath>();
	}

	public Position2D getPosition() {
		return this.position;
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public Color getBorderColor() {
		return this.borderColor;
	}

	public String getText() {
		return this.text;
	}

	public List<VisualPath> getVisualPaths() {
		return this.visualPaths;
	}

	public void addVisualPath(VisualPath visualPath) {
		this.visualPaths.add(visualPath);
	}
}
