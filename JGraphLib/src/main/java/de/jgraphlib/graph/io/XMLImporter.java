package de.jgraphlib.graph.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.Tuple;

public class XMLImporter<V extends Vertex<P>, P, E extends WeightedEdge<W>, W> {

	WeightedGraph<V, P, E, W, ?> graph;
	VertexPositionMapper<P> vertexPositionMapper;
	//EdgeWeightMapper<W> edgeWeightMapper;

	public XMLImporter(WeightedGraph<V, P, E, W, ?> graph, VertexPositionMapper<P> vertexPositionInterface /*,
			EdgeWeightMapper<W> edgeWeightInterface*/) {
		this.graph = graph;
		this.vertexPositionMapper = vertexPositionInterface;
		//this.edgeWeightMapper = edgeWeightInterface;
	}

	private List<Node> getElementChildNodes(Node node) {

		List<Node> elementChildNodes = new ArrayList<Node>();

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {

			Node childNode = node.getChildNodes().item(i);

			switch (childNode.getNodeType()) {
			case Node.ELEMENT_NODE:
				elementChildNodes.add(childNode);
				break;
			case Node.CDATA_SECTION_NODE:
				break;
			case Node.TEXT_NODE:
				break;
			}
		}

		return elementChildNodes;
	}

	private List<Tuple<String, String>> getNameTextContentPairs(List<Node> nodes) {
		List<Tuple<String, String>> attributeValuePairs = new ArrayList<Tuple<String, String>>();
		for (Node node : nodes)
			attributeValuePairs.add(new Tuple<String, String>(node.getNodeName(), node.getTextContent()));
		return attributeValuePairs;
	}

	public static Element getChildbyName(Node parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
			if (child instanceof Element && name.equals(child.getNodeName()))
				return (Element) child;
		return null;
	}

	public boolean importGraph(String xmlFilePath) {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFilePath);
			document.getDocumentElement().normalize();

			// Add vertices
			List<Node> xmlVertices = getElementChildNodes(document.getElementsByTagName("vertices").item(0));

			for (Node xmlVertex : xmlVertices) {
				List<Tuple<String, String>> xmlVertexAttributes = getNameTextContentPairs(
						getElementChildNodes(xmlVertex));
				
				graph.addVertex(vertexPositionMapper.translate(xmlVertexAttributes));
			}

			// Add edges
			List<Node> xmlEdges = getElementChildNodes(document.getElementsByTagName("edges").item(0));

			for (Node xmlEdge : xmlEdges) {
				V source = graph.getVertices()
						.get(Integer.parseInt(getChildbyName(xmlEdge, "source").getTextContent()));
				V target = graph.getVertices()
						.get(Integer.parseInt(getChildbyName(xmlEdge, "target").getTextContent()));

				/*List<Tuple<String, String>> edgeWeightAttributeValuePairs = getNameTextContentPairs(
						getElementChildNodes(getChildbyName(xmlEdge, "weight")));
				
				W weight = edgeWeightMapper.translate(edgeWeightAttributeValuePairs);*/

				// System.out.println(String.format("import Edge(source:%d, target %d,
				// weight:%s)", source.getID(), target.getID(), weight.toString()));

				graph.addEdge(source, target);
			}

			return true;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/* Test */
	public static void main(String args[]) {

		// Empty graph
		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>> graph = 
				new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance, Path<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier(),
						new Weighted2DGraphSupplier().getPathSupplier());

		XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance> importer = new XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new VertextPosition2DMapper());

		importer.importGraph("graph.xml");

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> app = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph);
	}
}
