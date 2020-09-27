package com.ascat.transformer;


import com.ascat.transformer.input.StructType;
import com.ascat.transformer.typo.Graph;
import com.ascat.transformer.typo.Node;
import com.ascat.transformer.typo.Sorter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class Transformer {

//    private Map<String, Input> inputs = new HashMap<>();

    private List<String> sortedStructNames = new ArrayList<>();
    private List<RelationMappingRule> relationMappingRuleList = new ArrayList<>();

    private List<JsonFilter> filters = new ArrayList<>();

    private Map<String, DataStruct> dataStructMap = new HashMap<>();

    private String resultExpression;

    public void addJsonInput(String structName, String structJsonData, StructType structType) {
//        this.inputs.put(structName, new JsonInput(structName, structJsonData));
        this.dataStructMap.put(structName, new DataStruct(structName, structJsonData, structType));
    }

    public void addHttpInput(String structName, String httpUrl, Map<String, String> httpParas, StructType structType) {
//        this.inputs.put(structName, new HttpInput(structName, dataType, httpUrl, httpParas));
        this.dataStructMap.put(structName, new DataStruct(structName, httpUrl, httpParas, structType));
    }

    public void addFilter(String filterExpression) {
        this.filters.add(new JsonFilter(filterExpression));
    }


    public void addParamsMapping(String StructFiled, String srcParam) {

    }

    public void addResultMapping(String resultField, String srcFiled) {

    }

    public void addRelationMapping(String dstFiled, String srcFiled) {
        this.relationMappingRuleList.add(new RelationMappingRule(dstFiled, srcFiled));
    }

//    public void addMapping(String mapping) {
//        this.relationMappingRuleList.add(new RelationMappingRule(mapping));
//    }

    private void sortInputStructs() {

        Map<String, Node> structNodeMap = new HashMap<>();

        Graph g = new Graph();

        for (RelationMappingRule rule : relationMappingRuleList) {
            Node parentNode = new Node(rule.getParentStructName());
            structNodeMap.put(rule.getParentStructName(), parentNode);

            Node childNode = new Node(rule.getChildStructName());
            structNodeMap.put(rule.getChildStructName(), childNode);
        }

        for (RelationMappingRule rule : relationMappingRuleList) {
            Node parentNode = structNodeMap.get(rule.getParentStructName());
            Node childNode = structNodeMap.get(rule.getChildStructName());
            parentNode.addNeighbor(childNode.getName());
        }

        for (Map.Entry<String, Node> entry : structNodeMap.entrySet()) {
            g.addNode(entry.getValue());
        }

        this.sortedStructNames = Sorter.topoSort(g);
    }

    private void sortMappingRules() {

        Collections.sort(relationMappingRuleList, (b1, b2) -> {
            int s1 = this.sortedStructNames.indexOf(b1.getParentStructName());
            int s2 = this.sortedStructNames.indexOf(b2.getParentStructName());
            if (s1 == s2) return 0;
            return s1 > s2 ? 1 : -1;
        });
    }

    public Map<String, DataStruct> process(Map<String, Object> runtimeParas) {

        sortInputStructs();

        sortMappingRules();

        resolveStructRelations();

        prepareDynamicData(runtimeParas);

        if (System.currentTimeMillis() > 0) {
            return new HashMap<>();
        }

        // apply filter
//        for (int i = 0; i < this.filters.size(); i++) {
//            applyFilter(dataStructMap, this.filters.get(i));
//        }
//
//        // apply mapping
//        for (int i = this.relationMappingRuleList.size() - 1; i >= 0; i--) {
//            applyMapping(dataStructMap, this.relationMappingRuleList.get(i));
//        }

        // apply result expression
//        for (String key : dataStructMap.keySet()) {
//            resultExpression = resultExpression.replace("$" + key + "$", dataStructMap.get(key).getStructData());
//        }
//        dataStructMap.put("$RESULT$", new DataStruct("$RESULT$", resultExpression));

        return dataStructMap;
    }

    private void resolveStructRelations() {

        for (RelationMappingRule rule : relationMappingRuleList) {
            String parentStructName = rule.getParentStructName();
            String childStructName = rule.getChildStructName();

            dataStructMap.get(parentStructName).addChildRelation(childStructName, dataStructMap.get(childStructName));
            dataStructMap.get(childStructName).addParentRelation(parentStructName, dataStructMap.get(parentStructName));

        }
    }

    private void prepareDynamicData(Map<String, Object> runtimeParas) {

//        for (Map.Entry<String, Input> entry : inputs.entrySet()) {
//            if (entry.getValue() instanceof HttpInput) {
//                HttpInput input = (HttpInput) entry.getValue();
//                if (!input.canAcquire()) {
//                    if (!input.validateDependency(relationMappingRuleList)) {
//                        throw new RuntimeException("Missing Mapping Rule for Struct: " + input.getStructName());
//                    }
//                }
//            }
//        }

        // acquire data with
        for (String structName : sortedStructNames) {

            DataStruct dataStruct = dataStructMap.get(structName);
            dataStruct.prepareData();

//            Input input = inputs.get(structName);
//            if (input instanceof HttpInput) {
//                HttpInput httpInput = (HttpInput) input;
//                if (httpInput.canAcquire()) {
//                    httpInput.acquireData(runtimeParas);
//                }
//            }
        }
    }

    private void applyMapping(Map<String, DataStruct> temps, RelationMappingRule jsonMapping) {

//        System.out.println(" process apply mapping with expression: " + jsonMapping.getExpression());
//        String parentStructName = jsonMapping.getParentStructName();
//        String childStructName = jsonMapping.getChildStructName();
//        String dstFieldName = jsonMapping.getDstFiledName();
//
//        JsonArray parentList = JsonParser.parseString(temps.get(parentStructName).getData()).getAsJsonArray();
//        JsonArray childList = JsonParser.parseString(temps.get(childStructName).getData()).getAsJsonArray();
//
//        for (int i = 0; i < parentList.size(); i++) {
//            JsonObject parentItem = parentList.get(i).getAsJsonObject();
//            JsonElement parentCheckVal = parentItem.get(jsonMapping.getParentFieldName());
//
//            JsonArray insertArray = new JsonArray();
//            for (int j = 0; j < childList.size(); j++) {
//                JsonObject childItem = childList.get(j).getAsJsonObject();
//                JsonElement childCheckVal = childItem.get(jsonMapping.getChildFieldName());
//
//                // fixme
//                if (childCheckVal != null && parentCheckVal != null && childCheckVal.toString().equals(parentCheckVal.toString())) {
//                    System.out.println("Match Child Item");
//
//                    insertArray.add(childItem);
//                }
//            }
//            if(insertArray.size() == 0){
//              // do nothing
//            } else if(insertArray.size() == 1){
//                parentItem.add(dstFieldName, insertArray.get(0));
//            } else{
//                parentItem.add(dstFieldName + "List", insertArray);
//            }
//        }
//
//        temps.put(parentStructName, new DataStruct(parentStructName, parentList.toString()));
    }


//    private void applyFilter(Map<String, DataStruct> temps, JsonFilter filter) {
//
//        String structName = filter.getStructName();
//        String jsonData = temps.get(structName).getStructData();
//
//        JsonArray objList = JsonParser.parseString(jsonData).getAsJsonArray();
//
//        for (int i = objList.size() - 1; i >= 0; i--) {
//            JsonObject item = objList.get(i).getAsJsonObject();
//
//            if (!filter.IsMatch(item)) {
//                objList.remove(i);
//            }
//        }
//        temps.put(structName, new DataStruct(structName, objList.toString()));
//    }

    public void setAcceptHolder(String resultExpression) {
        this.resultExpression = resultExpression;
    }

    public void addInvokeInput(String student, String invokeExpression) {

    }
}
