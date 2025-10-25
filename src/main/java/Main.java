import models.*;
import algorithms.*;
import utils.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Read graphs from JSON file
        List<Graph> graphs = JSONProcessor.readGraphsFromJSON("src/test/resources/ass_3_input.json");

        if (graphs.isEmpty()) {
            System.out.println("No graphs found in input file.");
            return;
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (Graph graph : graphs) {
            System.out.println("\n=== Processing Graph " + graph.id + " ===");
            System.out.println("Vertices: " + graph.getVertexCount() + ", Edges: " + graph.getEdgeCount());

            Map<String, Object> result = new HashMap<>();
            result.put("graph_id", graph.id);
            result.put("vertices", graph.getVertexCount());
            result.put("edges", graph.getEdgeCount());

            // Prim's Algorithm
            System.out.println("\n--- Prim's Algorithm ---");
            PrimsAlgorithm prim = new PrimsAlgorithm(graph);

            long startTime = System.nanoTime();
            List<Edge> primMST = prim.findMST();
            long endTime = System.nanoTime();

            int primCost = JSONProcessor.calculateTotalCost(primMST);
            double primTime = (endTime - startTime) / 1_000_000.0;

            System.out.println("MST Edges: " + primMST);
            System.out.println("Total Cost: " + primCost);
            System.out.println("Operations: " + prim.getOperationCount());
            System.out.println("Execution Time: " + String.format("%.2f", primTime) + " ms");

            result.put("prim_edges", primMST);
            result.put("prim_cost", primCost);
            result.put("prim_operations", prim.getOperationCount());
            result.put("prim_time", primTime);

            // Kruskal's Algorithm
            System.out.println("\n--- Kruskal's Algorithm ---");
            KruskalsAlgorithm kruskal = new KruskalsAlgorithm(graph);

            startTime = System.nanoTime();
            List<Edge> kruskalMST = kruskal.findMST();
            endTime = System.nanoTime();

            int kruskalCost = JSONProcessor.calculateTotalCost(kruskalMST);
            double kruskalTime = (endTime - startTime) / 1_000_000.0;

            System.out.println("MST Edges: " + kruskalMST);
            System.out.println("Total Cost: " + kruskalCost);
            System.out.println("Operations: " + kruskal.getOperationCount());
            System.out.println("Execution Time: " + String.format("%.2f", kruskalTime) + " ms");

            result.put("kruskal_edges", kruskalMST);
            result.put("kruskal_cost", kruskalCost);
            result.put("kruskal_operations", kruskal.getOperationCount());
            result.put("kruskal_time", kruskalTime);

            // Comparison
            System.out.println("\n--- Comparison ---");
            System.out.println("Total costs are " + (primCost == kruskalCost ? "EQUAL ✓" : "DIFFERENT ✗"));
            System.out.println("Prim's operations: " + prim.getOperationCount());
            System.out.println("Kruskal's operations: " + kruskal.getOperationCount());

            results.add(result);
        }

        // Write results to JSON file
        JSONProcessor.writeResultsToJSON("src/test/resources/ass_3_output.json", results);
    }
}