package com.ascat.transformer.typo;

import com.ascat.transformer.MappingRule;
import com.ascat.transformer.typo.Graph;
import com.ascat.transformer.typo.Node;

import java.util.*;

public class Sorter {

    public static List<String> topoSort(Graph g) {

        // Fetching the number of nodes in the graph
        int V = g.getSize();

        // List where we'll be storing the topological order
        List<String> order = new ArrayList<>();

        // Map which indicates if a node is visited (has been processed by the algorithm)
        Map<String, Boolean> visited = new HashMap<>();
        for (Node tmp : g.getNodes())
            visited.put(tmp.getName(), false);

        // We go through the nodes using black magic
        for (Node tmp : g.getNodes()) {
            if (!visited.get(tmp.getName()))
                blackMagic(g, tmp.getName(), visited, order);
        }

        // We reverse the order we constructed to get the
        // proper toposorting
        Collections.reverse(order);
        System.out.println("Topo Sort Result:");
        System.out.println(order);
        return order;
    }

    private static void blackMagic(Graph g, String v, Map<String, Boolean> visited, List<String> order) {
        // Mark the current node as visited
        visited.replace(v, true);
        Integer i;

        // We reuse the algorithm on all adjacent nodes to the current node
        for (String neighborId : g.getNode(v).getNeighbors()) {
            if (!visited.get(neighborId))
                blackMagic(g, neighborId, visited, order);
        }

        // Put the current node in the array
        order.add(v);
    }
}
