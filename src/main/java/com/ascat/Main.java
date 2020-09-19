package com.ascat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

        // read json from file
        Transformer transformer = new Transformer();

        URI classUri = Main.class.getClassLoader().getResource("test/class.json").toURI();
        String classJsonData = new String(Files.readAllBytes(Paths.get(classUri)));

        URI teacherUri = Main.class.getClassLoader().getResource("test/teacher.json").toURI();
        String teacherJsonData = new String(Files.readAllBytes(Paths.get(teacherUri)));

        URI studentUri = Main.class.getClassLoader().getResource("test/student.json").toURI();
        String studentJsonData = new String(Files.readAllBytes(Paths.get(studentUri)));

        transformer.addJsonInput("Class", classJsonData);
        transformer.addJsonInput("Teacher", teacherJsonData);
        transformer.addJsonInput("Student", studentJsonData);

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
