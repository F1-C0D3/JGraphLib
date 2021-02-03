package de.jgraphlib.algorithms;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

public class DijkstraShortestPathTest {
	@Test
	public void DijkstraShortestPathTest() {
		/*Manet<Node, Link> manet = new Manet<Node, Link>(new ManetSupplier.ManetNodeSupplier(),
				new ManetSupplier.ManetLinkSupplier(), new IdealRadioModel(100d, new DataRate(2d, Unit.Type.megabit)));

		GraphGenerator<Node, Link> generator = new GraphGenerator<Node, Link>(manet);
		GraphProperties playground = new GraphProperties(1024, 768, new IntRange(100, 200), new DoubleRange(50d, 100d),
				new IntRange(2, 4), new DoubleRange(50d, 100d));
		generator.generateRandomGraph(playground);

		Function<Tuple<Link, Node>, Double> metric = (Tuple<Link, Node> t) -> {
			return 1d;
		};

		DijkstraShortestPath<Node, Link> dijkstra = new DijkstraShortestPath<Node, Link>(manet);
		Path<Node, Link> shortestPath = dijkstra.compute(manet.getFirstVertex(), manet.getLastVertex(), metric);

		List<Integer> spCompare = new ArrayList<Integer>();
		spCompare.add(0);
		spCompare.add(1);
		spCompare.add(3);
		Iterator<Tuple<Link, Node<Link>>> it = shortestPath.iterator();
		List<Integer> spComputed = new ArrayList<Integer>();
		while (it.hasNext()) {
			spComputed.add(it.next().getSecond().getID());
		}
		assertTrue(spCompare.equals(spComputed));
		*/
	}
}