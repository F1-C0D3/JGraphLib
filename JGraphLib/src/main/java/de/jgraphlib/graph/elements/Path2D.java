package de.jgraphlib.graph.elements;

import de.jgraphlib.util.Tuple;

public class Path2D<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Path<V, E, W>
		implements Comparable<Path2D<V, E, W>> {

	private static final long serialVersionUID = 1L;

	public Path2D() {
		this.source = null;
		this.target = null;
	}

	public Path2D(V source, V target) {
		super.source = source;
		super.target = target;
		super.add(new Tuple<E, V>(null, source));
	}

	public double getDistance() {
		double distance = 0;
		for (Tuple<E, V> edgeVertexTuple : this)
			if (edgeVertexTuple.getFirst() != null)
				distance += edgeVertexTuple.getFirst().getWeight().getDistance();

		return distance;
	}

	@Override
	public int compareTo(Path2D<V, E, W> other) {
		return Double.compare(getDistance(), other.getDistance());
	}
}
