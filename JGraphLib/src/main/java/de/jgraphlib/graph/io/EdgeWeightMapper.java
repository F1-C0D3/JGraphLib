package de.jgraphlib.graph.io;

import java.util.List;

import de.jgraphlib.util.Tuple;

public abstract class EdgeWeightMapper<W> {

	public EdgeWeightMapper() {}
	
	public abstract List<Tuple<String /*name*/, String /*value*/>> translate(W edgeWeight);	
	
	public abstract W translate(List<Tuple<String /*name*/, String /*value*/>> attributesValues);	
}
