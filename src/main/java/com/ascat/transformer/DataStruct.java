package com.ascat.transformer;

public class DataStruct {

    private String structName;
    private String data;

    public DataStruct(String structName, String structValue) {
        this.structName = structName;
        this.data = structValue;
    }

    public String getData(){
        return data;
    }
}
