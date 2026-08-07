import java.util.ArrayList;
import java.util.HashMap;

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

        public void removeEdge(Vertex vertex2) {
            this.edges.removeIf(ed -> ed.getEnd().equals(vertex2));
        }

        public String getData() {
            return this.data;
        }

        public ArrayList<Edge> getEdges() {
            return this.edges;
        }
    }

    public class Graph {
        private HashMap<String, Vertex> hm;
        private boolean isWeighted;
        private boolean isDirected;

        // constructors
        public Graph(boolean isWeighted) {
            this.hm = new HashMap<>();
            this.isWeighted = isWeighted;
            this.isDirected = false;
        }

        public Graph(boolean isWeighted, boolean isDirected) {
            this.hm = new HashMap<>();
            this.isWeighted = isWeighted;
            this.isDirected = isDirected;
        }

        // Modifications
        public Vertex addVertex(String data) {
            Vertex vertex = new Vertex(data);
            hm.put(data, vertex);
            return vertex;
        }

        public void removeVertex(String data) {
            if (hm.containsKey(data))
                hm.remove(data);
        }

        public void addEdge(Vertex vertex1, Vertex vertex2, Integer weight) {
            if (!isWeighted)
                weight = null;
            vertex1.addEdge(vertex2, weight);
            if (isDirected)
                vertex2.addEdge(vertex1, weight);
        }

        public void removeEdge(Vertex vertex1, Vertex vertex2) {
            vertex1.removeEdge(vertex2);
            if (isDirected)
                vertex2.removeEdge(vertex1);
        }

        // getters
        public ArrayList<Vertex> getVerteces() {
            return new ArrayList<Vertex>(hm.values());
        }

        public Vertex getVertex(String data) {
            return hm.containsKey(data) ? hm.get(data) : null;
        }

    }

}