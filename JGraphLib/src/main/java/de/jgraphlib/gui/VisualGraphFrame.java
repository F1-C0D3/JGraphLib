package de.jgraphlib.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.util.RandomNumbers;

public class VisualGraphFrame<V extends Vertex<Position2D>, E extends WeightedEdge<?>> extends JFrame {

	TerminalPanel terminal;
	VisualGraphPanel<V, E> visualGraphPanel;

	public VisualGraphFrame(VisualGraph<V, E> graph) {

		this.setLayout(new BorderLayout());

		this.visualGraphPanel = new VisualGraphPanel<V, E>(graph);
		this.visualGraphPanel.setFont(new Font("NotoSans", Font.PLAIN, 14));
		this.visualGraphPanel.setLayout(new OverlayLayout(this.visualGraphPanel));

		this.terminal = new TerminalPanel(new Font("Monospace", Font.PLAIN, 16), Color.WHITE);
		this.terminal.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.terminal.setOpaque(false);
		this.terminal.setBackground(new Color(0, 0, 0, 100));
		this.terminal.setVisible(false);
		this.terminal.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				terminal.textArea.requestFocus();
				;
			}
		});

		this.visualGraphPanel.add(this.terminal);
		this.add(visualGraphPanel);

		this.visualGraphPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),
				VisualGraphFrameActions.TOGGLE_TERMINAL);
		this.visualGraphPanel.getActionMap().put(VisualGraphFrameActions.TOGGLE_TERMINAL,
				new VisualGraphFrameAction(VisualGraphFrameActions.TOGGLE_TERMINAL));
	}

	public void scale(double scaleFactor) {

		Font font = visualGraphPanel.getFont();
		visualGraphPanel.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scaleFactor)));
		visualGraphPanel.setVertexWidth((int) (visualGraphPanel.getVertexWidth() * scaleFactor));
		visualGraphPanel.repaint();

		font = terminal.getTextArea().getFont();
		terminal.getTextArea().setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scaleFactor)));
		terminal.repaint();
	}

	public VisualGraphPanel<V, E> getVisualGraphPanel() {
		return this.visualGraphPanel;
	}

	public TerminalPanel getTerminal() {
		return this.terminal;
	}

	private enum VisualGraphFrameActions {
		TOGGLE_TERMINAL;
	}

	private class VisualGraphFrameAction extends AbstractAction {

		VisualGraphFrameActions action;

		public VisualGraphFrameAction(VisualGraphFrameActions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (action) {
			case TOGGLE_TERMINAL:
				if (terminal.isVisible()) {
					terminal.setVisible(false);
				} else {
					terminal.setVisible(true);
				}
				break;
			default:
				break;
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
						new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

				NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = new NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						graph, new EdgeDistanceSupplier());

				NetworkGraphProperties properties = new NetworkGraphProperties(1024, 768, new IntRange(100, 200),
						new DoubleRange(50d, 100d), 100);

				generator.generate(properties);

				VisualGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>> visualGraph = new VisualGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>>(
						graph, new VisualGraphMarkUp());

				RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>> randomPath = new RandomPath<Vertex<Position2D>, WeightedEdge<EdgeDistance>>(
						graph);

				for (int i = 1; i <= 10; i++)
					visualGraph.addVisualPath(randomPath
							.compute(graph.getVertex(RandomNumbers.getRandom(0, graph.getVertices().size())), 5));

				VisualGraphFrame<Vertex<Position2D>, WeightedEdge<EdgeDistance>> frame = new VisualGraphFrame<Vertex<Position2D>, WeightedEdge<EdgeDistance>>(
						visualGraph);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int) screenSize.getWidth() * 3 / 4;
				int height = (int) screenSize.getHeight() * 3 / 4;
				frame.setPreferredSize(new Dimension(width, height));
				frame.setFont(new Font("Consolas", Font.PLAIN, 14));
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
