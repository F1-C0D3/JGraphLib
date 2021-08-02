package de.jgraphlib.gui;

import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.WeightedEdge;

public class EdgeIDPrinter<E extends WeightedEdge<W>, W extends EdgeWeight> extends EdgePrinter<E,W>{

	@Override
	public String print(E edge) {
		return Integer.toString(edge.getID());	
	}
}