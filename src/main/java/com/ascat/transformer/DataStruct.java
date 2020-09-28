package com.ascat.transformer;

import com.ascat.transformer.input.DynamicType;
import com.ascat.transformer.input.StructType;
import com.ascat.transformer.resolver.FileDataStructResolver;
import com.ascat.transformer.resolver.HttpDataStructResolver;
import com.ascat.transformer.resolver.InvokeDataStructResolver;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStruct {

    private String structName;
    private String structData;
    private StructType structType;
    private DynamicType dynamicType;

    private Boolean dataIsReady = false;

    private HashMap<String, DataStruct> childDataStructMap = new HashMap<>();
    private HashMap<String, DataStruct> parentDataStructMap = new HashMap<>();

    private HashMap<String, List<RelationMappingRule>> parentRelationRules = new HashMap<>();

    private HashMap<String, String> runtimeInputDataMap = new HashMap<>();

    // Static

    // HTTP
    private String httpUrl;
    private String resultJsonPath;

    // Invoke

    // File
    private String fileName;


    public HashMap<String, List<RelationMappingRule>> getParentRelationRules() {
        return parentRelationRules;
    }

    public DataStruct(String structName, String structValue, StructType structType) {
        this.structName = structName;
        this.structData = structValue;
        this.structType = structType;
        this.dataIsReady = true;
        this.dynamicType = DynamicType.Static;
    }

    public DataStruct(String structName, String httpUrl, String resultJsonPath, StructType structType) {
        this.structName = structName;
        this.structType = structType;
        this.structData = "[NOT READY]";
        this.httpUrl = httpUrl;
        this.resultJsonPath = resultJsonPath;
        this.dataIsReady = false;
        this.dynamicType = DynamicType.Http;
    }

    public String getStructData() {
        return structData;
    }

    public void addChildRelation(String childStructName, DataStruct dataStruct) {
        childDataStructMap.put(childStructName, dataStruct);
    }

    public void addParentRelation(String parentStructname, DataStruct dataStruct) {
        parentDataStructMap.put(parentStructname, dataStruct);
    }

    public void prepareData(Map<String, Object> runtimeParas) {
        if (dataIsReady) return;

        // todo
        System.out.println(structName + ": data is preparing //// .... ");

        // if there exist more then one parent data struct
        // the length of parent must be same

        this.runtimeInputDataMap = RuntimeInputDataResolver.process(parentDataStructMap, parentRelationRules);

        switch (dynamicType) {
            case Http:
                this.structData = HttpDataStructResolver.process(structName, this.httpUrl, this.resultJsonPath, this.runtimeInputDataMap, runtimeParas);
                break;
            case Invoke:
                this.structData = InvokeDataStructResolver.process(structName, this.runtimeInputDataMap, runtimeParas);
                break;
            case File:
                this.structData = FileDataStructResolver.process(structName, fileName, this.runtimeInputDataMap, runtimeParas);
                break;
            default:
                break;
        }

        dataIsReady = true;
    }

    public void addRelationRule(RelationMappingRule rule) {
        String parentStructName = rule.getParentStructName();
        if (this.parentRelationRules.containsKey(parentStructName)) {
            this.parentRelationRules.get(parentStructName).add(rule);
        } else {
            List<RelationMappingRule> ruleList = new ArrayList<>();
            ruleList.add(rule);
            this.parentRelationRules.put(parentStructName, ruleList);
        }
    }


    public String getDataFromPath(String dstJsonPath) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(structData);

        String result;
        if (structType == StructType.Array) {
            result = JsonPath.read(document, "$[*]" + dstJsonPath).toString();
        } else {
            result = JsonPath.read(document, "$" + dstJsonPath).toString();
        }

        return result;
    }
}
