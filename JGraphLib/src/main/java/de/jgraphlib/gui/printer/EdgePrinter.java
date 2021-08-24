package de.jgraphlib.gui.printer;

import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.WeightedEdge;

public abstract class EdgePrinter<E extends WeightedEdge<W>, W extends EdgeWeight> {

	public abstract String print(E edge); 	
}
