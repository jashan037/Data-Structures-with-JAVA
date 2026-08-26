package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Deque;
import java.util.Collections;

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

        public void setWeight(Integer weight) {
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
        private HashSet<String> hs;

        public Vertex(String data) {
            this.data = data;
            this.edges = new ArrayList<Edge>();
            this.hs = new HashSet<>();
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public void addEdge(Vertex vertex2, Integer weight) {
            hs.add(vertex2.getData());
            this.edges.add(new Edge(this, vertex2, weight));
        }

        public void removeEdge(Vertex vertex2) {
            hs.remove(vertex2.getData());
            this.edges.removeIf(ed -> ed.getEnd().equals(vertex2));
        }

        public String getData() {
            return this.data;
        }

        public ArrayList<Edge> getEdges() {
            return this.edges;
        }

        public boolean containsEdge(Vertex vertex2) {
            return hs.contains(vertex2.getData());
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

        public void addEdge(Edge edge) {
            Vertex startVertex = edge.getStart();
            startVertex.addEdge(edge);
            if (!isDirected()) {
                Vertex endVertex = edge.getEnd();
                endVertex.addEdge(startVertex, (isWeighted() ? edge.getWeight() : null));
            }
        }

        public void addEdge(Vertex vertex1, Vertex vertex2) {
            vertex1.addEdge(vertex2, null);
            if (!isDirected)
                vertex2.addEdge(vertex1, null);
        }

        public void addEdge(Vertex vertex1, Vertex vertex2, Integer weight) {
            if (!isWeighted)
                weight = null;
            vertex1.addEdge(vertex2, weight);
            if (!isDirected)
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

        public boolean isDirected() {
            return isDirected;
        }

        public boolean isWeighted() {
            return isWeighted;
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

    // DSU
    public static class DSU {
        private HashMap<Vertex, Vertex> parent;
        private HashMap<Vertex, Integer> size;

        public DSU(Graph graph) {
            parent = new HashMap<>();
            size = new HashMap<>();
            for (Vertex vertex : graph.getVertices()) {
                parent.put(vertex, vertex);
                size.put(vertex, 1);
            }
        }

        public Vertex find(Vertex vertex) {
            if (parent.get(vertex) == vertex)
                return vertex;
            parent.put(vertex, find(parent.get(vertex)));
            return parent.get(vertex);
        }

        public void union(Vertex vertex1, Vertex vertex2) {
            Vertex p1 = find(vertex1);
            Vertex p2 = find(vertex2);
            if (p1 == p2)
                return;
            if (size.get(p1) < size.get(p2)) {
                size.put(p1, size.get(p1) + size.get(p2));
                parent.put(p1, p2);
                return;
            }
            size.put(p2, size.get(p1) + size.get(p2));
            parent.put(p2, p1);
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

    // Prim
    public static Graph prims(Graph graph, Vertex start) {
        HashSet<Vertex> visited = new HashSet<>();
        Graph MST = new Graph(true, false);
        PriorityQueue<Edge> que = new PriorityQueue<>((a, b) -> (a.getWeight() - b.getWeight()));
        for (Vertex vertex : graph.getVertices())
            MST.addVertex(vertex.getData());
        primsDFS(MST, start, visited, que);
        return MST;
    }

    private static void primsDFS(Graph MST, Vertex start, HashSet<Vertex> visited, PriorityQueue<Edge> que) {
        visited.add(start);
        for (Edge edge : start.getEdges())
            que.offer(edge);
        if (que.isEmpty())
            return;
        while (!que.isEmpty()) {
            Edge edge = que.poll();
            Vertex nb = edge.getEnd();
            if (visited.contains(nb))
                continue;

            Vertex st = MST.getVertex(start.getData());
            Vertex end = MST.getVertex(nb.getData());
            st.addEdge(end, edge.getWeight());
            primsDFS(MST, nb, visited, que);
            return;
        }
    }

    // Kruskal
    public static Graph Kruskal(Graph graph, Vertex start) {
        PriorityQueue<Edge> que = new PriorityQueue<>((a, b) -> (a.getWeight() - b.getWeight()));
        Graph MST = new Graph(true, false);
        for (Vertex vertex : graph.getVertices()) {
            MST.addVertex(vertex.getData());
            for (Edge edge : vertex.getEdges())
                que.offer(edge);
        }
        DSU dsu = new DSU(MST);
        while (!que.isEmpty()) {
            Edge edge = que.poll();
            Vertex st = MST.getVertex(edge.getStart().getData());
            Vertex end = MST.getVertex(edge.getEnd().getData());
            if (dsu.find(st) != dsu.find(end)) {
                st.addEdge(end, edge.getWeight());
                dsu.union(st, end);
            }
        }
        return MST;
    }

    // cycle Detection
    public static boolean cycleDetection(Graph graph) {
        HashSet<Vertex> visited = new HashSet<>();
        boolean res = false;
        for (Vertex vertex : graph.getVertices()) {
            if (!visited.contains(vertex) && graph.isDirected()) {
                res = res || directedCycleDetection(vertex, visited, new HashSet<Vertex>());
            } else if (!visited.contains(vertex) && !graph.isDirected()) {
                res = res || undirectedCycleDetection(null, vertex, visited);
            }
            if (res)
                return res;
        }
        return res;
    }

    private static boolean directedCycleDetection(Vertex start, HashSet<Vertex> visited, HashSet<Vertex> onPath) {
        visited.add(start);
        onPath.add(start);
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (onPath.contains(nb) && visited.contains(nb)) {
                return true;
            }
            if (!visited.contains(nb) && directedCycleDetection(nb, visited, onPath)) {
                return true;
            }
        }
        onPath.remove(start);
        return false;
    }

    private static boolean undirectedCycleDetection(Vertex parent, Vertex start, HashSet<Vertex> visited) {
        visited.add(start);
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (nb != parent && visited.contains(nb)) {
                return true;
            }
            if (!visited.contains(nb) && undirectedCycleDetection(start, nb, visited)) {
                return true;
            }
        }
        return false;
    }

    // bipartite
    public static boolean isBipartite(Graph graph) {
        HashMap<Vertex, Integer> color = new HashMap<>();
        boolean res = false;
        for (Vertex vertex : graph.getVertices()) {
            if (!color.containsKey(vertex))
                res = res || isBipartiteDFS(null, vertex, color);
            if (res)
                return res;
        }
        return res;
    }

    private static boolean isBipartiteDFS(Vertex parent, Vertex start, HashMap<Vertex, Integer> color) {
        if (color.containsKey(parent))
            color.put(start, 1 - color.get(parent));
        else
            color.put(start, 0);
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (color.containsKey(nb) && ((color.get(nb) == color.get(start))))
                return false;
            if (!color.containsKey(nb) && !isBipartiteDFS(start, nb, color)) {
                return false;
            }
        }
        return true;
    }

    // Kosaraju
    public static Graph reverseGraph(Graph graph) {
        Graph revGraph = new Graph(graph.isWeighted(), graph.isDirected());
        for (Vertex vertex : graph.getVertices())
            revGraph.addVertex(vertex.getData());
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                Vertex st = revGraph.getVertex(edge.getStart().getData());
                Vertex end = revGraph.getVertex(edge.getEnd().getData());
                end.addEdge(st, edge.getWeight());
            }
        }
        return revGraph;
    }

    public static ArrayList<ArrayList<Vertex>> Kosaraju(Graph graph) {
        ArrayList<ArrayList<Vertex>> SCC = new ArrayList<>();
        Deque<Vertex> stack = new ArrayDeque<>();
        HashSet<Vertex> visited = new HashSet<>();
        for (Vertex vertex : graph.getVertices()) {
            if (!visited.contains(vertex))
                stackKosarajuDFS(vertex, stack, visited);
        }
        Graph revGraph = reverseGraph(graph);
        visited.clear();
        while (!stack.isEmpty()) {
            ArrayList<Vertex> CC = new ArrayList<>();
            Vertex vertex = revGraph.getVertex((stack.pop()).getData());
            if (!visited.contains(vertex)) {
                kosarajuDFS(vertex, visited, CC);
                SCC.add(CC);
            }
        }
        return SCC;
    }

    private static void stackKosarajuDFS(Vertex start, Deque<Vertex> stack, HashSet<Vertex> visited) {
        visited.add(start);
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (!visited.contains(nb)) {
                stackKosarajuDFS(nb, stack, visited);
            }
        }
        stack.push(start);
    }

    private static void kosarajuDFS(Vertex start, HashSet<Vertex> visited, ArrayList<Vertex> CC) {
        CC.add(start);
        visited.add(start);
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (!visited.contains(nb)) {
                kosarajuDFS(nb, visited, CC);
            }
        }
    }

    // tarjan
    private static int tarjanItr = 0;

    public static ArrayList<ArrayList<Vertex>> revtarjan(Graph graph) {
        ArrayList<ArrayList<Vertex>> SCC = new ArrayList<>();
        HashMap<Vertex, Integer> disc = new HashMap<>();
        HashMap<Vertex, Integer> low = new HashMap<>();
        HashSet<Vertex> onStack = new HashSet<>();
        Deque<Vertex> stack = new ArrayDeque<>();
        for (Vertex vertex : graph.getVertices()) {
            if (!disc.containsKey(vertex))
                tarjanDFS(vertex, disc, low, onStack, stack, SCC);
        }
        tarjanItr = 0;
        return SCC;
    }

    private static void tarjanDFS(Vertex start, HashMap<Vertex, Integer> disc, HashMap<Vertex, Integer> low,
            HashSet<Vertex> onStack, Deque<Vertex> stack, ArrayList<ArrayList<Vertex>> SCC) {
        disc.put(start, tarjanItr);
        low.put(start, tarjanItr);
        stack.push(start);
        onStack.add(start);
        tarjanItr++;
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (!disc.containsKey(nb)) {
                tarjanDFS(nb, disc, low, onStack, stack, SCC);
                if (onStack.contains(nb))
                    low.put(start, Math.min(low.get(nb), low.get(start)));
            } else if (onStack.contains(nb))
                low.put(start, Math.min(disc.get(nb), low.get(start)));
        }
        if (disc.get(start).equals(low.get(start))) {
            ArrayList<Vertex> CC = new ArrayList<>();
            while (!stack.isEmpty() && onStack.contains(start)) {
                Vertex rem = stack.poll();
                CC.add(rem);
                onStack.remove(rem);
            }
            SCC.add(CC);
        }
    }

    // topological Sort
    public static ArrayList<Vertex> topologicalSort(Graph graph) {
        HashSet<Vertex> visited = new HashSet<>();
        ArrayList<Vertex> sol = new ArrayList<>();
        for (Vertex vertex : graph.getVertices()) {
            if (visited.contains(vertex))
                continue;
            topologicalDFS(vertex, visited, sol);
        }
        Collections.reverse(sol);
        return sol;

    }

    private static void topologicalDFS(Vertex start, HashSet<Vertex> visited, ArrayList<Vertex> sol) {
        visited.add(start);
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (!visited.contains(nb)) {
                topologicalDFS(nb, visited, sol);
            }
        }
        sol.add(start);
    }

    // Bridges
    public static ArrayList<Edge> findBridges(Graph graph) {
        ArrayList<Edge> bridges = new ArrayList<>();
        HashMap<Vertex, Integer> disc = new HashMap<>();
        HashMap<Vertex, Integer> low = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) {
            if (!disc.containsKey(vertex))
                findBridgesDFS(null, vertex, disc, low, bridges);
        }
        tarjanItr = 0;
        return bridges;
    }

    private static void findBridgesDFS(Vertex parent, Vertex start, HashMap<Vertex, Integer> disc,
            HashMap<Vertex, Integer> low, ArrayList<Edge> bridges) {
        disc.put(start, tarjanItr);
        low.put(start, tarjanItr);
        tarjanItr++;
        for (Edge edge : start.getEdges()) {
            Vertex nb = edge.getEnd();
            if (!disc.containsKey(nb)) {
                findBridgesDFS(start, nb, disc, low, bridges);
                low.put(start, Math.min(low.get(start), low.get(nb)));
                if (disc.get(start) < low.get(nb)) {
                    bridges.add(edge);
                }
            } else if (nb != parent)
                low.put(start, Math.min(low.get(start), disc.get(nb)));
        }
    }

    // Articulation points
    // public static ArrayList<Vertex> findArticulation(Graph graph) {
    // HashMap<Vertex, Integer> disc = new HashMap<>();
    // HashMap<Vertex, Integer> low = new HashMap<>();
    // ArrayList<Vertex> sol = new ArrayList<>();
    // for (Vertex vertex : graph.getVertices()) {
    // if (disc.containsKey(vertex))
    // continue;
    // ArticulationDFS(null, vertex, disc, low, sol);
    // }
    // bridgesItr = 0;
    // return sol;
    // }

    // private static void ArticulationDFS(Vertex parent, Vertex start,
    // HashMap<Vertex, Integer> disc,
    // HashMap<Vertex, Integer> low, ArrayList<Vertex> sol) {
    // disc.put(start, bridgesItr);
    // low.put(start, bridgesItr);
    // bridgesItr++;
    // int childCount = 0;
    // for (Edge edge : start.getEdges()) {
    // Vertex nb = edge.getEnd();
    // if (!disc.containsKey(nb)) {
    // childCount++;
    // ArticulationDFS(start, nb, disc, low, sol);
    // low.put(start, Math.min(low.get(start), low.get(nb)));
    // if ((disc.get(start) <= low.get(nb)) && parent != null) {
    // sol.add(edge.getStart());
    // }
    // } else if (disc.containsKey(nb) && nb != parent) {
    // low.put(start, Math.min(low.get(start), disc.get(nb)));
    // }
    // }
    // if (parent == null && childCount > 1) {
    // sol.add(start);
    // }
    // }

    // Create a new clone Graph with added reverse Edges.
    // public static Graph revEdgesGraph(Graph graph, HashMap<Edge, Edge> revMap) {
    // Graph res = new Graph(graph.isWeighted(), graph.isDirected());
    // for (Vertex vertex : graph.getVertices())
    // res.addVertex(vertex.getData());
    // for (Vertex vertex : graph.getVertices()) {
    // for (Edge edge : vertex.getEdges()) {
    // Vertex startVertex = edge.getStart();
    // Vertex endVertex = edge.getEnd();
    // Edge rev = new Edge(endVertex, startVertex, 0);
    // graph.addEdge(rev);
    // revMap.put(edge, rev);
    // }
    // }
    // return res;
    // }

    // Find augmenting paths in a flow network
    // private static class flowEdge {
    // public int flowing;
    // public int cap;

    // public flowEdge(int flowing, int cap) {
    // this.flowing = flowing;
    // this.cap = cap;
    // }
    // }

    // public static int maxFlow(Graph graph, Vertex s, Vertex t) {
    // HashMap<Edge, flowEdge> flowMap = new HashMap<>();
    // HashMap<Edge, Edge> revMap = new HashMap<>();
    // int flow = Integer.MAX_VALUE;
    // Graph rev = revEdgesGraph(graph, revMap);
    // s = rev.getVertex(s.getData());
    // t = rev.getVertex(t.getData());
    // for (Vertex vertex : rev.getVertices()) {
    // for (Edge edge : vertex.getEdges())
    // flowMap.put(edge, new flowEdge(0, edge.getWeight()));
    // }
    // return maxFlowDFS(s, t, flow, flowMap);
    // }

    // private static int maxFlowDFS(Vertex s, Vertex t, int flow, HashMap<Edge,
    // flowEdge> map) {
    // if (s == t)
    // return flow;
    // int gotThrough = 0;
    // for (Edge edge : s.getEdges()) {
    // Vertex nb = edge.getEnd();
    // if (!map.containsKey(edge) && edge.getWeight() > 0) {
    // int newFlow = Math.min(flow, edge.getWeight());
    // flow = maxFlowDFS(nb, t, newFlow, map);
    // gotThrough += flow;
    // flowEdge obj = new flowEdge(flow, edge.getWeight() - flow);
    // map.put(edge, obj);
    // } else if (map.containsKey(edge)) {
    // int newFlow = Math.min(flow, map.get(edge).leftFlow);
    // flow = maxFlowDFS(nb, t, newFlow, map);
    // int nflow = flow + map.get(edge).flowing;
    // gotThrough += flow;
    // flowEdge obj = new flowEdge(nflow, map.get(edge).leftFlow - flow);
    // flow = nflow;
    // map.put(edge, obj);
    // }
    // }
    // return gotThrough;
    // }

    // private static int maxFlowDFS(Vertex s, Vertex t, int flow, HashMap<Edge,
    // flowEdge> map) {
    // if (s == t)
    // return flow;
    // for (Edge edge : s.getEdges()) {
    // flowEdge fledge = map.get(edge);
    // Vertex nb = edge.getEnd();
    // if (fledge.cap > 0) {
    // flow = Math.min(flow, fledge.cap);
    // flow = maxFlowDFS(nb, t, flow, map);
    // fledge.flowing = flow;
    // fledge.cap = fledge.cap - flow;

    // }
    // }
    // }

    // Revision
}