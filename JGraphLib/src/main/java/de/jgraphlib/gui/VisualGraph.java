package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.printer.EdgePrinter;
import de.jgraphlib.gui.printer.EdgeWeightPrinter;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.gui.style.VisualGraphStyle;
import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.maths.VectorLine2D;
import de.jgraphlib.util.Tuple;

// @formatter:off

public class VisualGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

	private TreeMap<Integer,VisualVertex> visualVertices;
	private TreeMap<Integer,VisualEdge> visualEdges;
	private ArrayList<VisualEdgeTuple> visualEdgeTuples;	
	private ArrayList<VisualPath> paths;
	private VisualGraphStyle style;
	private EdgePrinter<E, W> edgePrinter;
	private EdgeWeightPrinter<W> edgeWeightPrinter;
	
	public VisualGraph(Weighted2DGraph<V, E, W, ?> graph, VisualGraphStyle style, EdgePrinter<E, W> edgePrinter) {
		this.visualVertices = new TreeMap<Integer, VisualVertex>();
		this.visualEdges = new TreeMap<Integer, VisualEdge>();
		this.visualEdgeTuples = new ArrayList<VisualEdgeTuple>();
		this.paths = new ArrayList<VisualPath>();
		this.style = style;		
		this.edgePrinter = edgePrinter;
		createVisualVertices(graph);	
		createVisualEdges((DirectedWeighted2DGraph<V, E, ?, ?>) graph);
		createVisualPaths(graph.getPaths());		
	}
	
	public VisualGraph(Weighted2DGraph<V, E, W, ?> graph, VisualGraphStyle style, EdgeWeightPrinter<W> edgeWeightPrinter) {
		this.visualVertices = new TreeMap<Integer, VisualVertex>();
		this.visualEdges = new TreeMap<Integer, VisualEdge>();
		this.visualEdgeTuples = new ArrayList<VisualEdgeTuple>();
		this.paths = new ArrayList<VisualPath>();
		this.style = style;		
		this.edgeWeightPrinter = edgeWeightPrinter;
		createVisualVertices(graph);	
		createVisualEdges((DirectedWeighted2DGraph<V, E, ?, ?>) graph);
		createVisualPaths(graph.getPaths());		
	}
	
	public VisualGraph(Weighted2DGraph<V, E, W, ?> graph, VisualGraphStyle style) {
		this(graph, style, new WeightedEdgeIDPrinter<E,W>());
	}

	public Boolean hasEdgePrinter() {
		return !Objects.isNull(edgePrinter);
	}
	
	public Boolean hasEdgeWeightPrinter() {
		return !Objects.isNull(edgeWeightPrinter);
	}

	private void createVisualVertices(Weighted2DGraph<V, E, ?, ?> graph) {
		for (Vertex<Position2D> vertex : graph.getVertices())
			visualVertices.put(
					vertex.getID(),
					new VisualVertex(
							vertex.getPosition(), 
							style.getVertexBackgroundColor(),style.getVertexBorderColor(), 
							Integer.toString(vertex.getID())));
	}

	/*private void createUndirectedVisualEdges(UndirectedWeighted2DGraph<V, E, ?, ?> graph) {
		for (E edge : graph.getEdges()) {
			Tuple<V, V> vertices = graph.getVerticesOf(edge);
			Tuple<Point2D, Point2D> edgePosition = createVisualEdgePosition(vertices.getFirst().getPosition(), vertices.getSecond().getPosition());
			//visualEdges.add(new VisualEdge(edgePosition.getFirst(), edgePosition.getSecond(), style.getEdgeColor(), createEdgeText(edge)));
		}
	}*/

	private void createVisualEdges(DirectedWeighted2DGraph<V, E, ?, ?> graph) {

		Set<E> memorize = new HashSet<E>();
		
		for (E edge : graph.getEdges()) {
			
			if (!memorize.contains(edge)) {

				V source = graph.getSourceOf(edge);
				V target = graph.getTargetOf(edge);
				
				//System.out.println(String.format("edge %d: source %d, target %d", edge.getID(), source.getID(), target.getID()));
				
				if (graph.containsEdge(target, source)) {		
					
					Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>> visualEdgeTuplePosition = createVisualEdgeTuplePosition(source.getPosition(), target.getPosition());	
					
					VisualEdgeTuple visualEdgeTuple = new VisualEdgeTuple();
					
					visualEdgeTuple.setFirst(
							new VisualEdge(
									visualEdgeTuplePosition.getFirst(), 
									style.getEdgeColor(), 
									createEdgeTextPosition(source.getPosition(), target.getPosition(), EdgeTextPosition.top_center),
									createEdgeText(edge)));
					
					visualEdges.put(edge.getID(), visualEdgeTuple.getFirst());
					
					E opposedEdge = graph.getEdge(target, source);
					
					//System.out.println(String.format("edge %d: source %d, target %d", opposedEdge.getID(), graph.getSourceOf(opposedEdge).getID(), graph.getTargetOf(opposedEdge).getID()));
		
					visualEdgeTuple.setSecond(
							new VisualEdge(
									visualEdgeTuplePosition.getSecond(), 
									style.getEdgeColor(), 
									createEdgeTextPosition(graph.getSourceOf(opposedEdge).getPosition(), graph.getTargetOf(opposedEdge).getPosition(), EdgeTextPosition.top_center),
									createEdgeText(opposedEdge)));
					
					visualEdges.put(opposedEdge.getID(), visualEdgeTuple.getSecond());
					
					visualEdgeTuples.add(visualEdgeTuple);
					
					memorize.add(opposedEdge);
				}
				else visualEdges.put(
						edge.getID(),
						new VisualEdge(
								createVisualEdgePosition(source.getPosition(), target.getPosition()), 
								style.getEdgeColor(), 
								createEdgeTextPosition(source.getPosition(), target.getPosition(), EdgeTextPosition.center), 
								createEdgeText(edge)));
			}
		}
	}

	private Point2D createEdgeTextPosition(Position2D source, Position2D target, EdgeTextPosition edgeTextPosition) {	
		
		Point2D center = new Point2D(source.x() / 2 + target.x() / 2, source.y() / 2 + target.y() / 2);
		
		switch(edgeTextPosition) {
		// Place edge text directly into the center of the line
		case center:
			return center;
		// Place edge text in between the center and the target position
		case top_center:	
			Line2D centerTargetLine = new Line2D(center.x(), center.y(), target.x(), target.y());
			return new Point2D(centerTargetLine.x1() / 2 + centerTargetLine.x2() / 2, centerTargetLine.y1() / 2 + centerTargetLine.y2() / 2);
		// Place edge text in between the center and the source position
		case bottom_center:
			Line2D centerSourceLine = new Line2D(center.x(), center.y(), source.x(), source.y());
			return new Point2D(centerSourceLine.x1() / 2 + centerSourceLine.x2() / 2, centerSourceLine.y1() / 2 + centerSourceLine.y2() / 2);
		default:
			break;			
		}
		return null;	
	}
	
	private enum EdgeTextPosition{
		center,
		top_center,
		bottom_center
	}
	
	private String createEdgeText(E edge) {
		
		if (!Objects.isNull(edge.getWeight())) {	
			if(hasEdgeWeightPrinter()) 
				return edgeWeightPrinter.print(edge.getWeight());	
			else if (hasEdgePrinter()) 
				return edgePrinter.print(edge);
			else 
				return edge.getWeight().toString();	
		}
		else 
			return edge.toString();
	}

	private Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>> createVisualEdgeTuplePosition(Position2D a, Position2D b){
		
		Tuple<Point2D, Point2D> visualEdgePosition = createVisualEdgePosition(a, b);
		
		Line2D visualEdgePositionLine = new Line2D(visualEdgePosition.getFirst(), visualEdgePosition.getSecond());
		
		VectorLine2D aVectorLine = new VectorLine2D(visualEdgePosition.getFirst(), visualEdgePositionLine.getPerpendicularSlope());
				
		VectorLine2D bVectorLine = new VectorLine2D(visualEdgePosition.getSecond(), visualEdgePositionLine.getPerpendicularSlope());

		Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>> visualEdgeTuplePosition = new Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>>();
		
		visualEdgeTuplePosition.setFirst(new Tuple<Point2D,Point2D>(
				aVectorLine.getPointInDistance(style.getVisualEdgeTupleDistance()/2),
				bVectorLine.getPointInDistance(style.getVisualEdgeTupleDistance()/2)));
		
		visualEdgeTuplePosition.setSecond(new Tuple<Point2D,Point2D>(
				bVectorLine.getPointInDistance(-style.getVisualEdgeTupleDistance()/2),
				aVectorLine.getPointInDistance(-style.getVisualEdgeTupleDistance()/2)));
		
		return visualEdgeTuplePosition;
	}
	
	private Tuple<Point2D, Point2D> createVisualEdgePosition(Position2D source, Position2D target) {

		// 2D line through source and target position
		Line2D edgeLine = new Line2D(source, target);

		// 2D vector line in source position
		VectorLine2D sourcePositionVectorLine = new VectorLine2D(new Point2D(source.x(), source.y()), edgeLine.getCenter(), edgeLine.getSlope());
		
		// 2D vector line in target position
		VectorLine2D targetPositionVectorLine = new VectorLine2D(new Point2D(target.x(), target.y()),edgeLine.getCenter(), edgeLine.getSlope());

		Tuple<Point2D, Point2D> edgePosition = new Tuple<Point2D, Point2D>();

		// Calculate edge positions
		edgePosition.setFirst(sourcePositionVectorLine.getPointInDistance(style.getVertexWidth() / 2));
		edgePosition.setSecond(targetPositionVectorLine.getPointInDistance(style.getVertexWidth() / 2));

		return edgePosition;
	}

	public <P extends Path<V, E, W>> void createVisualPaths(List<P> paths) {
		Random random = new Random();
		for (P path : paths)
			createVisualPath(path, new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
	}

	public void createVisualPath(Path<V, E, ?> path) {
		Random random = new Random();
		createVisualPath(path, new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
	}

	public void createVisualPath(Path<V, E, ?> path, Color color) {

		VisualPath visualPath = new VisualPath(color);

		for (V vertex : path.getVertices()) 
			visualVertices.get(vertex.getID()).addVisualPath(visualPath);
		
		for (E edge : path.getEdges()) 
			visualEdges.get(edge.getID()).addVisualPath(visualPath);

		this.paths.add(visualPath);
	}

	public List<VisualVertex> getVisualVertices() {
		return new ArrayList<VisualVertex>(visualVertices.values());
	}

	public List<VisualEdge> getVisualEdges() {
		return new ArrayList<VisualEdge>(visualEdges.values());
	}
	
	public List<VisualEdgeTuple> getVisualEdgeTuples() {
		return visualEdgeTuples;
	}

	public VisualGraphStyle getStyle() {
		return this.style;
	}
}
