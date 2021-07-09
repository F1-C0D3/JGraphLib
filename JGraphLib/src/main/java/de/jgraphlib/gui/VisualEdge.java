package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.Position2D;
import de.jgraphlib.maths.Point2D;

public class VisualEdge {

	private final Point2D startPosition;
	private final Point2D targetPosition;
	private Color color;
	private String text;
	private List<VisualPath> visualPaths;

	public VisualEdge(Point2D startPosition, Point2D targetPosition, Color color, String text) {
		this.startPosition = startPosition;
		this.targetPosition = targetPosition;
		this.color = color;
		this.text = text;
		this.visualPaths = new ArrayList<VisualPath>();
	}

	public Point2D getStartPosition() {
		return this.startPosition;
	}

	public Point2D getTargetPosition() {
		return this.targetPosition;
	}

	public Color getColor() {
		return this.color;
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
