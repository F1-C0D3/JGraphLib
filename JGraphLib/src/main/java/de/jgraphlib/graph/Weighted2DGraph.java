package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public abstract class Weighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance, P extends Path<V,E,W>>
		extends WeightedGraph<V, Position2D, E, W, P> {

	public Weighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier, Supplier<P> pathSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier, pathSupplier);
	}
		
	public List<E> copyEdges() {	
		List<E> linkCopies = new ArrayList<E>();	
		for (E edge : getEdges()) {			
			E edgeCopy = edgeSupplier.get();		
			edgeCopy.setID(edge.getID());
			W edgeWeight = edgeWeightSupplier.get();
			edgeWeight.setDistance(edge.getWeight().getDistance());
			edgeCopy.setWeight(edgeWeight);		
			linkCopies.add(edgeCopy);
		}
		return linkCopies;
	}
	
	public V addVertex(double x, double y) {
		return super.addVertex(new Position2D(x, y));
	}

	public double getDistance(Position2D p1, Position2D p2) {
		return Math.sqrt(Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2));
	}
	
	public List<V> getVerticesInRadius(Position2D position, double radius) {
		List<V> vertices = new ArrayList<V>();
		for (Entry<Integer, V> entry : this.vertices.entrySet())
			if (getDistance(position, entry.getValue().getPosition()) <= radius)
				vertices.add(entry.getValue());
		return vertices;
	}

	public List<V> getVerticesInRadius(V source, double radius) {
		return getVerticesInRadius(source.getPosition(), radius);
	}

	public Boolean vertexInRadius(Position2D position, double radius) {
		for (Entry<Integer, V> entry : this.vertices.entrySet()) {
			if (getDistance(position, entry.getValue().getPosition()) <= radius)
				return true;
		}
		return false;
	}
	
	public Boolean vertexInRadius(Position2D position, double radius, List<V> exceptions) {
		for (Entry<Integer, V> entry : this.vertices.entrySet()) {
			if(!exceptions.contains(entry.getValue()))
				if (getDistance(position, entry.getValue().getPosition()) <= radius)
					return true;
		}
		return false;
	}
	
	public Boolean vertexInRadius(V source, double radius) {
		return vertexInRadius(source.getPosition(), radius);
	}

}
