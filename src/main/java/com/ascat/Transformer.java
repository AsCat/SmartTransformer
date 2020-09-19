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

    private Map<String, TempStruct> temps = new HashMap<>();

    private String resultExpression;

    public void addJsonInput(String structName, String structValue) {
        this.inputs.put(structName, new JsonInput(structValue));
        this.temps.put(structName, new TempStruct(structValue));
    }

    public void addFilter(String filterExpression) {
        this.filters.add(new JsonFilter(filterExpression));
    }

    public void addMapping(String mapping) {
        this.mappings.add(new JsonMapping(mapping));
    }

    public Map<String, TempStruct> process() {

        this.mappings = new DependencyParser().sort(this.mappings);

        // apply filter
        for (int i = 0; i < this.filters.size(); i++) {
            applyFilter(temps, this.filters.get(i));
        }

        // apply mapping
        for (int i = this.mappings.size() - 1; i >= 0; i--) {
            applyMapping(temps, this.mappings.get(i));
        }

        // apply result expression
        for (String key : temps.keySet()) {
            resultExpression = resultExpression.replace("$" + key, temps.get(key).getData());
        }
        temps.put("$RESULT", new TempStruct(resultExpression));

        return temps;
    }

    private void applyMapping(Map<String, TempStruct> temps, JsonMapping jsonMapping) {

        System.out.println(" process apply mapping with expression: " + jsonMapping.getExpression());
        String parentStructName = jsonMapping.getParentStructName();
        String childStructName = jsonMapping.getChildStructName();
        String dstFieldName = jsonMapping.getDstFiledName();

        JsonArray parentList = JsonParser.parseString(temps.get(parentStructName).getData()).getAsJsonArray();
        JsonArray childList = JsonParser.parseString(temps.get(childStructName).getData()).getAsJsonArray();

        for (int i = 0; i < parentList.size(); i++) {
            JsonObject parentItem = parentList.get(i).getAsJsonObject();
            JsonElement parentCheckVal = parentItem.get(jsonMapping.getParentFieldName());

            JsonArray insertArray = new JsonArray();
            for (int j = 0; j < childList.size(); j++) {
                JsonObject childItem = childList.get(j).getAsJsonObject();
                JsonElement childCheckVal = childItem.get(jsonMapping.getChildFieldName());

                // fixme
                if (childCheckVal != null && parentCheckVal != null && childCheckVal.toString().equals(parentCheckVal.toString())) {
                    System.out.println("Match Child Item");

                    insertArray.add(childItem);
                }
            }
            if(insertArray.size() == 0){
              // do nothing
            } else if(insertArray.size() == 1){
                parentItem.add(dstFieldName, insertArray.get(0));
            } else{
                parentItem.add(dstFieldName + "List", insertArray);
            }
        }

        temps.put(parentStructName, new TempStruct(parentList.toString()));
    }


    private void applyFilter(Map<String, TempStruct> temps, JsonFilter filter) {

        String structName = filter.getStructName();
        String jsonData = temps.get(structName).getData();

        JsonArray objList = JsonParser.parseString(jsonData).getAsJsonArray();

        for (int i = objList.size() - 1; i >= 0; i--) {
            JsonObject item = objList.get(i).getAsJsonObject();

            if (!filter.IsMatch(item)) {
                objList.remove(i);
            }
        }
        temps.put(structName, new TempStruct(objList.toString()));
    }

    public void setAcceptHolder(String resultExpression) {
        this.resultExpression = resultExpression;
    }

    public void addInvokeInput(String student, String invokeExpression) {

    }

    public void addHttpInput(String student, String s, String s1) {

    }
}
