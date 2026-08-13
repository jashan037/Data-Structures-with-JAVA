package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Map;

public class Grph {
    public static class Edge {
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

    public static class Vertex {
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

    public static class Graph {
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
        public ArrayList<Vertex> getVertices() {
            return new ArrayList<Vertex>(hm.values());
        }

        public Vertex getVertex(String data) {
            return hm.containsKey(data) ? hm.get(data) : null;
        }

        // traversels
        // DFS
        public void DFS(Vertex start) {
            internalDFS(start, new HashSet<Vertex>());
        }

        private void internalDFS(Vertex start, HashSet<Vertex> visited) {
            System.out.print(start.getData() + " ");
            visited.add(start);
            for (Edge e : start.getEdges()) {
                Vertex neighbour = e.getEnd();
                if (!visited.contains(neighbour)) {
                    internalDFS(neighbour, visited);
                }
            }
        }

        // BFS
        public void BFS(Vertex start) {
            internalBFS(start, new HashSet<Vertex>());
        }

        private void internalBFS(Vertex start, HashSet<Vertex> visited) {
            Deque<Vertex> q = new ArrayDeque<>();
            visited.add(start);
            q.addLast(start);
            while (!q.isEmpty()) {
                Vertex vertex = q.removeFirst();
                System.out.print(vertex.getData() + " ");
                for (Edge e : vertex.getEdges()) {
                    Vertex neighbour = e.getEnd();
                    if (!visited.contains(neighbour)) {
                        visited.add(neighbour);
                        q.addLast(neighbour);
                    }
                }
            }
        }
    }
    // Dijkstra

    public static class MapPair {
        public Map<String, Integer> dist;
        public Map<String, Vertex> prev;

        public MapPair(Map<String, Integer> dist, Map<String, Vertex> prev) {
            this.dist = dist;
            this.prev = prev;
        }
    }

    public static MapPair Dijkstra(Graph g, Vertex startingVertex) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, Vertex> previous = new HashMap<>();
        PriorityQueue<QueueObject> queue = new PriorityQueue<>();

        distances.put(startingVertex.getData(), 0);
        for (Vertex vertex : g.getVertices()) {
            if (vertex != startingVertex) {
                distances.put(vertex.getData(), Integer.MAX_VALUE);
            }
            previous.put(vertex.getData(), null);
        }

        queue.offer(new QueueObject(startingVertex, 0));
        while (!queue.isEmpty()) {
            Vertex current = queue.poll().vertex;
            for (Edge e : current.getEdges()) {
                Integer alternate = e.getWeight() + distances.get(current.getData());
                String neighbour = e.getEnd().getData();
                if (alternate < distances.get(neighbour)) {
                    distances.put(neighbour, alternate);
                    previous.put(neighbour, current);
                }
                queue.offer(new QueueObject(e.getEnd(), distances.get(neighbour)));
            }
        }
        return new MapPair(distances, previous);
    }

    static class QueueObject implements Comparable<QueueObject> {
        public Vertex vertex;
        public int priority;

        public QueueObject(Vertex v, int p) {
            this.vertex = v;
            this.priority = p;
        }

        public int compareTo(QueueObject o) {
            if (this.priority == o.priority)
                return 0;
            else if (this.priority < o.priority)
                return -1;
            else
                return 1;
        }
    }

    // ShortestPathBetween
    public static class Path {
        public int distance;
        public ArrayList<String> path;

        public Path(int distance, ArrayList<String> path) {
            this.distance = distance;
            this.path = path;
        }
    }

    public static Path ShortestPathBetween(Graph g, Vertex vertex1, Vertex vertex2) {
        MapPair res = Dijkstra(g, vertex1);
        Map<String, Integer> ds = res.dist;
        Map<String, Vertex> prev = res.prev;
        int distance = ds.get(vertex2.getData()) == null ? -1 : ds.get(vertex2.getData());
        ArrayList<String> path = new ArrayList<>();
        Vertex curr = vertex2;
        while (curr != null) {
            path.add(curr.getData());
            curr = prev.get(curr.getData());
        }
        for (int i = 0, j = path.size() - 1; i < j; i++, j--) {
            String temp = path.get(i);
            path.set(i, path.get(j));
            path.set(j, temp);
        }
        return new Path(distance, path);
    }

    // Prism

    private static class QueueObjectEdge implements Comparable<QueueObjectEdge> {
        private Edge edge;

        public QueueObjectEdge(Edge edge) {
            this.edge = edge;
        }

        public int compareTo(QueueObjectEdge o) {
            if (edge.getWeight() == o.edge.getWeight()) {
                return 0;
            } else if (edge.getWeight() > o.edge.getWeight()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static Graph Prism(Graph graph, Vertex vertex1) {
        HashSet<Vertex> visited = new HashSet<>();
        PriorityQueue<QueueObjectEdge> que = new PriorityQueue<>();
        Graph MST = new Graph(true, false);
        for (Vertex v : graph.getVertices())
            MST.addVertex(v.getData());
        visited.add(vertex1);
        internalPrism(vertex1, MST, que, visited);
        return MST;
    }

    private static void internalPrism(Vertex start, Graph MST, PriorityQueue<QueueObjectEdge> que,
            HashSet<Vertex> visited) {
        for (Edge edge : start.getEdges()) {
            que.offer(new QueueObjectEdge(edge));
        }
        if (que.isEmpty())
            return;
        Edge edge = que.poll().edge;
        while (visited.contains(edge.getEnd()))
            edge = que.poll().edge;
        Vertex stVer = MST.getVertex(edge.getStart().getData());
        Vertex endVer = MST.getVertex(edge.getEnd().getData());
        if (!visited.contains(edge.getEnd())) {
            stVer.addEdge(endVer, edge.weight);
            visited.add(edge.getEnd());
            internalPrism(edge.getEnd(), MST, que, visited);
        }
    }
}