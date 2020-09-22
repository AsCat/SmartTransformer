package com.ascat.transformer.input;

import com.ascat.transformer.MappingRule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HttpInput implements Input {

    private String structName;
    private String rawHttpUrl;
    private Map<String, String> rawHttpParas = new HashMap<>();

    private boolean hasDynamicParameters = false;
    private boolean canAcquireData = true;
    private String dynamicNodePath;

    private Map<String, DynamicPara> dynamicParas = new HashMap<>();

    public HttpInput(String structName, String url, Map<String, String> paras) {
        this.structName = structName;
        this.rawHttpUrl = url;
        this.rawHttpParas = paras;
        analysis();
    }

    private void analysis() {
        for (Map.Entry<String, String> entry : rawHttpParas.entrySet()) {
            if(entry.getValue().contains("$")){
                hasDynamicParameters = true;

                if(entry.getValue().contains(".")){
                    canAcquireData = false;
                }

                DynamicPara dynamicPara = new DynamicPara(entry.getKey(), entry.getValue());

                dynamicParas.put(entry.getKey(), dynamicPara);
            }
        }

        System.out.println(structName + " canAcquire = " + canAcquireData);

        // check dynamic paras in the same tail node
        HashSet<String> uniqueFieldPath = new HashSet<>();
        for (Map.Entry<String, DynamicPara> entry : dynamicParas.entrySet()){
            DynamicPara dynamicPara = entry.getValue();

            if(!dynamicPara.getHeadLessPara()){
                uniqueFieldPath.add(dynamicPara.getSrcStructFiledPath());
            }
        }
        if(uniqueFieldPath.size() >= 2){
            throw new RuntimeException("Different Dynamic Parameters Must In Same Json Node");
        }

        if(uniqueFieldPath.size() > 0){
            dynamicNodePath = uniqueFieldPath.iterator().next();
        }
    }

    public boolean canAcquire(){
        return canAcquireData;
    }

    public boolean validateDependency(List<MappingRule> mappingRuleList) {

        for(MappingRule rule : mappingRuleList){
            if(rule.getChildStructName().equals(structName)){
                 if(rule.getParentFieldPath().equals(dynamicNodePath)){
                     for (Map.Entry<String, DynamicPara> entry : dynamicParas.entrySet()){
                         if(entry.getValue().getSrcStructFiledName().equals(rule.getParentFieldName())){
                             return true;
                         }
                     }
                 }
            }
        }

        return false;
    }

    public String getStructName() {
        return structName;
    }
}
