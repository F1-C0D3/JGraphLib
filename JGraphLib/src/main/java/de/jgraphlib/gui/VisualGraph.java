package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jgraphlib.graph.Path;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class VisualGraph<V extends Vertex<Position2D>, E extends WeightedEdge<?>> {

    private ArrayList<VisualVertex> vertices;
    private ArrayList<VisualEdge> edges;

    public VisualGraph(UndirectedWeighted2DGraph<V, E, ?> graph, VisualGraphMarkUp markUp) {

	this.vertices = new ArrayList<VisualVertex>();
	this.edges = new ArrayList<VisualEdge>();

	for (Vertex<Position2D> vertex : graph.getVertices())
	    this.vertices.add(new VisualVertex(vertex.getPosition(), markUp.getVertexBackgroundColor(),
		    markUp.getVertexBorderColor(), Integer.toString(vertex.getID())));

	for (E edge : graph.getEdges()) {

	    String edgeText = "";

	    if (edge.getWeight() != null)
		edgeText = edge.getWeight().toString();

	    Tuple<V, V> vertices = graph.getVerticesOf(edge);

	    this.edges.add(new VisualEdge(vertices.getFirst().getPosition(), vertices.getSecond().getPosition(),
		    markUp.getEdgeColor(), edgeText));
	}
    }

    public List<VisualVertex> getVertices() {
	return this.vertices;
    }

    public List<VisualEdge> getEdges() {
	return this.edges;
    }

    public void addVisualPath(Path<V, E> path) {
	Random random = new Random();
	Color visualPathColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
	this.addVisualPath(path, visualPathColor);
    }

    public void addVisualPath(Path<V, E> path, Color color) {

	VisualPath visualPath = new VisualPath(color);

	for (Tuple<E, V> edgeAndVertex : path) {

	    E edge = edgeAndVertex.getFirst();
	    V vertex = edgeAndVertex.getSecond();

	    if (edge != null)
		edges.get(edge.getID()).addVisualPath(visualPath);

	    if (vertex != null)
		vertices.get(vertex.getID()).addVisualPath(visualPath);
	}
    }
}
