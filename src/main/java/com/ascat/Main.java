package com.ascat;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // write your code here

        Transformer transformer = new Transformer();
        transformer.addJsonInput("Class", "[{\"id\":1,\"teacherId\":2,\"name\":\"小1班\"}, {\"id\":2,\"teacherId\":2,\"name\":\"小2班\"}, {\"id\":3,\"teacherId\":3,\"name\":\"小3班\"}]");
        transformer.addJsonInput("Teacher", "[{\"id\":1,\"name\":\"李老师\",\"age\":24},{\"id\":2,\"name\":\"陈老师\",\"age\":46},{\"id\":3,\"name\":\"王老师\",\"age\":26}]");
        transformer.addJsonInput("Student", "[{\"id\":1,\"name\":\"东东\",\"sex\":\"男\", \"classId\":1},{\"id\":2\",\"name\":\"丁丁\",\"sex\":\"男\", \"classId\":2},{\"id\":3,\"name\":\"琪琪\",\"sex\":\"女\", \"classId\":1}]");

        //transformer.addInvokeInput("Student", "invoke://com.sss.xcc.getStudents({studentId}, 222)");
        //transformer.addHttpInput("Student", "http://www.baidu.com/students/{studentId}", ".content");

        // transformer.addInvokeInput("Student", "HTTP", "invokeId", "id=")

        transformer.addMapping("(Class.teacherId=Teacher.id)->Class.teacher");
        transformer.addMapping("(Student.classId=Class.id)->Class.student");

        //transformer.addFilter("Teacher.age<40");
        //transformer.addFilter("Student.sex=\"男\"");

        transformer.setAcceptHolder("{\"class\":$Class}");

        System.out.println("[RESULT]");
        Map<String, TempStruct> output = transformer.process();
        for (String key : output.keySet()) {
            System.out.println("----------------------");
            System.out.println(key);
            System.out.println(output.get(key).getData());
        }

        // JsonElement jelement = new JsonParser().parse(jsonLine);
    }
}
