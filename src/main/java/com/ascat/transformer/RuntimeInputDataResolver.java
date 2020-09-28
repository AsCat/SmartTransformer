package com.ascat.transformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeInputDataResolver {
    public static HashMap<String, String> process(HashMap<String, DataStruct> parentDataStructMap, HashMap<String, List<RelationMappingRule>> parentRelationRules) {
        HashMap<String, String> resultData = new HashMap<>();

        // todo

        for (Map.Entry<String, List<RelationMappingRule>> entry : parentRelationRules.entrySet()) {

            String structName = entry.getKey();
            List<RelationMappingRule> ruleList = entry.getValue();

            DataStruct parentStruct = parentDataStructMap.get(structName);

            for (RelationMappingRule rule: ruleList) {
                String dstJsonPath = rule.getChildFieldPath() + "." + rule.getChildFieldName();
                String srcJsonPath = rule.getParentFieldPath() + "." + rule.getParentFieldName();
                String resultValue = parentStruct.getDataFromPath(srcJsonPath);
                resultData.put(rule.getChildStructName() + dstJsonPath, resultValue);
            }
        }

        return resultData;
    }
}
