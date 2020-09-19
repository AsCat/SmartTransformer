package com.netease;

import java.util.*;
import java.util.stream.Collectors;

public class DependencyParser {

    Stack<Node> stack;

    public DependencyParser() {
        stack = new Stack<Node>();
    }

    public List<JsonMapping> sort(List<JsonMapping> mappings) {

        Map<String, Node> nodes = new HashMap<String, Node>();
        Node firstNode = null;

        for (JsonMapping jm : mappings) {
            Node node = new Node(jm);
            nodes.put(jm.getParentStructName(), node);
        }

        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            // String key = entry.getKey();
            Node node = entry.getValue();
            node.addDependency(nodes.get(node.getDependencyStructName()));

            if (firstNode == null) {
                firstNode = node;
            }
        }

        toplogicalSort(firstNode);

        // dependency
        List<String> orderedStructNames = new ArrayList<String>();
        System.out.println("dependency order:");
        Stack<Node> resultStack = stack;
        while (resultStack.empty() == false) {
            JsonMapping jsonMapping = resultStack.pop().data;
            System.out.print(jsonMapping.getParentStructName() + " <-- " + jsonMapping.getChildStructName() + ", ");
            System.out.println();
            if(!orderedStructNames.contains(jsonMapping.getParentStructName())){
                orderedStructNames.add(jsonMapping.getParentStructName());
            }
            if(!orderedStructNames.contains(jsonMapping.getChildStructName())){
                orderedStructNames.add(jsonMapping.getChildStructName());
            }
        }

        // sort mappings
        List<JsonMapping> result = new ArrayList<JsonMapping>();
        for (final String name : orderedStructNames) {
            List<JsonMapping> matchMappins = mappings.stream().filter(o -> o.getParentStructName().equalsIgnoreCase(name)).collect(Collectors.toList());
            result.addAll(matchMappins);
        }

        System.out.println("ordered mappings:");
        for (JsonMapping jm: result) {
            System.out.println(jm.getExpression());
        }

        return result;
    }

    public void toplogicalSort(Node node) {
        List<Node> neighbours = node.getDependencies();
        for (int i = 0; i < neighbours.size(); i++) {
            Node n = neighbours.get(i);
            if (n != null && !n.visited) {
                toplogicalSort(n);
                n.visited = true;
            }
        }
        stack.push(node);
    }

    static class Node {
        JsonMapping data;
        boolean visited;
        List<Node> neighbours;

        Node(JsonMapping data) {
            this.data = data;
            this.neighbours = new ArrayList<Node>();
        }

        public void addDependency(Node neighbourNode) {
            this.neighbours.add(neighbourNode);
        }

        public List<Node> getDependencies() {
            return neighbours;
        }

        public void setDependencies(List<Node> neighbours) {
            this.neighbours = neighbours;
        }

        public String getDependencyStructName() {
            return data.getChildStructName();
        }

        public String toString() {
            return "" + data;
        }
    }
}
