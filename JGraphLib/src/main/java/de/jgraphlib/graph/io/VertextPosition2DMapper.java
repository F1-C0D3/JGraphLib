package de.jgraphlib.graph.io;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.Tuple;

public class VertextPosition2DMapper extends VertexPositionMapper<Position2D> {

	@Override
	public List<Tuple<String, String>> translate(Position2D position) {
		List<Tuple<String, String>> attributesValues = new ArrayList<Tuple<String, String>>();
		attributesValues.add(new Tuple<String, String>("x", Double.toString(position.x())));
		attributesValues.add(new Tuple<String, String>("y", Double.toString(position.y())));
		return attributesValues;
	}

	@Override
	public Position2D translate(List<Tuple<String, String>> attributesValues) {
		return new Position2D(Double.parseDouble(attributesValues.get(0).getSecond()),
				Double.parseDouble(attributesValues.get(1).getSecond()));
	}
	
}
