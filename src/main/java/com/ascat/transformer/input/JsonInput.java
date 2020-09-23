package com.ascat.transformer.input;

public class JsonInput implements Input{

    private String structName;
    private String rawJsonData;

    private String runtimeData;

    public JsonInput(String structName, String rawJsonData) {
        this.structName = structName;
        this.rawJsonData = rawJsonData;

        this.runtimeData = rawJsonData;
    }
}
