import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.HashMap;

/**
 * Implements the GraphSearchEngine interface.
 */
public class GraphSearchEngineImpl implements GraphSearchEngine {
	public GraphSearchEngineImpl () {
	}

	private HashMap<Node, Node> _previous = new HashMap<>();
	private ArrayList<Node> _queue = new ArrayList<>();
	private HashSet<Node> _visited = new HashSet<>();


	public List<Node> findShortestPath (Node s, Node t) {
		Node currentNode = s;
		_visited.add(s);
		while(currentNode != null && !currentNode.equals(t)){
			addNeighbors(currentNode);
			currentNode = _queue.get(0);
			_queue.remove(0);
		}

		return backTrack(currentNode, s, t);
	}

	private void addNeighbors (Node node) {
		if(node.getNeighbors().equals(null)){
			System.out.println("no neighbors!");
			return;
		}
		for(Node n : node.getNeighbors()){
			if(!_visited.contains(n)){
				_queue.add(n);
				_previous.put(n, node);
				_visited.add(n);
			}
		}
	}

	private List<Node> backTrack (Node n, Node s, Node t){
		Node currentNode = _previous.get(n);
		ArrayList<Node> path = new ArrayList<>();

		if(s == null){
			path.add(null);
			return path;
		}

		path.add(0, t);
		while(_previous.containsKey(currentNode)){
			path.add(0, currentNode);
			currentNode = _previous.get(currentNode);
		}


		if(!s.equals(t)){
			path.add(0, s);
		}


		for(Node i : path){
			System.out.println(i.getName());
		}

		return path;
	}

}
