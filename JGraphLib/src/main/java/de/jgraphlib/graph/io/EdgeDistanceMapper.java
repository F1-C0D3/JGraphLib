package de.jgraphlib.graph.io;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.util.Tuple;

public class EdgeDistanceMapper extends EdgeWeightMapper<EdgeDistance> {

	@Override
	public List<Tuple<String, String>> translate(EdgeDistance edgeWeight) {
		List<Tuple<String, String>> attributesValues = new ArrayList<Tuple<String, String>>();
		attributesValues.add(new Tuple<String, String>("distance", Double.toString(edgeWeight.getDistance())));
		return attributesValues;
	}

	@Override
	public EdgeDistance translate(List<Tuple<String, String>> attributesValues) {
		return new EdgeDistance(Double.parseDouble(attributesValues.get(0).getSecond()));
	}

}
