package graph;

/**
 * Perform an operation on each Vertex in a graph. The operation to perform is
 * defined by extending this class and implementing the operation method.
 *
 * @author Albin Hjalmas.
 * @param <V> the type of the vertices contained within the graph.
 */
public abstract class DFO<V> {

    private ST<V, Boolean> marked; // Symbol table containing the marked state of the vertices.
    private Graph<V> graph;

    public DFO(Graph<V> g) {
        graph = g;
    }

    /**
     * Run the operation on all vertices in the graph.
     */
    public void run() {
        // Create marked table.
        marked = new ST<>();
        for (V v : graph.getVertices()) {
            marked.add(v, Boolean.FALSE);
        }

        // Start the depth first search.
        dfs(graph, graph.getVertices().get(0));
    }

    /**
     * Perform depth first search on the graph.
     *
     * @param g
     * @param vertex
     * @param a
     */
    private void dfs(Graph<V> g, V vertex) {
        // Mark current vertex.
        marked.remove(vertex);
        marked.add(vertex, Boolean.TRUE);
        // Execute the operation on the vertex.
        operation(vertex);
        for (V v : g.getAdj(vertex)) {
            // Check if current vertex has been visited,
            // if not: visit it, perform operation and mark it.
            if (marked.get(v) == false) {
                dfs(g, v);
            }
        }
    }

    /**
     * The operation to perform on each vertex.
     *
     * @param vertex the vertex to perform an operation on.
     */
    public abstract void operation(V vertex);
}
