package com.ascat.transformer.input;

import com.ascat.transformer.DataStruct;

public class DynamicPara {

    private String paraKey;
    private String rawMappingPath;

    private Boolean isHeadLessPara;

    private String srcParaName;

    private String srcStructName;
    private String srcStructFiledPath;
    private String srcStructFiledName;

    private DataStruct refDataStruct;

    private String runtimeValue;

    public DynamicPara(String key, String value) {
        this.paraKey = key;
        this.rawMappingPath = value;

        if (this.rawMappingPath.contains(".")) {
            isHeadLessPara = false;
            String cleanMappingPath = this.rawMappingPath.replace("$", "");
            this.srcStructName = cleanMappingPath.substring(0, cleanMappingPath.indexOf("."));
            String fieldPathAndName = cleanMappingPath.substring(this.srcStructName.length());
            int lastDotIndex = fieldPathAndName.lastIndexOf(".");
            if (lastDotIndex >= 0) {
                srcStructFiledPath = fieldPathAndName.substring(0, lastDotIndex);
                srcStructFiledName = fieldPathAndName.substring(lastDotIndex + 1);
            } else {
                srcStructFiledPath = "";
                srcStructFiledName = fieldPathAndName;
            }
        } else {
            isHeadLessPara = true;
            srcParaName = this.rawMappingPath.replace("$", "");
        }
    }

    public String getParaKey() {
        return paraKey;
    }

    public void setParaKey(String paraKey) {
        this.paraKey = paraKey;
    }

    public String getRawMappingPath() {
        return rawMappingPath;
    }

    public void setRawMappingPath(String rawMappingPath) {
        this.rawMappingPath = rawMappingPath;
    }

    public String getSrcStructName() {
        return srcStructName;
    }

    public void setSrcStructName(String srcStructName) {
        this.srcStructName = srcStructName;
    }

    public Boolean getHeadLessPara() {
        return isHeadLessPara;
    }

    public void setHeadLessPara(Boolean headLessPara) {
        isHeadLessPara = headLessPara;
    }

    public DataStruct getRefDataStruct() {
        return refDataStruct;
    }

    public void setRefDataStruct(DataStruct refDataStruct) {
        this.refDataStruct = refDataStruct;
    }

    public String getRuntimeValue() {
        return runtimeValue;
    }

    public void setRuntimeValue(String runtimeValue) {
        this.runtimeValue = runtimeValue;
    }

    public String getSrcParaName() {
        return srcParaName;
    }

    public void setSrcParaName(String srcParaName) {
        this.srcParaName = srcParaName;
    }

    public String getSrcStructFiledPath() {
        return srcStructFiledPath;
    }

    public void setSrcStructFiledPath(String srcStructFiledPath) {
        this.srcStructFiledPath = srcStructFiledPath;
    }

    public String getSrcStructFiledName() {
        return srcStructFiledName;
    }

    public void setSrcStructFiledName(String srcStructFiledName) {
        this.srcStructFiledName = srcStructFiledName;
    }
}
