package com.ascat.transformer.resolver;

import com.ascat.transformer.Main;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileDataStructResolver {
    public static String process(String structName, String fileName, HashMap<String, String> runtimeInputDataMap, Map<String, Object> runtimeParas) {

        String result = null;
        try {
            URI classUri = Main.class.getClassLoader().getResource(fileName).toURI();
            result = new String(Files.readAllBytes(Paths.get(classUri)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
