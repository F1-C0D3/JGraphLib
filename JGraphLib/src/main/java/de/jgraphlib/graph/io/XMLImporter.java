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

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.WeightedGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.Tuple;

public class XMLImporter<V extends Vertex<P>, P, E extends WeightedEdge<W>, W> {

	WeightedGraph<V, P, E, W> graph;
	VertexPositionXMLInterface<P> vertexPositionXMLInterface;
	EdgeWeightXMLInterface<W> edgeWeightXMLInterface;

	public XMLImporter(WeightedGraph<V, P, E, W> graph, VertexPositionXMLInterface<P> vertexPositionInterface,
			EdgeWeightXMLInterface<W> edgeWeightInterface) {
		this.graph = graph;
		this.vertexPositionXMLInterface = vertexPositionInterface;
		this.edgeWeightXMLInterface = edgeWeightInterface;
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
				graph.addVertex(vertexPositionXMLInterface.translate(xmlVertexAttributes));
			}

			// Add edges
			List<Node> xmlEdges = getElementChildNodes(document.getElementsByTagName("edges").item(0));

			for (Node xmlEdge : xmlEdges) {
				V source = graph.getVertices()
						.get(Integer.parseInt(getChildbyName(xmlEdge, "source").getTextContent()));
				V target = graph.getVertices()
						.get(Integer.parseInt(getChildbyName(xmlEdge, "target").getTextContent()));

				List<Tuple<String, String>> edgeWeightAttributeValuePairs = getNameTextContentPairs(
						getElementChildNodes(getChildbyName(xmlEdge, "weight")));
				W weight = edgeWeightXMLInterface.translate(edgeWeightAttributeValuePairs);

				//System.out.println(String.format("import Edge(source:%d, target %d, weight:%s)", source.getID(), target.getID(), weight.toString()));
				
				graph.addEdge(source, target, weight);
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
		UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
				new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier(), new EdgeDistanceSupplier());

		XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance> importer = new XMLImporter<Vertex<Position2D>, Position2D, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new VertexPositionXMLInterface<Position2D>() {
					@Override
					public List<Tuple<String, String>> translate(Position2D position) {
						List<Tuple<String, String>> attributesValues = new ArrayList<Tuple<String, String>>();
						attributesValues.add(new Tuple<String, String>("x", Double.toString(position.x())));
						attributesValues.add(new Tuple<String, String>("y", Double.toString(position.y())));
						return attributesValues;
					}

					@Override
					public Position2D translate(List<Tuple<String, String>> attributesValues) {
						return new Position2D(Double.parseDouble(attributesValues.get(0).getSecond()),
								Double.parseDouble(attributesValues.get(1).getSecond()));
					}
				}, new EdgeWeightXMLInterface<EdgeDistance>() {

					@Override
					public List<Tuple<String, String>> translate(EdgeDistance edgeWeight) {
						List<Tuple<String, String>> attributesValues = new ArrayList<Tuple<String, String>>();

						attributesValues
								.add(new Tuple<String, String>("distance", Double.toString(edgeWeight.getDistance())));
						return attributesValues;
					}

					@Override
					public EdgeDistance translate(List<Tuple<String, String>> attributesValues) {
						return new EdgeDistance(Double.parseDouble(attributesValues.get(0).getSecond()));
					}
				});

		importer.importGraph("graph.xml");

		VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> app = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
				graph, new EdgeDistanceSupplier());
	}
}
