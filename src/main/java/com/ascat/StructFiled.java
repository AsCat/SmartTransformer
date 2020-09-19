package com.ascat;

public class StructFiled {

    private String structName;
    private String structFiled;

    public StructFiled(String structFiledStr) {
        this.structName = StringUtils.left(structFiledStr, ".");
        this.structFiled = StringUtils.right(structFiledStr, ".");
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public String getStructFiled() {
        return structFiled;
    }

    public void setStructFiled(String structFiled) {
        this.structFiled = structFiled;
    }
}
