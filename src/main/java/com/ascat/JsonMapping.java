package com.ascat;

public class JsonMapping {

    private String expression;
    private StructFiled dstFiled;
    private StructFiled mapSrcFiled1;
    private StructFiled mapSrcFiled2;

    public JsonMapping(String mapping) {
        //("(A.a2=B.b1)->A.a4"
        this.expression = mapping;
        mapSrcFiled1 = new StructFiled(StringUtils.middle(mapping, "(", "="));
        mapSrcFiled2 = new StructFiled(StringUtils.middle(mapping, "=", ")"));
        dstFiled = new StructFiled(StringUtils.middle(mapping, "->", ""));
    }

    public String getDstFiledName() {
        return this.dstFiled.getStructFiled();
    }

    public String getParentStructName() {
        return this.dstFiled.getStructName();
    }

    public String getParentFieldName() {
        return mapSrcFiled1.getStructName().equalsIgnoreCase(dstFiled.getStructName()) ? mapSrcFiled1.getStructFiled() : mapSrcFiled2.getStructFiled();
    }

    public String getChildStructName() {
        return mapSrcFiled1.getStructName().equalsIgnoreCase(dstFiled.getStructName()) ? mapSrcFiled2.getStructName() : mapSrcFiled1.getStructName();
    }

    public String getChildFieldName() {
        return mapSrcFiled1.getStructName().equalsIgnoreCase(dstFiled.getStructName()) ? mapSrcFiled2.getStructFiled() : mapSrcFiled1.getStructFiled();
    }

    public String getExpression(){
        return expression;
    }
}
