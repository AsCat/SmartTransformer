package com.netease;

import com.google.gson.JsonObject;

public class JsonFilter {

    private String structName;
    private String filedName;
    private MatchCondition condition;
    private String checkValue;

    public JsonFilter(String filterExpression) {
        // A.a1=1
        structName = StringUtils.left(filterExpression, ".");
        filedName = StringUtils.middle(filterExpression, ".", MatchCondition.SYMBOL);
        condition = new MatchCondition(filterExpression);
        checkValue = StringUtils.right(filterExpression, condition.getSymbol());
    }

    public String getStructName() {
        return structName;
    }

    public String getFiledName() {
        return filedName;
    }

    public MatchCondition getCondition() {
        return condition;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public Boolean IsMatch(JsonObject object){
        String currentValue = object.get(filedName).toString();
        return condition.Match(currentValue, checkValue);
    }
}
