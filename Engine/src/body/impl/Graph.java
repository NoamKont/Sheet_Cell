package body.impl;

import body.Cell;
import body.Coordinate;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {
    private Map<Coordinate, List<Coordinate>> graph;  // Original graph
    private Map<Coordinate, List<Coordinate>> graph_T;  // Transpose of the graph

    public Graph() {
        this.graph = new HashMap<>();
        this.graph_T = new HashMap<>();
    }

    public Graph(Map<Coordinate, List<Coordinate>> graph, Map<Coordinate, List<Coordinate>> graph_T) {
        this.graph = graph;
        this.graph_T = graph_T;
    }

    public Map<Coordinate,List<Coordinate>> getGraph(){
        return this.graph;
    }

    public Map<Coordinate,List<Coordinate>> getGraph_T(){
        return this.graph_T;
    }

    //COPY C-TOR
    public Graph(Graph other) {
        // Deep copy of the original graph
        this.graph = new HashMap<>();
        for (Map.Entry<Coordinate, List<Coordinate>> entry : other.graph.entrySet()) {
            this.graph.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Deep copy of the transpose graph
        this.graph_T = new HashMap<>();
        for (Map.Entry<Coordinate, List<Coordinate>> entry : other.graph_T.entrySet()) {
            this.graph_T.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    //return you list of cells that depend on you
    public List<Coordinate> getNeighbors(Coordinate coordinate) {
        return this.graph.get(coordinate);
    }

    //return you list of cells that you depend on them
    public List<Coordinate> getSources(Coordinate coordinate) {
        return this.graph_T.get(coordinate);
    }

    public void addVertex(Coordinate coordinate) {
        graph.putIfAbsent(coordinate, new LinkedList<>());
        graph_T.putIfAbsent(coordinate, new LinkedList<>());  // Ensure vertex is also added to the transpose graph
    }

    public void addEdge(Coordinate from, Coordinate to) {
        graph.putIfAbsent(from, new LinkedList<>());
        graph.putIfAbsent(to, new LinkedList<>());
        if(!graph.get(from).contains(to)){
            graph.get(from).add(to);
            graph_T.putIfAbsent(to, new LinkedList<>());
            graph_T.get(to).add(from);  // Add reverse edge in the transpose graph
        }
    }

    public void removeEdge(Coordinate from, Coordinate to) {
        List<Coordinate> neighbors = graph.get(from);
        List<Coordinate> neighborsT = graph_T.get(to);  // Transpose neighbors

        if (neighbors != null) {
            neighbors.remove(to);
        }

        if (neighborsT != null) {
            neighborsT.remove(from);
        }
    }

    public void removeEntryEdges(Coordinate coordinate) {
        List<Coordinate> neighbors = graph_T.get(coordinate);
        for(int i = 0 ; i < neighbors.size() ;) {
            removeEdge(neighbors.get(i), coordinate);
        }

    }

    public List<Coordinate> topologicalSort() {
        List<Coordinate> sortedList = new ArrayList<>();
        Set<Coordinate> visited = new HashSet<>();
        Stack<Coordinate> stack = new Stack<>();

        for (Coordinate vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                topologicalSortUtil(vertex, visited, stack);
            }
        }

        // Pop elements from the stack to get the sorted order
        while (!stack.isEmpty()) {
            sortedList.add(stack.pop());
        }

        return sortedList;
    }

    private void topologicalSortUtil(Coordinate vertex, Set<Coordinate> visited, Stack<Coordinate> stack) {
        visited.add(vertex);

        // Recur for all the adjacent vertices
        for (Coordinate neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                topologicalSortUtil(neighbor, visited, stack);
            }
        }

        // Push current vertex to stack which stores result
        stack.push(vertex);
    }

    public boolean hasCycle() {
        Set<Coordinate> visited = new HashSet<>();
        Set<Coordinate> recursionStack = new HashSet<>();

        for (Coordinate vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleUtil(vertex, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasCycleUtil(Coordinate vertex, Set<Coordinate> visited, Set<Coordinate> recursionStack) {
        // Mark the node as visited and add it to the recursion stack
        visited.add(vertex);
        recursionStack.add(vertex);

        // Recur for all neighbors
        for (Coordinate neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                if (hasCycleUtil(neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                // If neighbor is in recursion stack, then there is a cycle
                return true;
            }
        }
        // Remove from recursion stack before returning
        recursionStack.remove(vertex);
        return false;
    }


    public Set<Coordinate> listOfAccessibleVertex(Coordinate coordinate) {
        Set<Coordinate> list = new HashSet<>();
        if(!graph.containsKey(coordinate)){
            return list;
        }
        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(coordinate);
        while(!queue.isEmpty()){
            Coordinate current = queue.remove();
            list.add(current);
            for(Coordinate coord : getNeighbors(current)){
                queue.add(coord);
            }
        }
        return list;
    }
}
