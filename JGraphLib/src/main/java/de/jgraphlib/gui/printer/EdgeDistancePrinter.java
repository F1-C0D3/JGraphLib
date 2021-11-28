package de.jgraphlib.gui.printer;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.WeightedEdge;

public class EdgeDistancePrinter<E extends WeightedEdge<W>, W extends EdgeDistance> extends EdgePrinter<E,W>{

	@Override
	public String print(E edge) {
		return String.format("%.2f", edge.getWeight().getDistance());	
	}
}
