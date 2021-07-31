package de.jgraphlib.graph.io;

import java.util.List;

import de.jgraphlib.util.Tuple;

public abstract class VertexPositionMapper<P> {

	public VertexPositionMapper() {}

	public abstract List<Tuple<String /* name */, String /* value */>> translate(P position);

	public abstract P translate(List<Tuple<String /* name */, String /* value */>> attributesValues);

}
