package algorithms;

import models.*;
import java.util.*;

public class KruskalsAlgorithm {
    private Graph graph;
    private int operationCount;
    private Map<String, Integer> nodeToIndex;

    public KruskalsAlgorithm(Graph graph) {
        this.graph = graph;
        this.operationCount = 0;
        createNodeMappings();
    }

    private void createNodeMappings() {
        nodeToIndex = new HashMap<>();
        for (int i = 0; i < graph.nodes.size(); i++) {
            nodeToIndex.put(graph.nodes.get(i), i);
        }
    }

    public List<Edge> findMST() {
        List<Edge> mst = new ArrayList<>();
        operationCount = 0;

        List<Edge> sortedEdges = new ArrayList<>(graph.edges);
        Collections.sort(sortedEdges);
        operationCount += (int)(graph.edges.size() * Math.log(graph.edges.size()));

        int vertices = graph.nodes.size();
        int[] parent = new int[vertices];
        int[] rank = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        int edgesAdded = 0;
        int edgeIndex = 0;

        while (edgesAdded < vertices - 1 && edgeIndex < sortedEdges.size()) {
            operationCount++;

            Edge nextEdge = sortedEdges.get(edgeIndex++);

            int rootFrom = find(parent, nodeToIndex.get(nextEdge.from));
            int rootTo = find(parent, nodeToIndex.get(nextEdge.to));

            if (rootFrom != rootTo) {
                mst.add(nextEdge);
                edgesAdded++;
                union(parent, rank, rootFrom, rootTo);
            }
        }

        return mst;
    }

    private int find(int[] parent, int vertex) {
        operationCount++;
        if (parent[vertex] != vertex) {
            parent[vertex] = find(parent, parent[vertex]);
            operationCount++;
        }
        return parent[vertex];
    }

    private void union(int[] parent, int[] rank, int x, int y) {
        operationCount++;

        if (rank[x] < rank[y]) {
            parent[x] = y;
        } else if (rank[x] > rank[y]) {
            parent[y] = x;
        } else {
            parent[y] = x;
            rank[x]++;
        }
    }

    public int getOperationCount() {
        return operationCount;
    }
}