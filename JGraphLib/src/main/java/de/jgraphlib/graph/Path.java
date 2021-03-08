package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import de.jgraphlib.util.Tuple;

public class Path<V extends Vertex<?>, E extends WeightedEdge<W>, W> extends LinkedList<Tuple<E, V>> {

	private static final long serialVersionUID = 1L;
	protected V source;
	protected V target;
	
	public Path() {
		this.source = null;
		this.target = null;
	}
	
	public Path(V source) {
		this.source = source;
		super.add(new Tuple<E, V>(null, source));
	}

	public Path(V source, V target) {
		this.source = source;
		this.target = target;
		super.add(new Tuple<E, V>(null, source));
	}
	
	public Path(Path<V,E,W> path) {
		this.source = path.source;
		this.target = path.target;
		this.addAll(path);
	}
	
	@Override
	public boolean add(Tuple<E, V> tuple) {
		return super.add(tuple);
	}

	@Override
	public void clear() {
		super.clear();
		super.add(new Tuple<E, V>(null, source));
	}

	public V getSource() {
		return this.source;
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
		List<E> vertices = new ArrayList<E>();
		for (Tuple<E, V> tuple : this)
			vertices.add(tuple.getFirst());
		return vertices;
	}

	@Override
	public Tuple<E, V> getFirst() {
		if (this.get(0) != null)
			return this.get(0);
		return null;
	}

	@Override
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
			for (Tuple<E, V> tuple : this)
				if (edge.equals(tuple.getFirst()))
					return true;
		return false;
	}

	public Boolean isComplete() {
		if (this.getLast() != null)
			return this.getLast().getSecond().equals(target);
		return false;
	}

	public List<V> getUnvisitedVertices(List<V> vertices) {
		List<V> unvisitedVertices = new ArrayList<V>();
		for (V vertex : vertices)
			if (!this.contains(vertex))
				unvisitedVertices.add(vertex);
		return unvisitedVertices;
	}

	public List<V> getVisitedVertices() {
		List<V> vertices = new ArrayList<V>();
		for (Tuple<E, V> tuple : this)
			vertices.add(tuple.getSecond());
		return vertices;
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

	public Double getCost(Function<W, Double> metric) {
		if (metric != null) {
			Double cost = 0d;
			for (Tuple<E, V> tuple : this)
				if (tuple.getFirst() != null)
					cost += metric.apply(tuple.getFirst().getWeight());
			if (cost > 0)
				return cost;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Tuple<E, V>> iterator = this.iterator();
		while (iterator.hasNext()) {
			Tuple<E, V> next = iterator.next();
			if (iterator.hasNext())
				stringBuilder.append(next.getSecond().getID()).append(", ");
			else
				stringBuilder.append(next.getSecond().getID());
		}
		return stringBuilder.toString();
	}
}
