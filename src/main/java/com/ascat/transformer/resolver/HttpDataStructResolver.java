package com.ascat.transformer.resolver;

import com.ascat.transformer.DataStruct;
import com.ascat.transformer.StringUtils;
import com.ascat.transformer.input.StructType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpDataStructResolver {

    private static Logger logger = LoggerFactory.getLogger(HttpDataStructResolver.class.getName());

    public static String process(String structName, String httpUrl, String resultJsonPath,
                                 HashMap<String, String> dynamicParas, Map<String, Object> runtimeParas) {

        String originalUrl = httpUrl;

        HashMap<String, String> lazyResolveParas = new HashMap<>();

        // resolve dynamicParas
        for (Map.Entry<String, String> entry : dynamicParas.entrySet()) {
            if (entry.getKey().startsWith(structName + ".")) {
                String paraKey = entry.getKey().substring(structName.length() + 1);
                String replaceParaKey = "{" + paraKey + "}";
                String paraVal = entry.getValue();

                if (originalUrl.contains(replaceParaKey)) {
                    if (paraVal.contains("{") || paraVal.contains("}")) {
                        // the replace value is an object
                    } else if (paraVal.startsWith("[") && paraVal.endsWith("]")) {
                        lazyResolveParas.put(paraKey, paraVal);
                    } else {
                        originalUrl = originalUrl.replace(replaceParaKey, paraVal);
                    }
                }

            }
        }

        // resolve runtimeParas
        for (Map.Entry<String, Object> entry : runtimeParas.entrySet()) {
            if (entry.getKey().startsWith(structName + ".")) {
                String paraKey = entry.getKey().substring(structName.length() + 1);
                String replaceParaKey = "{" + paraKey + "}";
                String paraVal = entry.getValue().toString();

                if (originalUrl.contains(replaceParaKey)) {
                    if (paraVal.contains("{") || paraVal.contains("}")) {
                        // the replace value is an object
                    } else if (paraVal.startsWith("[") && paraVal.endsWith("]")) {
                        lazyResolveParas.put(paraKey, paraVal);
                    } else {
                        originalUrl = originalUrl.replace(replaceParaKey, paraVal);
                    }
                }
            }
        }


        String resultJsonData = null;

        if(lazyResolveParas.size() > 0){
            // check the para
            logger.debug("lazyResolveParas = {}", lazyResolveParas);

            List<Map<String, String>> resolvedParasList = new ArrayList<>();

            int arrayLength = -1;
            if(lazyResolveParas.size() >= 1){
                // make sure that all the pars array are same length
                for (Map.Entry<String, String> entry : lazyResolveParas.entrySet()){
                    String values = entry.getValue().substring(1, entry.getValue().length() - 1);
                    String[] valuesArray = values.split(",");


                    if(arrayLength < 0){
                        arrayLength = valuesArray.length;

                        // add new item to list
                        for(int i = 0; i < arrayLength; i++){
                            Map<String, String> paraMap = new HashMap<>();
                            paraMap.put(entry.getKey(), valuesArray[i]);
                            resolvedParasList.add(paraMap);
                        }

                    } else {
                        if(valuesArray.length != arrayLength){
                            throw new RuntimeException("Exist Lazy Resolve Paras with different length");
                        }

                        // add new item to map
                        for(int i = 0; i < arrayLength; i++){
                            resolvedParasList.get(i).put(entry.getKey(), valuesArray[i]);
                        }
                    }
                }
            }

            logger.debug("ResolvedParasList = {}", resolvedParasList);

            JsonArray resultJsonArray = new JsonArray();

            for(Map<String, String> paraMap: resolvedParasList){

                String resRequestUrl = resolveUrlWithParas(originalUrl, paraMap);

                String httpResult;
                if(resRequestUrl.contains("{") || resRequestUrl.contains("}")){
                    throw new RuntimeException("Request Url Contains Unresolved Parameters: " + originalUrl);
                }

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(resRequestUrl).build();

                logger.debug("Request URL= {}", resRequestUrl);

                long start_time = System.nanoTime();

                try {
                    Response response = client.newCall(request).execute();

                    if(response.code() != 200){
                        throw new RuntimeException(String.format("Unexpected response status code %s for url %s: ",
                                response.code(), request.url().toString()));
                    }

                    httpResult = response.body().string();

                    if(StringUtils.isNullOrEmpty(httpResult)){
                        throw new RuntimeException(String.format("Unexpected response body %s for url %s: ",
                                response.code(), request.url().toString()));
                    }
                    resultJsonData = getDataFromPath(httpResult, resultJsonPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long end_time = System.nanoTime();
                double difference = (end_time - start_time) / 1e6;
                logger.debug("Request Finished in {} ms", difference);

                JsonElement element = JsonParser.parseString(resultJsonData);
                resultJsonArray.add(element);
            }

            resultJsonData = resultJsonArray.toString();

        } else {

            String httpResult;
            // check url is fill all parameters
            if(originalUrl.contains("{") || originalUrl.contains("}")){
                throw new RuntimeException("Request Url Contains Unresolved Parameters: " + originalUrl);
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(originalUrl).build();

            logger.debug("Request URL= {}", originalUrl);

            long start_time = System.nanoTime();

            try {
                Response response = client.newCall(request).execute();
                if(response.code() != 200){
                    throw new RuntimeException(String.format("Unexpected status response code %s for url %s: ",
                            response.code(), request.url().toString()));
                }
                httpResult = response.body().string();
                resultJsonData = getDataFromPath(httpResult, resultJsonPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long end_time = System.nanoTime();
            double difference = (end_time - start_time) / 1e6;
            logger.debug("Request Finished in {} ms", difference);
        }

        logger.debug("DataStruct {} Resolver Result is {}", structName, resultJsonData);

        return resultJsonData;
    }

    private static String resolveUrlWithParas(String requestUrl, Map<String, String> paraMap) {
        String resultRequestUrl = requestUrl;
        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            String paraKey = entry.getKey().startsWith(".") ? entry.getKey().substring(1): entry.getKey();
            String replaceParaKey = "{" + paraKey + "}";
            String paraVal = entry.getValue();
            resultRequestUrl = resultRequestUrl.replace(replaceParaKey, paraVal);
        }
        return resultRequestUrl;
    }

    public static String getDataFromPath(String structData, String dstJsonPath) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(structData);

        String result = JsonPath.read(document, dstJsonPath).toString();

        return result;
    }
}
