package graph;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A undirected graph.
 *
 * @author Albin Hjalmas.
 * @param <V> The type of the vertices in this graph.
 */
public class Graph<V> implements Serializable {

    private ST<V, ArrayList<V>> adj; // Adjacency lists.
    private int edges; // Number of edges

    /**
     * Constructor.
     */
    public Graph() {
        // Create symbol table for holding vertices and 
        // accompanying adjacency lists.
        adj = new ST<>();
        edges = 0;
    }

    /**
     * Constructor.
     *
     * @param vertices argument-list with the default vertices.
     */
    public Graph(V... vertices) {
        // Create symbol table for holding vertices and 
        // accompanying adjacency lists.
        adj = new ST<>();

        // Add vertices and create adjacency lists.
        for (V vertex : vertices) {
            adj.add(vertex, new ArrayList<>());
        }

        edges = 0;
    }

    /**
     * Adds a v to this graph.
     *
     * @param v the v to add to this graph.
     */
    public void addV(V v) {
        adj.add(v, new ArrayList<>());
    }

    /**
     * Removes the specified v from this graph.
     *
     * @param v the v to remove.
     * @return true if successful else false.
     */
    public boolean removeV(V v) {
        // Remove all edges connected to v
        ArrayList<V> a = adj.get(v);
        if (a == null) {
            return false;
        }

        for (V v2 : a) {
            ArrayList<V> b = adj.get(v2);
            b.remove(v);
            edges--;
        }

        // Finally remove v.
        adj.remove(v);
        return true;
    }

    /**
     * Adds an edge between v1 and v2.
     *
     * @param v1 first vertex.
     * @param v2 second vertex.
     * @return true if this graph contains both v1 and v2 else false.
     * @precondition this.containsV(v1) && this.containsV(v2)
     */
    public boolean addE(V v1, V v2) {
        // First check if this graph
        // contains an edge between v1 and v2.
        // We do not allow parallell edges.
        if (containsE(v1, v2) || !(containsV(v1) && containsV(v2))) {
            return false;
        }

        // Add to adjacency lists.
        adj.get(v1).add(v2);
        adj.get(v2).add(v1);
        edges++;
        return true;
    }

    /**
     * Removes edge between v1 and v2.
     *
     * @param v1 first vertex.
     * @param v2 second vertex.
     * @return true if successful else false.
     * @precondition this.containsE(v1, v2)
     */
    public boolean removeE(V v1, V v2) {
        // First check if this graph even 
        // contains an edge between v1 and v2.
        if (!containsE(v1, v2)) {
            return false;
        }

        // Remove from eachothers adjacency lists.
        adj.get(v1).remove(v2);
        adj.get(v2).remove(v1);
        edges--;
        return true;
    }

    /**
     * Checks if this graph contains vertex v.
     *
     * @param v the vertex to look for.
     * @return true if v is contained within this graph else false.
     */
    public boolean containsV(V v) {
        for (V vertex : adj.getKeys()) {
            if (vertex.equals(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this graph contains edge between v1 and v2.
     *
     * @param v1 first vertex.
     * @param v2 second vertex.
     * @return true if the graph contains the specified edge else false.
     */
    public boolean containsE(V v1, V v2) {
        // First check if it contains both vertices
        if (!(containsV(v1) && containsV(v2))) {
            return false;
        }

        // Now check if it contains an edge between v1 and v2
        // Do this by iterating over v1's adjacency list. If v2 is in 
        // v1's adjacency list it means that there is an edge between
        // v1 and v2.
        for (V v : adj.get(v1)) {
            if (v.equals(v2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the number of vertices contained in this graph.
     *
     * @return number of vertices.
     */
    public int getV() {
        return adj.size();
    }

    /**
     * Gets the number of edges contained in this graph.
     *
     * @return number of edges.
     */
    public int getE() {
        return edges;
    }

    /**
     * Get a list of vertices adjacent to v.
     *
     * @param v the vertex.
     * @return a list of vertices adjacent to v.
     */
    public ArrayList<V> getAdj(V v) {
        return adj.get(v);
    }
    
    /**
     * Return all vertices belonging to this graph.
     * @return a list of all vertices.
     */
    public ArrayList<V> getVertices() {
        return adj.getKeys();
    }
}
