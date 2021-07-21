package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.EdgeWeightSupplier;
import de.jgraphlib.graph.Path;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.maths.VectorLine2D;
import de.jgraphlib.util.Tuple;

public class VisualGraph<V extends Vertex<Position2D>, E extends WeightedEdge<?>> {

	private ArrayList<VisualVertex> vertices;
	private ArrayList<VisualEdge> edges;
	private VisualGraphStyle style;

	public VisualGraph(Weighted2DGraph<V, E, ?> graph, VisualGraphStyle markUp) {
		this.vertices = new ArrayList<VisualVertex>();
		this.edges = new ArrayList<VisualEdge>();
		this.style = markUp;
		
		buildVertices(graph);
		
		// quick and dirty --> replace with visitor pattern
		if(graph instanceof UndirectedWeighted2DGraph)
			buildUndirectedEdges((UndirectedWeighted2DGraph<V, E, ?>) graph);
		
		// quick and dirty --> replace with visitor pattern
		if(graph instanceof DirectedWeighted2DGraph) 
			buildDirectedEdges((DirectedWeighted2DGraph<V, E, ?>) graph);				
	}
	
	private void buildVertices(Weighted2DGraph<V, E, ?> graph) {
		for (Vertex<Position2D> vertex : graph.getVertices())
			this.vertices.add(
					new VisualVertex(
							vertex.getPosition(), 
							style.getVertexBackgroundColor(),
							style.getVertexBorderColor(), 
							Integer.toString(vertex.getID())));
	}

	private void buildUndirectedEdges(UndirectedWeighted2DGraph<V, E, ?> graph) {
		for (E edge : graph.getEdges()) {

			String edgeText = "";

			if (edge.getWeight() != null)
				edgeText = edge.getWeight().toString();

			Tuple<V, V> vertices = graph.getVerticesOf(edge);

			Tuple<Point2D, Point2D> edgePosition = buildEdgePosition(vertices.getFirst().getPosition(),
					vertices.getSecond().getPosition());

			this.edges.add(
					new VisualEdge(edgePosition.getFirst(), edgePosition.getSecond(), style.getEdgeColor(), edgeText));
		}
	}

	private void buildDirectedEdges(DirectedWeighted2DGraph<V, E, ?> graph) {
		for (E edge : graph.getEdges()) {

			String edgeText = "";

			if (edge.getWeight() != null)
				edgeText = edge.getWeight().toString();

			V source = graph.getSourceOf(edge);
			V target = graph.getTargetOf(edge);

			Tuple<Point2D, Point2D> edgePosition = buildEdgePosition(source.getPosition(), target.getPosition());

			this.edges.add(
					new VisualEdge(edgePosition.getFirst(), edgePosition.getSecond(), style.getEdgeColor(), edgeText));
		}
	}

	private Tuple<Point2D, Point2D> buildEdgePosition(Position2D source, Position2D target) {

		// 2D line through source and target position
		Line2D edgeLine = new Line2D(source, target);

		// 2D vector line in source position
		VectorLine2D sourcePositionVectorLine = new VectorLine2D(new Point2D(source.x(), source.y()),
				edgeLine.getCenter(), edgeLine.getSlope());
		// 2D vector line in target position
		VectorLine2D targetPositionVectorLine = new VectorLine2D(new Point2D(target.x(), target.y()),
				edgeLine.getCenter(), edgeLine.getSlope());

		Tuple<Point2D, Point2D> edgePosition = new Tuple<Point2D, Point2D>();

		// Calculate edge positions
		edgePosition.setFirst(sourcePositionVectorLine.getPointInDistance(style.getVertexWidth() / 2));
		edgePosition.setSecond(targetPositionVectorLine.getPointInDistance(style.getVertexWidth() / 2));

		return edgePosition;
	}

	public List<VisualVertex> getVertices() {
		return this.vertices;
	}

	public List<VisualEdge> getEdges() {
		return this.edges;
	}
	
	public VisualGraphStyle getStyle() {
		return this.style;
	}

	public void addVisualPath(Path<V, E, ?> path) {
		Random random = new Random();
		Color visualPathColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
		this.addVisualPath(path, visualPathColor);
	}

	public void addVisualPath(Path<V, E, ?> path, Color color) {

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
