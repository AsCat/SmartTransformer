package com.ascat.transformer;

import com.ascat.transformer.input.StructType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DataStruct {

    private String structName;
    private String structData;
    private StructType structType;

    private Boolean dataIsReady = false;

    private HashMap<String, DataStruct> childDataStructMap = new HashMap<>();
    private HashMap<String, DataStruct> parentDataStructMap = new HashMap<>();

    private HashMap<String, String> runtimeData = new HashMap<>();

    public DataStruct(String structName, String structValue, StructType structType) {
        this.structName = structName;
        this.structData = structValue;
        this.structType = structType;
        this.dataIsReady = true;
    }

    public DataStruct(String structName, String httpUrl, Map<String, String> httpParas, StructType structType) {
        this.structName = structName;
        this.structType = structType;
        this.structData = "ERROR: DATA IS NOT READY";
        this.dataIsReady = false;
    }

    public String getStructData(){
        return structData;
    }

    public void addChildRelation(String childStructName, DataStruct dataStruct) {
        childDataStructMap.put(childStructName, dataStruct);
    }

    public void addParentRelation(String parentStructname, DataStruct dataStruct){
        parentDataStructMap.put(parentStructname, dataStruct);
    }

    public void prepareData(){
        if(dataIsReady) return;

        // todo
        System.out.println(structName + ": data is preparing //// .... ");

        // if there exist more then one parent data struct
        // the length of parent must be same

        this.structData = DynamicDataResover.

        // mock data here
        URI classUri = null;
        try {
            classUri = Main.class.getClassLoader().getResource("test/" + structName.toLowerCase() + "List.json").toURI();
            this.structData = new String(Files.readAllBytes(Paths.get(classUri)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataIsReady = true;
    }
}
