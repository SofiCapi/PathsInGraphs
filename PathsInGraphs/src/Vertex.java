public class Vertex implements Comparable<Vertex> {
    private int vertexIndex;
    private double distance;

    public Vertex(int vertexIndex, double distance) {
        this.vertexIndex = vertexIndex;
        this.distance = distance;
    }

    public int getVertexIndex() {
        return vertexIndex;
    }

    public void setVertexIndex(int vertexIndex) {
        this.vertexIndex = vertexIndex;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int compareTo(Vertex other) {
        return Double.compare(this.distance, other.distance);
    }
}
