package de.jgraphlib.gui.style;

import java.awt.Color;
import java.awt.Stroke;

public class ArrowStyle {

	private Color color;
	private Stroke stroke;
	private ArrowLegStyle legStyle;
	private int legLength;
	
	public ArrowStyle(Color color, Stroke stroke, ArrowLegStyle legStyle, int legLength) {
		this.color = color;
		this.stroke = stroke;
		this.legStyle = legStyle;
		this.legLength = legLength;
	}	
	
	public Color getColor() {
		return this.color;
	}
	
	public Stroke getStroke() {
		return this.stroke;
	}
	
	public ArrowLegStyle getLegStyle() {
		return this.legStyle;
	}
	
	public int getLegLength() {
		return this.legLength;
	}
}
