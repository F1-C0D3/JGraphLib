package de.jgraphlib.graph.io;

import de.jgraphlib.graph.UndirectedWeightedGraph;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;

public class XMLExporter<V extends Vertex<P>, P, E extends WeightedEdge<W>,W> {

    UndirectedWeightedGraph<V, P, E, W> graph;

    public XMLExporter(UndirectedWeightedGraph<V, P, E, W> graph) {
	this.graph = graph;
    }

    /*
    public boolean exportGraph(String filePath) {
	/*try {
	    JAXBContext jaxbContent = JAXBContext.newInstance(graph.getClass());
	    Marshaller marshaller = jaxbContent.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    OutputStream outputStream = new FileOutputStream(filePath);
	    marshaller.marshal(graph, outputStream);
	    outputStream.close();
	    return true;
	} catch (JAXBException exception) {
	    exception.printStackTrace();
	} catch (IOException exception) {
	    exception.printStackTrace();
	}

	return false;
    }*/
}
