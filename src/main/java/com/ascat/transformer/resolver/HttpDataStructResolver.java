package com.ascat.transformer.resolver;

import com.ascat.transformer.DataStruct;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpDataStructResolver {

    public static String process(String structName, String httpUrl, String resultJsonPath,
                                 HashMap<String, String> runtimeInputDataMap, Map<String, Object> runtimeParas) {

        String requestUrl = httpUrl;

        for (Map.Entry<String, Object> entry : runtimeParas.entrySet()) {
            if(entry.getKey().startsWith(structName + ".")){
                String paras = entry.getKey().substring(structName.length() + 1);
                requestUrl = requestUrl.replace("{" + paras + "}", entry.getValue().toString());
            }
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        String result = null;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
