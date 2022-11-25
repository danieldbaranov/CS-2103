import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;
import java.io.*;

/**
 * Code to test an <tt>GraphSearchEngine</tt> implementation.
 */
public class GraphSearchEngineTester {
	@Test
	@Timeout(5)
	void testShortestPath1 () {
		final GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
		final IMDBGraph graph;
		try {
			graph = new IMDBGraphImpl(IMDBGraphImpl.IMDB_DIRECTORY + "/testActors.tsv",
					IMDBGraphImpl.IMDB_DIRECTORY + "/testMovies.tsv");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertTrue(false);
			return;
		}
		final Node actor1 = graph.getActor("Kris");
		final Node actor2 = graph.getActor("Sandy");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		System.out.println(shortestPath);
		assertEquals(5, shortestPath.size());
		final String[] correctNames = { "Kris", "Blah2", "Sara", "Blah3", "Sandy" };
		int idx = 0;
		for (Node node : shortestPath) {
			assertEquals(correctNames[idx++], node.getName());
		}
	}

	/**
	 * Testing whether the search algorithm will recur or not when in a loop
	 */
	@Test
	@Timeout(5)
	void testRecurringPath () {
		final GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
		final IMDBGraph graph;
		try {
			graph = new IMDBGraphImpl(IMDBGraphImpl.IMDB_DIRECTORY + "/testRecurringActors.tsv",
					IMDBGraphImpl.IMDB_DIRECTORY + "/testRecurringMovies.tsv");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertTrue(false);
			return;
		}
		final Node actor1 = graph.getActor("Kris");
		final Node actor2 = graph.getActor("Dan");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		System.out.println(shortestPath);
		assertEquals(5, shortestPath.size());
		final String[] correctNames = { "Kris", "Blah1", "Sandy", "Blah3", "Dan" };
		int idx = 0;
		for (Node node : shortestPath) {
			assertEquals(correctNames[idx++], node.getName());
		}
	}

	@Test
	@Timeout(5)
	void testGiantSimpleNode () {
		final GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
		final IMDBGraph graph;

		int num = 1000;

		SimpleNode[] NodeList = new SimpleNode[num];

		for(int i = 0; i < num; i++){
			NodeList[i] = new SimpleNode(String.valueOf(i));
		}

		for(int i = 0; i < num - 1; i++){
			NodeList[i].addNeighbor(NodeList[i + 1]);
		}

		final List<Node> shortestPath = searchEngine.findShortestPath(NodeList[0], NodeList[num - 1]);
		System.out.println(shortestPath);
		assertEquals(num, shortestPath.size());
	}

	@Test
	@Timeout(5)
	void testSameName () {
		final GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
		final IMDBGraph graph;
		try {
			graph = new IMDBGraphImpl(IMDBGraphImpl.IMDB_DIRECTORY + "/testActors.tsv",
					IMDBGraphImpl.IMDB_DIRECTORY + "/testMovies.tsv");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertTrue(false);
			return;
		}
		final Node actor1 = graph.getActor("Kris");
		final Node actor2 = graph.getActor("Kris");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		System.out.println(shortestPath);
		assertEquals(1, shortestPath.size());
		final String[] correctNames = { "Kris" };
		int idx = 0;
		for (Node node : shortestPath) {
			assertEquals(correctNames[idx++], node.getName());
		}
	}

	@Test
	@Timeout(5)
	void testNoPath () {
		final GraphSearchEngine searchEngine = new GraphSearchEngineImpl();
		final IMDBGraph graph;
		try {
			graph = new IMDBGraphImpl(IMDBGraphImpl.IMDB_DIRECTORY + "/testActors.tsv",
					IMDBGraphImpl.IMDB_DIRECTORY + "/testMovies.tsv");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertTrue(false);
			return;
		}
		final Node actor1 = graph.getActor("Derik");
		final Node actor2 = graph.getActor("Kris");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		System.out.println(shortestPath);
		assertEquals(1, shortestPath.size());
		assertEquals(shortestPath.get(0), null);
	}
}
