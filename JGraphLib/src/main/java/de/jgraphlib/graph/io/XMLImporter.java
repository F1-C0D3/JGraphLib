package de.jgraphlib.graph.io;

import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.WeightedGraph;

public class XMLImporter<V extends Vertex<P>, P, E extends WeightedEdge<W>,W> {

	WeightedGraph<V, P, E, W> graph;

    public XMLImporter(WeightedGraph<V, P, E, W> graph) {
	this.graph = graph;
    }
    
    /*
    public boolean importGraph(String filePath) {
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(graph.getClass());
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    InputStream inputStream = new FileInputStream(filePath);
	    graph = (WeightedUndirectedGraph<V, E>) jaxbUnmarshaller.unmarshal(inputStream);
	    return true;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}

	return false;
    }*/
}

