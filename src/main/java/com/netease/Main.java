package com.netease;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
	// write your code here

        Transformer transformer = new Transformer();
        transformer.addInput("A", "[{a1:1,a2:2,a3:3}, {a1:2,a2:2,a3:3}, {a1:3,a2:2,a3:3}]");
        transformer.addInput("B", "[{b1:2,b2:2,b3:4}]");
        transformer.addInput("C", "[{c1:4,c2:2,c3:5}]");

        transformer.addMapping("(B.b3=C.c1)->B.c4");
        transformer.addMapping("(A.a2=B.b1)->A.a4");
        transformer.addMapping("(B.b3=C.c1)->B.c6");
        transformer.addMapping("(B.b3=C.c1)->B.c5");

        transformer.addFilter("A.a1>2");

        transformer.setAcceptHolder("{a:$A,b:$B,c:$C}");

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println("[RESULT]");
        Map<String, TempStrunct> output = transformer.process();
        for (String key : output.keySet()) {
            System.out.println("----------------------");
            System.out.println(key);
            System.out.println(output.get(key).getData());
        }

//        JsonElement jelement = new JsonParser().parse(jsonLine);
    }
}
