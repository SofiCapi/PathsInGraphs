import java.util.*;
import java.util.stream.IntStream;

public class ShortestAndLongestPathCalculator {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void DijkstraAlgorithm(Graph graph, int source, HashMap<Integer, String> indexNames) {
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getDistance));

        List<Double> distance = new ArrayList<>(Collections.nCopies(graph.getAllVertices(), Double.POSITIVE_INFINITY));
        int[] previous = new int[graph.getAllVertices()];

        distance.set(source, 0.0);
        previous[source] = -1;

        priorityQueue.offer(new Vertex(source, 0.0));

        while (!priorityQueue.isEmpty()) {
            Vertex currentVertex = priorityQueue.poll();

            int vertexIndexOfCurrentVertex = currentVertex.getVertexIndex();

            if (currentVertex.getDistance() > distance.get(vertexIndexOfCurrentVertex)) continue;

            for (Edge edge : graph.getAdjacencyList().get(vertexIndexOfCurrentVertex)) {
                int destinationVertex = edge.getDestination();
                double weight = edge.getWeight();
                double newDistance = distance.get(vertexIndexOfCurrentVertex) + weight;

                if (newDistance < distance.get(destinationVertex)) {
                    distance.set(destinationVertex, newDistance);
                    previous[destinationVertex] = vertexIndexOfCurrentVertex;
                    priorityQueue.offer(new Vertex(destinationVertex, newDistance));
                }
            }
        }

        printInformation(distance, source, previous, graph.getAllVertices(), indexNames);
    }

    public static void longestPathForNonDAG(Graph graph, int source, HashMap<Integer, String> indexNames){
        List<Double> distance = new ArrayList<>(Collections.nCopies(graph.getAllVertices(), Double.NEGATIVE_INFINITY));
        distance.set(source, 0.0);

        int[] previous = new int[graph.getAllVertices()];
        previous[source] = -1;

        boolean[] visited = new boolean[graph.getAllVertices()];

        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getDistance).reversed());

        queue.add(new Vertex(source, 0.0));

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();

            if (visited[u.getVertexIndex()]) {
                continue;
            }

            visited[u.getVertexIndex()] = true;

            graph.getAdjacencyList().get(u.getVertexIndex()).stream()
                    .filter(edge -> !visited[edge.getDestination()])
                    .forEach(edge -> {
                        if (distance.get(u.getVertexIndex()) + edge.getWeight() > distance.get(edge.getDestination())) {
                            distance.set(edge.getDestination(), distance.get(u.getVertexIndex()) + edge.getWeight());
                            previous[edge.getDestination()] = u.getVertexIndex();
                            queue.add(new Vertex (edge.getDestination(), distance.get(u.getVertexIndex()) + edge.getWeight()));
                        }
                    });
        }

        printInformation(distance, source, previous, graph.getAllVertices(), indexNames);
    }

    static void shortestAndLongestPathForDAG(Graph graph, int source, int allVertices, HashMap<Integer, String> indexNames, int shortestOrLongest) {
        int[] previous = new int[allVertices];
        previous[source] = -1;

        Stack<Integer> ordering = new Stack<>();
        boolean[] visited = new boolean[allVertices];

        IntStream.range(0, allVertices)
                .filter(i -> !visited[i])
                .forEach(i -> topologicalSorting(graph, i, visited, ordering));

        if(shortestOrLongest == 0){
            shortestPathForDAG(graph, source, indexNames, ordering, previous);
        }
        else if (shortestOrLongest == 1) {
            longestPathForDAG(graph, source, indexNames, ordering, previous);
        }
    }

    public static void shortestPathForDAG(Graph graph, int source, HashMap<Integer, String> indexNames, Stack<Integer> ordering, int[] previous){
        List<Double> distance = new ArrayList<>(Collections.nCopies(graph.getAllVertices(), Double.POSITIVE_INFINITY));
        distance.set(source, 0.0);

        while (!ordering.isEmpty()) {
            int currentVertex = ordering.pop();

            if (distance.get(currentVertex) != Double.POSITIVE_INFINITY) {
                graph.getAdjacencyList().get(currentVertex).forEach(edge -> {
                    if ((distance.get(currentVertex) + edge.getWeight()) < distance.get(edge.getDestination())) {
                        distance.set(edge.getDestination(), distance.get(currentVertex) + edge.getWeight());
                        previous[edge.getDestination()] = currentVertex;
                    }
                });
            }
        }

        printInformation(distance, source, previous, graph.getAllVertices(), indexNames);
    }

    public static void longestPathForDAG(Graph graph, int source, HashMap<Integer, String> indexNames, Stack<Integer> ordering, int[] previous){
        List<Double> distance = new ArrayList<>(Collections.nCopies(graph.getAllVertices(), Double.NEGATIVE_INFINITY));
        distance.set(source, 0.0);

        while (!ordering.isEmpty()) {
            int currentVertex = ordering.pop();

            if (distance.get(currentVertex) != Double.NEGATIVE_INFINITY) {
                graph.getAdjacencyList().get(currentVertex)
                        .forEach(edge -> {
                            int destination = edge.getDestination();
                            if (distance.get(currentVertex) + edge.getWeight() > distance.get(destination)) {
                                distance.set(destination, distance.get(currentVertex) + edge.getWeight());
                                previous[destination] = currentVertex;
                            }
                        });
            }
        }
        printInformation(distance, source, previous, graph.getAllVertices(), indexNames);
    }

    private static void topologicalSorting(Graph graph, int vertexIndex, boolean[] visited, Stack<Integer> ordering) {
        visited[vertexIndex] = true;

        graph.getAdjacencyList().get(vertexIndex).stream()
                .filter(edge -> !visited[edge.getDestination()])
                .forEach(edge -> topologicalSorting(graph, edge.getDestination(), visited, ordering));

        ordering.push(vertexIndex);
    }

    private static void printInformation(List<Double> distance, int source, int[] previous, int allVertices, HashMap<Integer, String> indexNames){
        if (indexNames == null)
            printInformationWithoutName(distance, source, previous, allVertices);
        else
            printInformationWithName(distance, source, previous, allVertices, indexNames);
    }

    private static void printInformationWithoutName(List<Double> distance, int source, int[] previous, int allVertices) {
        System.out.format(ANSI_GREEN + "%15s%15s%15s%15s", "Source", "Destination", "Cost", "Route\n" + ANSI_RESET);

        IntStream.range(0, allVertices)
                .forEach(i -> {
                    System.out.format("%15s%15s%15s", source, i, distance.get(i));
                    System.out.print("\t\t");
                    if (distance.get(i) == Double.POSITIVE_INFINITY || distance.get(i) == Double.NEGATIVE_INFINITY)
                        System.out.println("-");
                    else {
                        printRouteWithoutNames(previous, i);
                        System.out.println();
                    }
                });
    }

    private static void printInformationWithName(List<Double> distance, int source, int[] previous, int allVertices, HashMap<Integer, String> indexNames) {
        System.out.format(ANSI_GREEN + "%15s%15s%15s%17s", "Source", "Destination", "Cost", "Route\n" + ANSI_RESET);

        IntStream.range(0, allVertices)
                .forEach(i -> {
                    System.out.format("%15s%15s%15s", indexNames.get(source), indexNames.get(i), distance.get(i));
                    System.out.print("\t\t");
                    if (distance.get(i) == Double.POSITIVE_INFINITY || distance.get(i) == Double.NEGATIVE_INFINITY)
                        System.out.println("-");
                    else {
                        printRouteWithNames(previous, i, indexNames);
                        System.out.println();
                    }
                });
    }

    private static void printRouteWithoutNames(int[] previous, int vertexIndex) {
        if (vertexIndex >= 0) {
            printRouteWithoutNames(previous, previous[vertexIndex]);
            System.out.print(vertexIndex + " ");
        }
    }

    private static void printRouteWithNames(int[] previous, int vertexIndex, HashMap<Integer, String> indexNames) {
        if (vertexIndex >= 0) {
            printRouteWithNames(previous, previous[vertexIndex], indexNames);
            System.out.print(indexNames.get(vertexIndex) + " ");
        }
    }
}
