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

}