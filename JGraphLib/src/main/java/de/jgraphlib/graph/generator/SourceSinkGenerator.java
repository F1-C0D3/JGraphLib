package de.jgraphlib.graph.generator;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class SourceSinkGenerator<V extends Vertex<?>, E extends WeightedEdge<?>> {

	
	public SourceSinkGenerator(WeightedGraph<V,?,E,?, ?> graph, int objectives) {
		
	}
	
	public List<Tuple<V,V>> generate() {
		
		List<Tuple<V,V>> objectives = new ArrayList<Tuple<V,V>>();
		
		
		return objectives;
	}
}
