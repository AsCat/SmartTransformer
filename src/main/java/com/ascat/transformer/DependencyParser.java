package com.ascat.transformer;

import com.ascat.transformer.typo.Graph;
import com.ascat.transformer.typo.Node;

import java.util.*;

public class DependencyParser {

    Stack<StructNode> stack;

    public DependencyParser() {
        stack = new Stack<>();
    }

    public List<MappingRule> sort(List<MappingRule> mappingRuleList) {

        Map<String, Node> structNodeMap = new HashMap<>();

        Graph g = new Graph();

        for (MappingRule rule : mappingRuleList) {
            Node parentNode = new Node(rule.getParentStructName());
            structNodeMap.put(rule.getParentStructName(), parentNode);

            Node childNode = new Node(rule.getChildStructName());
            structNodeMap.put(rule.getChildStructName(), childNode);
        }

        for (MappingRule rule : mappingRuleList) {
            Node parentNode = structNodeMap.get(rule.getParentStructName());
            Node childNode = structNodeMap.get(rule.getChildStructName());
            parentNode.addNeighbor(childNode.getName());
        }

        for (Map.Entry<String, Node> entry : structNodeMap.entrySet()){
            g.addNode(entry.getValue());
        }

        System.out.println(g);

//        Map<String, StructNode> structNodeMap = new HashMap<>();
//        StructNode firstNode = null;
//
//        for (MappingRule rule : mappingRuleList) {
//            StructNode parentNode = new StructNode(rule.getParentStructName());
//            structNodeMap.put(rule.getParentStructName(), parentNode);
//
//            StructNode childNode = new StructNode(rule.getChildStructName());
//            structNodeMap.put(rule.getChildStructName(), childNode);
//        }
//
//        for (MappingRule rule : mappingRuleList) {
//            StructNode parentNode = structNodeMap.get(rule.getParentStructName());
//            StructNode childNode = structNodeMap.get(rule.getChildStructName());
//            parentNode.addChild(childNode);
//        }
//
//        for (Map.Entry<String, StructNode> entry : nodes.entrySet()) {
//            // String key = entry.getKey();
//            StructNode structNode = entry.getValue();
//            structNode.addChild(nodes.get(structNode.getChildStructName()));
//
//            if (firstNode == null) {
//                firstNode = structNode;
//            }
//        }

//        toplogicalSort(structNodeMap.get);

        // dependency
//        List<String> orderedStructNames = new ArrayList<String>();
//        System.out.println("dependency order:");
//        Stack<StructNode> resultStack = stack;
//        while (resultStack.empty() == false) {
//            MappingRule jsonMapping = resultStack.pop().mappingRule;
//            System.out.print(jsonMapping.getParentStructName() + " <-- " + jsonMapping.getChildStructName() + ", ");
//            System.out.println();
//            if(!orderedStructNames.contains(jsonMapping.getParentStructName())){
//                orderedStructNames.add(jsonMapping.getParentStructName());
//            }
//            if(!orderedStructNames.contains(jsonMapping.getChildStructName())){
//                orderedStructNames.add(jsonMapping.getChildStructName());
//            }
//        }

        // sort mappings
//        List<MappingRule> result = new ArrayList<MappingRule>();
//        for (final String name : orderedStructNames) {
//            List<MappingRule> matchMappins = mappingRuleList.stream().filter(o -> o.getParentStructName().equalsIgnoreCase(name)).collect(Collectors.toList());
//            result.addAll(matchMappins);
//        }
//
//        System.out.println("ordered mappings:");
//        for (MappingRule jm: result) {
//            System.out.println(jm.getExpression());
//        }

//        return result;

        return null;
    }

    public void toplogicalSort(StructNode node) {
        List<StructNode> neighbours = node.getChildNodes();
        for (int i = 0; i < neighbours.size(); i++) {
            StructNode n = neighbours.get(i);
            if (n != null && !n.visited) {
                toplogicalSort(n);
                n.visited = true;
            }
        }
        stack.push(node);
    }

    static class StructNode {
        //MappingRule mappingRule;
        String structName;
        boolean visited;
        List<StructNode> childNodes;

        StructNode(String structName) {
            this.structName = structName;
            this.childNodes = new ArrayList<>();
        }

        public void addChild(StructNode childNode) {
            this.childNodes.add(childNode);
        }

        public List<StructNode> getChildNodes() {
            return childNodes;
        }

        public void setChildNodes(List<StructNode> childNodes) {
            this.childNodes = childNodes;
        }

//        public String getChildStructName() {
//            return mappingRule.getChildStructName();
//        }

        public String toString() {
            return "" + structName;
        }
    }
}
