package com.netease;

public class MatchCondition {

    public static String[] SYMBOL = new String[]{"=","<",">"};
    private String symbol;

    public MatchCondition(String filterExpression) {
        for(String s: SYMBOL){
            if(filterExpression.contains(s)){
                symbol = s;
            };
        }
    }

    public String getSymbol(){
        return symbol;
    }

    public Boolean Match(String currentValue, String checkValue) {
        if(symbol.equalsIgnoreCase("=")){
            return currentValue.equals(checkValue);
        }else if(symbol.equalsIgnoreCase(">")){
            return Integer.parseInt(currentValue) > Integer.parseInt(checkValue);
        }else if(symbol.equalsIgnoreCase("<")){
            return Integer.parseInt(currentValue) < Integer.parseInt(checkValue);
        }else{
            return true;
        }
    }
}
