package de.jgraphlib.graph.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class PathProblemGenerator<V extends Vertex<?>, E extends WeightedEdge<W>, W, P extends Path<V,E,W>> {

	protected RandomNumbers randomNumbers;
	protected Supplier<P> pathSupplier;
	
	public PathProblemGenerator(RandomNumbers randomNumbers, Supplier<P> pathSupplier) {
		this.randomNumbers = randomNumbers;
		this.pathSupplier = pathSupplier;
	}
	
	private V randomNode(WeightedGraph<V, ?, E, W, ?> graph) {				
		return graph.getVertices().get(randomNumbers.getRandom(0, graph.getVertices().size()-1));
	}
	
	public List<P> generate(WeightedGraph<V, ?, E, W, ?> graph, PathProblemProperties pathProblemProperties){	
		
		List<P> problems = new ArrayList<P>();	
		
		RandomPath<V,E,W> randomPath = new RandomPath<V,E,W>(graph);	
		
		for(int i=0; i < pathProblemProperties.getPathCount(); i++) {	
			
			V source = randomNode(graph);
			
			int pathLength = randomNumbers.getRandom(pathProblemProperties.getMinLength(), pathProblemProperties.getMaxLength());
			
			Path<V,E,W> path;

			while((path = randomPath.compute(source, pathLength)).size() < pathLength) {
				source = randomNode(graph);
				path = randomPath.compute(source, pathLength);	
			}
			
			P problem = pathSupplier.get();
			
			problem.set(path.getSource(), path.getTarget());
			
			problems.add(problem);
		}
		
		return problems;
	}
	
}
