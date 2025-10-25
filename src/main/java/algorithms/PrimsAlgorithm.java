package algorithms;

import models.*;
import java.util.*;

public class PrimsAlgorithm {
    private Graph graph;
    private int operationCount;
    private Map<String, Integer> nodeToIndex;
    private Map<Integer, String> indexToNode;

    public PrimsAlgorithm(Graph graph) {
        this.graph = graph;
        this.operationCount = 0;
        createNodeMappings();
    }

    private void createNodeMappings() {
        nodeToIndex = new HashMap<>();
        indexToNode = new HashMap<>();
        for (int i = 0; i < graph.nodes.size(); i++) {
            nodeToIndex.put(graph.nodes.get(i), i);
            indexToNode.put(i, graph.nodes.get(i));
        }
    }

    public List<Edge> findMST() {
        List<Edge> mst = new ArrayList<>();
        operationCount = 0;

        int vertices = graph.nodes.size();
        boolean[] inMST = new boolean[vertices];
        int[] minWeight = new int[vertices];
        int[] parent = new int[vertices];

        Arrays.fill(minWeight, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        minWeight[0] = 0;

        for (int i = 0; i < vertices; i++) {
            operationCount++;

            // Find vertex with minimum weight that's not in MST
            int minVertex = findMinVertex(minWeight, inMST);
            inMST[minVertex] = true;

            // Add edge to MST if it's not the starting vertex
            if (parent[minVertex] != -1) {
                String fromNode = indexToNode.get(parent[minVertex]);
                String toNode = indexToNode.get(minVertex);
                mst.add(new Edge(fromNode, toNode, minWeight[minVertex]));
            }

            // Update the minWeight values for adjacent vertices
            updateMinWeights(minVertex, minWeight, parent, inMST);
        }

        return mst;
    }

    private int findMinVertex(int[] minWeight, boolean[] inMST) {
        int minVertex = -1;

        for (int i = 0; i < graph.nodes.size(); i++) {
            operationCount++;
            if (!inMST[i] && (minVertex == -1 || minWeight[i] < minWeight[minVertex])) {
                minVertex = i;
            }
        }

        return minVertex;
    }

    private void updateMinWeights(int currentVertex, int[] minWeight, int[] parent, boolean[] inMST) {
        String currentNode = indexToNode.get(currentVertex);

        // Go through all edges to find ones connected to current vertex
        for (Edge edge : graph.edges) {
            operationCount++;

            String otherNode = null;
            boolean isCurrentFrom = edge.from.equals(currentNode);
            boolean isCurrentTo = edge.to.equals(currentNode);

            if (isCurrentFrom && !isCurrentTo) {
                otherNode = edge.to;
            } else if (isCurrentTo && !isCurrentFrom) {
                otherNode = edge.from;
            }

            if (otherNode != null) {
                int otherIndex = nodeToIndex.get(otherNode);
                if (!inMST[otherIndex] && edge.weight < minWeight[otherIndex]) {
                    minWeight[otherIndex] = edge.weight;
                    parent[otherIndex] = currentVertex;
                    operationCount++;
                }
            }
        }
    }

    public int getOperationCount() {
        return operationCount;
    }
}