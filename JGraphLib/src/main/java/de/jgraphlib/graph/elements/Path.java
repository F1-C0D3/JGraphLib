package de.jgraphlib.graph.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import de.jgraphlib.util.Tuple;

public class Path<V extends Vertex<?>, E extends WeightedEdge<W>, W> extends LinkedList<Tuple<E, V>> {

	protected V source;
	protected V target;

	public Path(V source, V target) {
		this.source = source;
		this.target = target;
		super.add(new Tuple<E, V>(null, source));
	}

	public Path() {
	}

	public void set(V source, V target) {
		this.source = source;
		this.target = target;
		this.add(new Tuple<E, V>(null, source));
	}

	public void update(Path<V, E, W> path) {
		this.clear();
		super.addAll(path.subList(1, path.size()));
	}

	public List<Tuple<E, V>> cropUntil(V vertex) {
		List<Tuple<E, V>> removedItems = new ArrayList<Tuple<E, V>>();
		while (size() > 0) {
			if (getLast().getSecond().equals(vertex))
				return removedItems;
			else
				removedItems.add(removeLast());
		}
		return removedItems;
	}

	public boolean add(E edge, V vertex) {
		return super.add(new Tuple<E, V>(edge, vertex));
	}

	public boolean add(Tuple<E, V> tuple) {
		return super.add(tuple);
	}

	public void clear() {
		super.clear();
		super.add(new Tuple<E, V>(null, source));
	}

	public void setSource(V source) {
		this.source = source;
	}

	public V getSource() {
		return this.source;
	}

	public void setTarget(V target) {
		this.target = target;
	}

	public V getTarget() {
		return this.target;
	}

	public List<V> getVertices() {
		List<V> vertices = new ArrayList<V>();
		for (Tuple<E, V> tuple : this)
			vertices.add(tuple.getSecond());
		return vertices;
	}

	public List<E> getEdges() {
		List<E> edges = new ArrayList<E>();
		for (int i = 1; i < this.size(); i++)
			edges.add(get(i).getFirst());
		return edges;
	}

	public E getIncomingEdge(V vertex) {
		for (int i = 0; i < size(); i++)
			if (get(i).getSecond().equals(vertex))
				return get(i).getFirst();
		return null;
	}

	public E getOutgoingEdgeOf(V vertex) {
		for (int i = 0; i < size() - 1; i++)
			if (get(i).getSecond().equals(vertex))
				return get(i + 1).getFirst();
		return null;
	}

	public Tuple<E, V> getFirst() {
		if (this.get(0) != null)
			return this.get(0);
		return null;
	}

	public Tuple<E, V> getLast() {
		if (this.size() > 0)
			return this.get(this.size() - 1);
		return null;
	}

	public V getLastVertex() {
		if (this.size() > 0)
			return this.get(this.size() - 1).getSecond();
		return null;
	}

	public E getLastEdge() {
		if (this.size() > 0)
			return this.get(this.size() - 1).getFirst();
		return null;
	}

	public boolean contains(V vertex) {
		if (vertex != null && this.size() > 0)
			for (Tuple<E, V> tuple : this)
				if (vertex.equals(tuple.getSecond()))
					return true;
		return false;
	}

	public boolean contains(E edge) {
		if (edge != null && this.size() > 0)
			for (E e : this.getEdges())
				if (edge.equals(e))
					return true;
		return false;
	}

	public Boolean isComplete() {
		if (this.getLast() != null)
			return this.getLast().getSecond().equals(target);
		return false;
	}

	public List<V> getUnvisitedVerticesOf(List<V> vertices) {
		List<V> unvisitedVertices = new ArrayList<V>();
		for (V vertex : vertices)
			if (!this.contains(vertex))
				unvisitedVertices.add(vertex);
		return unvisitedVertices;
	}

	public boolean equals(Path<V, E, W> path) {
		for (int i = 0; i < this.size(); i++) {
			if (path.get(i) != null)
				if (!path.get(i).getSecond().equals(this.get(i).getSecond()))
					return false;
				else
					return false;
		}
		return true;
	}

	public Double getCost(Function<E, Double> metric) {

		for (Tuple<E, V> tuple : this) {
			double cost = 0;
			if (tuple.getFirst() != null) {
				cost += metric.apply(tuple.getFirst());
			}
			return cost;
		}

		return Double.POSITIVE_INFINITY;
	}

	/*
	 * @Override public String toString() { StringBuilder stringBuilder = new
	 * StringBuilder(); Iterator<Tuple<E, V>> iterator = this.iterator(); while
	 * (iterator.hasNext()) { Tuple<E, V> next = iterator.next(); if
	 * (iterator.hasNext())
	 * stringBuilder.append(next.getSecond().getID()).append(", "); else
	 * stringBuilder.append(next.getSecond().getID()); } return
	 * stringBuilder.toString(); }
	 */

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < getVertices().size(); i++) {
			if (i > 0)
				stringBuilder
						.append(String.format("-%d-[%d]", getEdges().get(i - 1).getID(), getVertices().get(i).getID()));
			else
				stringBuilder.append(String.format("[%d]", getVertices().get(i).getID()));
		}

		return stringBuilder.toString();
	}

	public Tuple<V, V> getSourceTargetTuple() {
		return new Tuple<V, V>(getSource(), getTarget());
	}
}
