package com.ascat.transformer;

public class RelationMappingRule {

    private String expression;
    private StructFiled dstFiled;
    private StructFiled srcFiled1;
    private StructFiled srcFiled2;

    private StructFiled parentFiled;
    private StructFiled childFiled;

    public RelationMappingRule(String mapping) {
        //("(A.a2=B.b1)->A.a4"
        this.expression = mapping;
        srcFiled1 = new StructFiled(StringUtils.middle(mapping, "(", "="));
        srcFiled2 = new StructFiled(StringUtils.middle(mapping, "=", ")"));
        dstFiled = new StructFiled(StringUtils.middle(mapping, "->", ""));

        if(dstFiled.getStructName().equals(srcFiled1.getStructName())){
            parentFiled = srcFiled1;
            childFiled = srcFiled2;
        }else {
            parentFiled = srcFiled2;
            childFiled = srcFiled1;
        }
    }

    public RelationMappingRule(String dstFiled, String srcFiled) {
        this.expression = dstFiled + "=" + srcFiled;
        srcFiled1 = new StructFiled(dstFiled);
        srcFiled1 = new StructFiled(srcFiled);

    }

    public String getDstFiledName() {
        return this.dstFiled.getStructFiledName();
    }

    public String getParentStructName() {
        return parentFiled.getStructName();
    }

    public String getParentFieldPath() {
        return parentFiled.getStructFiledPath();
    }

    public String getParentFieldName() {
        return parentFiled.getStructFiledName();
    }

    public String getChildStructName() {
        return childFiled.getStructName();
    }

    public String getChildFieldPath() {
        return childFiled.getStructFiledPath();
    }

    public String getChildFieldName() {
        return childFiled.getStructFiledName();
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "MappingRule{" +
                "expression='" + expression + '\'' +
                '}';
    }
}
