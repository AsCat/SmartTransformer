package com.ascat;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transformer {

    private Map<String, JsonInput> inputs = new HashMap<>();
    private List<JsonMapping> mappings = new ArrayList<>();
    private List<JsonFilter> filters = new ArrayList<>();

    private Map<String, TempStrunct> temps = new HashMap<>();

    private String resultExpression;

    public void addInput(String structName, String structValue) {
        this.inputs.put(structName, new JsonInput(structValue));
        this.temps.put(structName, new TempStrunct(structValue));
    }

    public void addFilter(String filterExpression) {
        this.filters.add(new JsonFilter(filterExpression));
    }

    public void addMapping(String mapping) {
        this.mappings.add(new JsonMapping(mapping));
    }

    public Map<String, TempStrunct> process() {

        this.mappings = new DependencyParser().sort(this.mappings);

        // apply mapping
        for (int i = this.mappings.size() - 1; i >= 0; i--) {
            applyMapping(temps, this.mappings.get(i));
        }

        // apply filter
        for(int i = 0; i < this.filters.size(); i++){
            applyFilter(temps, this.filters.get(i));
        }

        // apply result expression
        for (String key : temps.keySet()){
            resultExpression = resultExpression.replace("$" + key, temps.get(key).getData());
        }
        temps.put("$RESULT", new TempStrunct(resultExpression));

        return temps;
    }

    private void applyMapping(Map<String, TempStrunct> temps, JsonMapping jsonMapping) {

        System.out.println(" process apply mapping with expression: " + jsonMapping.getExpression());
        String parentStructName = jsonMapping.getParentStructName();
        String childStructName = jsonMapping.getChildStructName();
        String dstFieldName = jsonMapping.getDstFiledName();

        JsonArray parentList = JsonParser.parseString(temps.get(parentStructName).getData()).getAsJsonArray();
        JsonArray childList = JsonParser.parseString(temps.get(childStructName).getData()).getAsJsonArray();

        for (int i = 0; i < parentList.size(); i++) {
            JsonObject parentItem = parentList.get(i).getAsJsonObject();
            JsonElement parentCheckVal = parentItem.get(jsonMapping.getParentFieldName());

            for (int j = 0; j < childList.size(); j++) {
                JsonObject childItem = childList.get(j).getAsJsonObject();
                JsonElement childCheckVal = childItem.get(jsonMapping.getChildFieldName());

                // fixme
                if (childCheckVal != null && parentCheckVal != null && childCheckVal.toString().equals(parentCheckVal.toString())) {
                    System.out.println("Match Child Item");

                    parentItem.add(dstFieldName, childItem);
                }
            }
        }

        temps.put(parentStructName, new TempStrunct(parentList.toString()));
    }


    private void applyFilter(Map<String, TempStrunct> temps, JsonFilter filter) {

        String structName = filter.getStructName();
        String jsonData = temps.get(structName).getData();

        JsonArray objList = JsonParser.parseString(jsonData).getAsJsonArray();

        for (int i = objList.size() - 1; i >= 0; i--){
            JsonObject item = objList.get(i).getAsJsonObject();

            if(!filter.IsMatch(item)){
                objList.remove(i);
            }
        }
        temps.put(structName, new TempStrunct(objList.toString()));
    }

    public void setAcceptHolder(String resultExpression) {
        this.resultExpression = resultExpression;
    }
}
