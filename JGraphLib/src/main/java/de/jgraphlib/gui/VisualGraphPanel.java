package de.jgraphlib.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Path;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.maths.VectorLine2D;
import de.jgraphlib.util.RandomNumbers;

public class VisualGraphPanel<V extends Vertex<Position2D>, E extends WeightedEdge<?>> extends JPanel {

	private static final long serialVersionUID = 1L;
	private VisualGraph<V, E> graph;
	private Scope scope;
	private double xScale, yScale;
	private int vertexWidth = 40, padding = 2 * vertexWidth;
	private static Stroke EDGE_STROKE = new BasicStroke(1);
	private static BasicStroke EDGE_PATH_STROKE = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
			0.0f, new float[] { 10.0f, 2.0f }, 0);
	private static Stroke VERTEX_STROKE = new BasicStroke(2);
	private static Stroke VERTEX_PATH_STROKE = new BasicStroke(4);
	private static String header = "JGraphLib v1.0";

	public VisualGraphPanel() {
	}

	public VisualGraphPanel(VisualGraph<V, E> graph) {
		this.graph = graph;
		this.scope = this.getScope(graph);
	}

	public int getVertexWidth() {
		return vertexWidth;
	}

	public void setVertexWidth(int vertexWidth) {
		this.vertexWidth = vertexWidth;
	}

	public void paintPlayground(Graphics2D g2) {

		g2.setColor(Color.LIGHT_GRAY);
		FontMetrics fontMetrics = g2.getFontMetrics();
		Rectangle2D stringBounds = fontMetrics.getStringBounds(header, g2);
		g2.drawString(header, padding, padding / 2 + (int) (stringBounds.getHeight() / 2));

		g2.setColor(Color.WHITE);
		g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - (2 * padding));

		g2.setStroke(EDGE_STROKE);
		g2.setColor(Color.LIGHT_GRAY);

		Line2D xAxis = new Line2D(0 * xScale + padding, (scope.y.max - 0) * yScale + padding,
				scope.x.max * xScale + padding, scope.y.max * yScale + padding);

		g2.drawLine((int) xAxis.p1().x(), (int) xAxis.p1().y(), (int) xAxis.p2().x(), (int) xAxis.p2().y());

		VectorLine2D xAxisVector = new VectorLine2D(xAxis.p1().x(), xAxis.p1().y(), xAxis.getSlope());

		Line2D yAxis = new Line2D(0 * xScale + padding, (scope.y.max - 0) * yScale + padding,
				scope.x.min * xScale + padding, scope.y.min * yScale + padding);

		g2.drawLine((int) yAxis.p1().x(), (int) yAxis.p1().y(), (int) yAxis.p2().x(), (int) yAxis.p2().y());

		VectorLine2D yAxisVector = new VectorLine2D(yAxis.p1().x(), yAxis.p1().y(), yAxis.getSlope());

		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		int steps = 20;
		int xOffset = (int) xAxis.getLength() / steps;
		int yOffset = (int) yAxis.getLength() / steps;

		Color gridLineColor = new Color(245, 245, 245);

		for (int i = 1; i < steps; i++) {

			g2.setColor(Color.GRAY);

			Point2D xAxisPoint = xAxisVector.getPointInDistance(i * xOffset);
			g2.drawLine((int) xAxisPoint.x(), (int) xAxisPoint.y(), (int) xAxisPoint.x(),
					(int) xAxisPoint.y() + padding / 4);

			Point2D yAxisPoint = yAxisVector.getPointInDistance(i * -yOffset);
			g2.drawLine((int) yAxisPoint.x(), (int) yAxisPoint.y(), (int) yAxisPoint.x() - padding / 4,
					(int) yAxisPoint.y());

			String xAxisPointText = decimalFormat.format(i * (scope.x.max / steps));
			fontMetrics = g2.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(xAxisPointText, g2);
			g2.drawString(xAxisPointText, (int) xAxisPoint.x() - (int) stringBounds.getCenterX(),
					(int) xAxisPoint.y() - (int) stringBounds.getCenterY() + padding / 2);

			String yAxisPointText = decimalFormat.format(i * (scope.y.max / steps));
			fontMetrics = g2.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(yAxisPointText, g2);
			g2.drawString(yAxisPointText, (int) yAxisPoint.x() - (int) stringBounds.getCenterX(),
					(int) yAxisPoint.y() - (int) stringBounds.getCenterY() - padding / 2);

			g2.setColor(gridLineColor);

			g2.drawLine((int) xAxisPoint.x(), (int) xAxisPoint.y(), (int) xAxisPoint.x(),
					(int) (xAxisPoint.y() - (scope.y.max * yScale)));

			g2.drawLine((int) yAxisPoint.x(), (int) yAxisPoint.y(), (int) (yAxisPoint.x() + (scope.x.max * xScale)),
					(int) (yAxisPoint.y()));
		}
	}

	public void paintEdge(Graphics2D g2, VisualEdge edge) {

		// build (scaled & padded) source and target position of edge
		Point2D startPosition = new Point2D(edge.getStartPosition().x() * xScale + padding,
				(scope.y.max - edge.getStartPosition().y()) * yScale + padding);
		Point2D targetPosition = new Point2D(edge.getTargetPosition().x() * xScale + padding,
				(scope.y.max - edge.getTargetPosition().y()) * yScale + padding);

		// 2D line through source and target position of edge
		Line2D edgeLine = new Line2D(startPosition, targetPosition);

		// Edge Visual Paths

		if (!edge.getVisualPaths().isEmpty()) {

			VectorLine2D startPositionLine = new VectorLine2D(edge.getStartPosition().x() * xScale + padding,
					(scope.y.max - edge.getStartPosition().y()) * yScale + padding, edgeLine.getPerpendicularSlope());

			VectorLine2D targetPositionLine = new VectorLine2D(edge.getTargetPosition().x() * xScale + padding,
					(scope.y.max - edge.getTargetPosition().y()) * yScale + padding, edgeLine.getPerpendicularSlope());

			int i = 0;
			int offset = 0;
			int offsetSteps = 5;

			for (VisualPath visualPath : edge.getVisualPaths()) {

				Point2D pathStartPosition;
				Point2D pathTargetPosition;

				if (i % 2 == 0) {
					pathStartPosition = startPositionLine.getPointInDistance(offset);
					pathTargetPosition = targetPositionLine.getPointInDistance(offset);
					offset += offsetSteps;

				} else {
					pathStartPosition = startPositionLine.getPointInDistance(-offset);
					pathTargetPosition = targetPositionLine.getPointInDistance(-offset);
				}

				g2.setStroke(EDGE_PATH_STROKE);
				g2.setColor(visualPath.getColor());
				g2.drawLine((int) pathStartPosition.x(), (int) pathStartPosition.y(), (int) pathTargetPosition.x(),
						(int) pathTargetPosition.y());

				i++;
			}
		} else {
			g2.setStroke(EDGE_STROKE);
			g2.setColor(Color.GRAY);
			g2.drawLine(edgeLine.x1().intValue(), edgeLine.y1().intValue(), edgeLine.x2().intValue(),
					edgeLine.y2().intValue());
		}

		// Edge Text

		Point2D lineCenter = new Point2D(edgeLine.x1() / 2 + edgeLine.x2() / 2, edgeLine.y1() / 2 + edgeLine.y2() / 2);
		FontMetrics fontMetrics = g2.getFontMetrics();
		Rectangle2D stringBounds = fontMetrics.getStringBounds(edge.getText(), g2);
		g2.setColor(edge.getColor());
		g2.drawString(edge.getText(), (int) lineCenter.x() - (int) stringBounds.getCenterX(),
				(int) lineCenter.y() - (int) stringBounds.getCenterY());

		// Edge Arrows

		int arrowLegLength = 20;

		if (graph.getStyle().isDirected()) {
				
			double shortSide = Math.sqrt(Math.pow(arrowLegLength, 2)/2);
			

			/*		s 	: 	source (given)
			 * 		t 	: 	target (given)
			 * 		l-t	:	arrowLegLength (given)
			 * 		a 	:   (sought)
			 * 		l 	:	leftLegEndPosition (sought)
			 * 		r 	: 	rightLegtEndPosition (sought)
			 * 		 
			 * 							l	
			 * 							|\
			 * 							| \
			 * 		s ------------------a--t
			 * 							| /
			 * 							|/
			 * 							r
			 */
			
			// 2D vector line pointing from target t towards point a
			VectorLine2D vectorLine1 = new VectorLine2D(targetPosition, startPosition, edgeLine.getSlope());
			// 2D vector line pointing from point a towards points l and r
			VectorLine2D vectorLine2 = new VectorLine2D(vectorLine1.getPointInDistance(shortSide),
					edgeLine.getPerpendicularSlope());
			
			Point2D leftLegEndPosition = vectorLine2.getPointInDistance(shortSide);
			Point2D rightLegEndPosition = vectorLine2.getPointInDistance(-shortSide);
	
			g2.setStroke(EDGE_STROKE);
			g2.setColor(Color.GRAY);
			g2.drawLine((int) targetPosition.x(), (int) targetPosition.y(), (int) leftLegEndPosition.x(), (int) leftLegEndPosition.y());
			g2.drawLine((int) targetPosition.x(), (int) targetPosition.y(), (int) rightLegEndPosition.x(), (int) rightLegEndPosition.y());
		}
	}

	public void paintArrow(Graphics2D g2, Point2D position, Point2D reference, double arrowLegLength) {

		Line2D referenceLine = new Line2D(reference.x(), reference.y(), position.x(), position.y());

		double shortSide = Math.sqrt(1 / 2 * Math.pow(arrowLegLength, 2));

		VectorLine2D vectorLine1 = new VectorLine2D(position, reference, referenceLine.getSlope());
		vectorLine1.getPointInDistance(shortSide);

		VectorLine2D vectorLine2 = new VectorLine2D(vectorLine1.getPointInDistance(shortSide),
				referenceLine.getPerpendicularSlope());

		Point2D leftLegEndPosition = vectorLine2.getPointInDistance(shortSide);
		Point2D rightLegEndPosition = vectorLine2.getPointInDistance(-shortSide);

		g2.setStroke(EDGE_PATH_STROKE);
		g2.setColor(Color.GRAY);
		g2.drawLine((int) position.x(), (int) position.y(), (int) leftLegEndPosition.x(), (int) leftLegEndPosition.y());
		g2.drawLine((int) position.x(), (int) position.y(), (int) rightLegEndPosition.x(),
				(int) rightLegEndPosition.y());
	}

	public void paintTrainsmissionRange(Graphics2D g2, VisualVertex vertex) {

		int x = (int) ((vertex.getPosition().x() * xScale + padding) - vertexWidth / 2);
		int y = (int) (((scope.y.max - vertex.getPosition().y()) * yScale + padding) - vertexWidth / 2);

		int transmissionRange = 250;
		g2.setStroke(EDGE_STROKE);
		g2.setColor(new Color(155, 155, 155, 75));
		g2.fillOval(x - (transmissionRange / 2 - (vertexWidth / 2)), y - (transmissionRange / 2 - (vertexWidth / 2)),
				transmissionRange, transmissionRange);
		// g2.drawOval(x - (transmissionRange/2-(vertexWidth /2)) , y -
		// (transmissionRange/2-(vertexWidth/2)), transmissionRange, transmissionRange);

	}

	public void paintVertex(Graphics2D g2, VisualVertex vertex) {
		int x = (int) ((vertex.getPosition().x() * xScale + padding) - vertexWidth / 2);
		int y = (int) (((scope.y.max - vertex.getPosition().y()) * yScale + padding) - vertexWidth / 2);

		g2.setColor(vertex.getBackgroundColor());
		g2.fillOval(x, y, vertexWidth, vertexWidth);

		if (!vertex.getVisualPaths().isEmpty()) {

			int angle = 360 / vertex.getVisualPaths().size();
			int angleOffset = 0;

			for (VisualPath visualPath : vertex.getVisualPaths()) {
				g2.setStroke(new BasicStroke(2 + vertex.getVisualPaths().size()));
				g2.setColor(visualPath.getColor());
				g2.drawArc(x, y, vertexWidth, vertexWidth, angleOffset, angle);
				angleOffset += angle;
			}
		} else {
			g2.setStroke(VERTEX_STROKE);
			g2.setColor(Color.GRAY);
			g2.drawOval(x, y, vertexWidth, vertexWidth);
		}

		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D stringBounds = fm.getStringBounds(vertex.getText(), g2);
		Point vertexCenter = new Point(x + (vertexWidth / 2), y + (vertexWidth / 2));
		g2.setColor(Color.BLACK);
		g2.drawString(vertex.getText(), vertexCenter.x - (int) stringBounds.getCenterX(),
				vertexCenter.y - (int) stringBounds.getCenterY());

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		if (graph.getVertices().isEmpty())
			return;

		this.xScale = ((double) getWidth() - 2 * padding) / (scope.x.max);
		this.yScale = ((double) getHeight() - 2 * padding) / (scope.y.max);

		this.paintPlayground(g2);

		for (VisualEdge edge : graph.getEdges())
			this.paintEdge(g2, edge);

		for (VisualVertex vertex : graph.getVertices())
			this.paintVertex(g2, vertex);
	}

	private class Scope {
		Range x;
		Range y;
		boolean isSet = false;

		@Override
		public String toString() {
			return String.format("x:%d-%d, y:%d-%d", this.x.min, this.x.max, this.y.min, this.y.max);
		}
	}

	private class Range {
		double min;
		double max;

		public Range(double min, double max) {
			this.min = min;
			this.max = max;
		}
	}

	private Scope getScope(VisualGraph<V, E> graph) {

		Scope scope = new Scope();

		for (VisualVertex vertex : graph.getVertices()) {

			if (!scope.isSet) {
				scope.x = new Range(vertex.getPosition().x(), vertex.getPosition().x());
				scope.y = new Range(vertex.getPosition().y(), vertex.getPosition().y());
				scope.isSet = true;
			} else {
				if (vertex.getPosition().x() > scope.x.max)
					scope.x.max = vertex.getPosition().x();
				else if (vertex.getPosition().x() < scope.x.min)
					scope.x.min = vertex.getPosition().x();

				if (vertex.getPosition().y() > scope.y.max)
					scope.y.max = vertex.getPosition().y();
				else if (vertex.getPosition().y() < scope.y.min)
					scope.y.min = vertex.getPosition().y();
			}
		}

		return scope;
	}

	public VisualGraph<V, E> getVisualGraph() {
		return this.graph;
	}

	public void updateVisualGraph(VisualGraph<V, E> graph) {
		this.graph = graph;
		this.scope = this.getScope(graph);
	}

	public void addVisualPath(Path<V, E, ?> path) {
		this.graph.addVisualPath(path);
		this.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
						new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

				NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, new EdgeDistanceSupplier(), new RandomNumbers());

				NetworkGraphProperties properties = new NetworkGraphProperties(1024, 768, new IntRange(100, 200),
						new DoubleRange(50d, 100d), new DoubleRange(100, 100));

				generator.generate(properties);

				VisualGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>> visualGraph = new VisualGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>>(
						graph, new VisualGraphStyle(false));

				RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> randomPath = new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph);

				for (int i = 1; i <= 10; i++)
					visualGraph.addVisualPath(randomPath
							.compute(graph.getVertex(new RandomNumbers().getRandom(0, graph.getVertices().size())), 5));

				VisualGraphPanel<Vertex<Position2D>, WeightedEdge<EdgeDistance>> panel = new VisualGraphPanel<Vertex<Position2D>, WeightedEdge<EdgeDistance>>(
						visualGraph);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int) screenSize.getWidth() * 3 / 4;
				int height = (int) screenSize.getHeight() * 3 / 4;
				panel.setPreferredSize(new Dimension(width, height));
				panel.setFont(new Font("Consolas", Font.PLAIN, 14));
				panel.setLayout(null);

				JFrame frame = new JFrame("VisualGraphPanel");
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
