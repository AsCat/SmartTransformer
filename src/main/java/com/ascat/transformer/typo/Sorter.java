package com.ascat.transformer.typo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Sorter {

    private static Logger logger = LoggerFactory.getLogger(Sorter.class.getName());

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
        logger.debug("Topo Sort Result: {}", order);
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
