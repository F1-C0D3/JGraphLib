package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.util.Tuple;

public class VisualEdge {

	private final Point2D startPosition;
	private final Point2D targetPosition;
	private Color color;
	private String text;
	private final Point2D textPosition;
	private List<VisualPath> visualPaths;

	public VisualEdge(Point2D startPosition, Point2D targetPosition, Color color, Point2D textPosition, String text) {
		this.startPosition = startPosition;
		this.targetPosition = targetPosition;
		this.color = color;
		this.text = text;
		this.textPosition = textPosition;
		this.visualPaths = new ArrayList<VisualPath>();
	}
	
	public VisualEdge(Tuple<Point2D, Point2D> position, Color color, Point2D textPosition, String text) {
		this.startPosition = position.getFirst();
		this.targetPosition = position.getSecond();
		this.color = color;
		this.text = text;
		this.visualPaths = new ArrayList<VisualPath>();
		this.textPosition = textPosition;
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
	
	public Point2D getTextPosition() {
		return this.textPosition;
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
