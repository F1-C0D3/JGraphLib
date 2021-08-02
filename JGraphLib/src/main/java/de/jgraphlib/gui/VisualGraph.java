package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.maths.VectorLine2D;
import de.jgraphlib.util.Tuple;

public class VisualGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

	private ArrayList<VisualVertex> vertices;
	private ArrayList<VisualEdge> edges;
	private ArrayList<VisualPath> paths;
	private VisualGraphStyle style;
	private EdgePrinter<E,W> edgePrinter;

	public VisualGraph(Weighted2DGraph<V, E, ?, ?> graph, VisualGraphStyle style, EdgePrinter<E,W> edgePrinter) {
		this.vertices = new ArrayList<VisualVertex>();
		this.edges = new ArrayList<VisualEdge>();
		this.paths = new ArrayList<VisualPath>();
		this.style = style;
		this.edgePrinter = edgePrinter;
		
		buildVertices(graph);
		
		if(graph.isDirected())
			buildDirectedEdges((DirectedWeighted2DGraph<V, E, ?, ?>) graph);
		else
			buildUndirectedEdges((UndirectedWeighted2DGraph<V, E, ?, ?>) graph);
				
		buildPaths(graph);		
	}
	
	public void setEdgePrinter(EdgePrinter<E,W> edgePrinter) {
		this.edgePrinter = edgePrinter;
	}
	
	public Boolean hasEdgePrinter() {
		return edgePrinter != null;
	}

	private void buildVertices(Weighted2DGraph<V, E, ?, ?> graph) {
		for (Vertex<Position2D> vertex : graph.getVertices())
			this.vertices.add(
					new VisualVertex(
							vertex.getPosition(), 
							style.getVertexBackgroundColor(),
							style.getVertexBorderColor(), 
							Integer.toString(vertex.getID())));
	}

	private void buildUndirectedEdges(UndirectedWeighted2DGraph<V, E, ?, ?> graph) {
		for (E edge : graph.getEdges()) {

			String edgeText = "";

			if (edge.getWeight() != null)
				if(hasEdgePrinter())
					edgeText = edgePrinter.print(edge);
				else
					edgeText = edge.getWeight().toString();

			Tuple<V, V> vertices = graph.getVerticesOf(edge);

			Tuple<Point2D, Point2D> edgePosition = buildEdgePosition(vertices.getFirst().getPosition(),
					vertices.getSecond().getPosition());

			this.edges.add(
					new VisualEdge(edgePosition.getFirst(), edgePosition.getSecond(), style.getEdgeColor(), edgeText));
		}
	}

	private void buildDirectedEdges(DirectedWeighted2DGraph<V, E, ?, ?> graph) {
		for (E edge : graph.getEdges()) {

			String edgeText = "";

			if (edge.getWeight() != null)
				if(hasEdgePrinter())
					edgeText = edgePrinter.print(edge);
				else
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

	public void buildPaths(Weighted2DGraph<V, E, ?, ?> graph) {
		
		Random random = new Random();
		
		for(Path<V,E,?> path : graph.getPaths()) 
			buildPath(path, new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));	
	}
	
	public void buildPath(Path<V, E, ?> path) {	
		Random random = new Random();
		buildPath(path, new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
	}

	public void buildPath(Path<V, E, ?> path, Color color) {

		VisualPath visualPath = new VisualPath(color);

		for (Tuple<E, V> edgeAndVertex : path) {

			E edge = edgeAndVertex.getFirst();
			V vertex = edgeAndVertex.getSecond();

			if (edge != null)
				edges.get(edge.getID()).addVisualPath(visualPath);

			if (vertex != null)
				vertices.get(vertex.getID()).addVisualPath(visualPath);
		}
		
		this.paths.add(visualPath);
	}
}
