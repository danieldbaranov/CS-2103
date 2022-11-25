import java.util.Collection;
import java.util.LinkedList;

 class SimpleNode implements Node {
    private final String _name;
    private final Collection<Node> _neighbors;

    public SimpleNode (String name) {
        _name = name;

        // Note: could also use an ArrayList, but LinkedList is likely slightly
        // more efficient in this program.
        _neighbors = new LinkedList<Node>();
    }

    public String getName () {
        return _name;
    }

    public Collection<Node> getNeighbors () {
        return _neighbors;
    }

    public void addNeighbor(Node movie){
        _neighbors.add(movie);
    }
}