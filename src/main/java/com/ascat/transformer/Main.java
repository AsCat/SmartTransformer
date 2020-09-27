package com.ascat.transformer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

        // read json from file
        Transformer transformer = new Transformer();

        URI classUri = Main.class.getClassLoader().getResource("test/classList.json").toURI();
        String classJsonData = new String(Files.readAllBytes(Paths.get(classUri)));

        URI teacherUri = Main.class.getClassLoader().getResource("test/teacherList.json").toURI();
        String teacherJsonData = new String(Files.readAllBytes(Paths.get(teacherUri)));

        URI studentUri = Main.class.getClassLoader().getResource("test/studentList.json").toURI();
        String studentJsonData = new String(Files.readAllBytes(Paths.get(studentUri)));


        Map<String, String> classParas = new HashMap<>();
        classParas.put("id", "$runtime_id$");
        transformer.addHttpInput("Class", "http://www.baidu.com/classes", classParas);

//        transformer.addHttpInput("Class", classJsonData)
//        transformer.addJsonInput("Teacher", teacherJsonData);
        transformer.addJsonInput("Student", studentJsonData);

        Map<String, String> teacherParas = new HashMap<>();
//        teacherParas.put("classId", "$Class.id$");
//        teacherParas.put("teacherId", "$Class.teacherId$");
        transformer.addHttpInput("Teacher", "http://www.baidu.com/teachers", teacherParas);


        //transformer.addInvokeInput("Student", "invoke://com.sss.xcc.getStudents({studentId}, 222)");
        //transformer.addHttpInput("Student", "http://www.baidu.com/students/{studentId}", ".content");

        // transformer.addInvokeInput("Student", "HTTP", "invokeId", "id=")


        transformer.addParamsMapping("Class.id", "$param$.id");

        transformer.addRelationMapping("Teacher.id", "Class.teacherId");

        transformer.addResultMapping("result", "Teacher");


        transformer.addMapping("(Class.teacherId=Teacher.id)->Class.teacher");

        transformer.addMapping("(Student.classId=Class.id)->Class.student");

        transformer.addMapping("(Student.teacherId=Teacher.id)->Teacher.student");

        //transformer.addFilter("Teacher.age<40");
        //transformer.addFilter("Student.sex=\"男\"");

        transformer.setAcceptHolder("{\"class\":$Class$}");

        // runtime paras input
        Map<String, Object> runtimeParas = new HashMap<>();
        runtimeParas.put("runtime_id", 1);

        Map<String, DataStruct> output = transformer.process(runtimeParas);


        System.out.println("");
        System.out.println("============ Transformer Result ============");
        System.out.println("[RESULT]");

        for (String key : output.keySet()) {
            System.out.println("----------------------");
            System.out.println(key);
            System.out.println(output.get(key).getData());
        }

        // JsonElement jelement = new JsonParser().parse(jsonLine);
    }
}
