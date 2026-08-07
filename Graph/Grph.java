import java.util.ArrayList;

public class Grph {
    private class Edge {
        private Vertex vertex1;
        private Vertex vertex2;
        private Integer weight;

        public Edge(Vertex vertex1, Vertex vertex2, Integer weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }

        public Vertex getStart() {
            return vertex1;
        }

        public Vertex getEnd() {
            return vertex2;
        }

        public Integer getWeight() {
            return weight;
        }
    }

    private class Vertex {
        private String data;
        private ArrayList<Edge> edges;

        public Vertex(String data) {
            this.data = data;
            this.edges = new ArrayList<Edge>();
        }

        public void addEdge(Vertex vertex2, Integer weight) {
            this.edges.add(new Edge(this, vertex2, weight));
        }

        public void removeEdge(Vertex vertex2, Integer weight) {
            this.edges.removeIf(ed -> ed.getEnd().equals(vertex2));
        }

        public String getData() {
            return this.data;
        }

        public ArrayList<Edge> getEdges() {
            return this.edges;
        }
    }

}