package utils;

import models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class JSONProcessor {

    public static List<Graph> readGraphsFromJSON(String filename) {
        List<Graph> graphs = new ArrayList<>();

        try {
            FileReader reader = new FileReader(filename);
            JSONObject json = new JSONObject(new JSONTokener(reader));

            JSONArray graphsArray = json.getJSONArray("graphs");

            for (int i = 0; i < graphsArray.length(); i++) {
                JSONObject graphObj = graphsArray.getJSONObject(i);
                int id = graphObj.getInt("id");

                JSONArray nodesArray = graphObj.getJSONArray("nodes");
                List<String> nodes = new ArrayList<>();
                for (int j = 0; j < nodesArray.length(); j++) {
                    nodes.add(nodesArray.getString(j));
                }

                JSONArray edgesArray = graphObj.getJSONArray("edges");
                List<Edge> edges = new ArrayList<>();
                for (int j = 0; j < edgesArray.length(); j++) {
                    JSONObject edgeObj = edgesArray.getJSONObject(j);
                    String from = edgeObj.getString("from");
                    String to = edgeObj.getString("to");
                    int weight = edgeObj.getInt("weight");
                    edges.add(new Edge(from, to, weight));
                }

                graphs.add(new Graph(id, nodes, edges));
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }

        return graphs;
    }

    public static void writeResultsToJSON(String filename, List<Map<String, Object>> results) {
        try {
            JSONObject output = new JSONObject();
            JSONArray resultsArray = new JSONArray();

            for (Map<String, Object> result : results) {
                JSONObject resultObj = new JSONObject();
                resultObj.put("graph_id", result.get("graph_id"));

                JSONObject inputStats = new JSONObject();
                inputStats.put("vertices", result.get("vertices"));
                inputStats.put("edges", result.get("edges"));
                resultObj.put("input_stats", inputStats);

                JSONObject prim = new JSONObject();
                prim.put("total_cost", result.get("prim_cost"));
                prim.put("operations_count", result.get("prim_operations"));
                prim.put("execution_time_ms", result.get("prim_time"));

                JSONArray primEdges = new JSONArray();
                for (Edge edge : (List<Edge>)result.get("prim_edges")) {
                    JSONObject edgeObj = new JSONObject();
                    edgeObj.put("from", edge.from);
                    edgeObj.put("to", edge.to);
                    edgeObj.put("weight", edge.weight);
                    primEdges.put(edgeObj);
                }
                prim.put("mst_edges", primEdges);
                resultObj.put("prim", prim);

                JSONObject kruskal = new JSONObject();
                kruskal.put("total_cost", result.get("kruskal_cost"));
                kruskal.put("operations_count", result.get("kruskal_operations"));
                kruskal.put("execution_time_ms", result.get("kruskal_time"));

                JSONArray kruskalEdges = new JSONArray();
                for (Edge edge : (List<Edge>)result.get("kruskal_edges")) {
                    JSONObject edgeObj = new JSONObject();
                    edgeObj.put("from", edge.from);
                    edgeObj.put("to", edge.to);
                    edgeObj.put("weight", edge.weight);
                    kruskalEdges.put(edgeObj);
                }
                kruskal.put("mst_edges", kruskalEdges);
                resultObj.put("kruskal", kruskal);

                resultsArray.put(resultObj);
            }

            output.put("results", resultsArray);

            FileWriter writer = new FileWriter(filename);
            writer.write(output.toString(2));
            writer.close();

            System.out.println("Results written to: " + filename);

        } catch (Exception e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    public static int calculateTotalCost(List<Edge> mst) {
        int totalCost = 0;
        for (Edge edge : mst) {
            totalCost += edge.weight;
        }
        return totalCost;
    }
}