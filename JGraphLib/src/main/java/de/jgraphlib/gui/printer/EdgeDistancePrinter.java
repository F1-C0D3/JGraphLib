package de.jgraphlib.gui.printer;

import de.jgraphlib.graph.elements.EdgeDistance;

public class EdgeDistancePrinter<W extends EdgeDistance> extends EdgeWeightPrinter<W>{
	public String print(W edgeDistance) {
		return String.format("%.2f", edgeDistance.getDistance());	
	}
}
