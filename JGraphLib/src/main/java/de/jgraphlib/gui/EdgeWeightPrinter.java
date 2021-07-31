package de.jgraphlib.gui;

import de.jgraphlib.graph.elements.EdgeWeight;

public abstract class EdgeWeightPrinter<W extends EdgeWeight> {

	public abstract String print(W edgeWeight); 
			
}
