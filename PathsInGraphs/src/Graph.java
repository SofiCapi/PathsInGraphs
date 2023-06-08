import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Graph {
    private List<List<Edge>> adjacencyList;
    private int allVertices;

    public Graph(List<Edge> adjacencyList, int allVertices) {
        setAllVertices(allVertices);
        setAdjacencyList(IntStream.range(0, getAllVertices())
                .mapToObj(i -> new ArrayList<Edge>())
                .collect(Collectors.toCollection(() -> new ArrayList<>(getAllVertices()))));

        Objects.requireNonNull(adjacencyList).forEach(edge -> getAdjacencyList().get(edge.getSource()).add(edge));
    }

    public boolean isDAG() {
        boolean[] visited = new boolean[adjacencyList.size()];
        boolean[] recursionStack = new boolean[adjacencyList.size()];

        for (int i = 0; i < adjacencyList.size(); i++) {
            if (!visited[i]) {
                if (isCyclic(i, visited, recursionStack)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isCyclic(int node, boolean[] visited, boolean[] recursionStack) {
        visited[node] = true;
        recursionStack[node] = true;

        for (Edge edge : adjacencyList.get(node)) {
            if (!visited[edge.getDestination()]) {
                if (isCyclic(edge.getDestination(), visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack[edge.getDestination()]) {
                return true;
            }
        }

        recursionStack[node] = false;
        return false;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(List<List<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public int getAllVertices() {
        return allVertices;
    }

    public void setAllVertices(int allVertices) {
        this.allVertices = allVertices;
    }

}
