import java.util.List;

public class Main {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_ORANGE = "\u001B[38;5;208m";

    public static void main(String[] args) {

        FileManagement fm = new FileManagement();

        if (fm.isInputValidation()) {
            List<Edge> listOfEdges = fm.getListOfEdges();
            System.out.println();
            int allVertices = fm.getAllVertices();
            int startingNode = fm.getStartingNode();

            Graph graph = new Graph(listOfEdges, allVertices);

            if (graph.isDAG()) {
                System.out.println(ANSI_ORANGE + "* * * * * * * * * * The Graph is a DAG * * * * * * * * * *" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Shortest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.shortestAndLongestPathForDAG(graph, startingNode, allVertices, fm.getIndexWithName(), 0);
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Longest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.shortestAndLongestPathForDAG(graph, startingNode, allVertices, fm.getIndexWithName(), 1);
            } else {
                System.out.println(ANSI_ORANGE + "* * * * * * * * * The Graph is NOT a DAG * * * * * * * * *" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Shortest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.DijkstraAlgorithm(graph, startingNode, fm.getIndexWithName());
                System.out.println(ANSI_CYAN + "\n\t\t\t\t\t<!---Longest Path---!>" + ANSI_RESET);
                ShortestAndLongestPathCalculator.longestPathForNonDAG(graph, startingNode, fm.getIndexWithName());
            }
        } else
            System.out.println("The Inputs Does Not Meet the Requirements");
    }
}