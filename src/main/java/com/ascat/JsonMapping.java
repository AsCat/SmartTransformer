package com.ascat;

public class JsonMapping {

    private String expression;
    private StructFiled dstFiled;
    private StructFiled srcFiled1;
    private StructFiled srcFiled2;

    public JsonMapping(String mapping) {
        //("(A.a2=B.b1)->A.a4"
        this.expression = mapping;
        srcFiled1 = new StructFiled(StringUtils.middle(mapping, "(", "="));
        srcFiled2 = new StructFiled(StringUtils.middle(mapping, "=", ")"));
        dstFiled = new StructFiled(StringUtils.middle(mapping, "->", ""));
    }

    public String getDstFiledName() {
        return this.dstFiled.getStructFiled();
    }

    public String getParentStructName() {
        return this.dstFiled.getStructName();
    }

    public String getParentFieldName() {
        return srcFiled1.getStructName().equalsIgnoreCase(dstFiled.getStructName()) ? srcFiled1.getStructFiled() : srcFiled2.getStructFiled();
    }

    public String getChildStructName() {
        return srcFiled1.getStructName().equalsIgnoreCase(dstFiled.getStructName()) ? srcFiled2.getStructName() : srcFiled1.getStructName();
    }

    public String getChildFieldName() {
        return srcFiled1.getStructName().equalsIgnoreCase(dstFiled.getStructName()) ? srcFiled2.getStructFiled() : srcFiled1.getStructFiled();
    }

    public String getExpression() {
        return expression;
    }
}
