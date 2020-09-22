package com.ascat.transformer;

public class StructFiled {

    private String rawStructFiled;

    private String structName;
    private String structFiledPath;
    private String structFiledName;

    public StructFiled(String structFiledStr) {
        this.rawStructFiled = structFiledStr;

        this.structName = rawStructFiled.substring(0, rawStructFiled.indexOf("."));
        String fieldPathAndName = structFiledStr.substring(this.structName.length());

        int lastDotIndex = fieldPathAndName.lastIndexOf(".");
        if (lastDotIndex >= 0) {
            structFiledPath = fieldPathAndName.substring(0, lastDotIndex);
            structFiledName = fieldPathAndName.substring(lastDotIndex + 1);
        } else {
            structFiledPath = "";
            structFiledName = fieldPathAndName;
        }
    }

    public String getStructFiledPath() {
        return structFiledPath;
    }

    public String getStructFiledName() {
        return structFiledName;
    }

    public String getRawStructFiled() {
        return rawStructFiled;
    }

    public String getStructName() {
        return structName;
    }
}
