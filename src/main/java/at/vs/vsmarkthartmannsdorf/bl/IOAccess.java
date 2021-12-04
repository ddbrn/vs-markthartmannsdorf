package at.vs.vsmarkthartmannsdorf.bl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;

// 25.11.2021 Simon: create "storeClassFiles", "readClassFiles" Functions
// 02.12.2021 Simon: fixed JSON /read and /write
// 04.12.2021 Simon: add "storeTeacherFiles", "readTeacherFiles" Functions
public class IOAccess {

    private static File FILE_CLASS = Paths.get(System.getProperty("user.dir"), "src", "main", "resources","class.json").toFile();
    private static File FILE_TEACHER = Paths.get(System.getProperty("user.dir"), "src", "main", "resources","teacher.json").toFile();

    public static boolean storeClassFiles(List<SchoolClass> schoolClassList) {
        try {

            ObjectMapper om = new ObjectMapper();
            String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(schoolClassList);

            FileWriter fileWriter = new FileWriter(IOAccess.FILE_CLASS.getAbsolutePath());
            fileWriter.write(jsonStr);
            fileWriter.close();
            System.out.println("FileWrite wrote in \"" + IOAccess.FILE_CLASS.getName() + "\".");

            return true; //successfully wrote data

        } catch (IOException e) {
            System.out.println("Failed to write in the File: \"" + IOAccess.FILE_CLASS.getName() + "\".");
            e.printStackTrace();
            return false; //not successfully wrote data
        }
    }

    public static List<SchoolClass> readClassFiles() {
        List<SchoolClass> schoolClassList = new ArrayList<>();
        if (!new File(FILE_CLASS.getAbsolutePath()).exists()) {
            return new ArrayList<>();
        }
        try  {
            String result = Files.readString(Paths.get(FILE_CLASS.getAbsolutePath()));

            ObjectMapper om = new ObjectMapper();
            SchoolClass[] schoolClasses = om.readValue(result, SchoolClass[].class);

            schoolClassList = Arrays.asList(schoolClasses);
            System.out.println("FileWrite read in \"" + IOAccess.FILE_CLASS.getName() + "\".");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolClassList;
    }

    public static boolean storeTeacherFiles(List<Teacher> teacherList) {
        try {

            ObjectMapper om = new ObjectMapper();
            String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(teacherList);

            FileWriter fileWriter = new FileWriter(IOAccess.FILE_TEACHER.getAbsolutePath());
            fileWriter.write(jsonStr);
            fileWriter.close();
            System.out.println("FileWrite wrote in \"" + IOAccess.FILE_TEACHER.getName() + "\".");

            return true; //successfully wrote data

        } catch (IOException e) {
            System.out.println("Failed to write in the File: \"" + IOAccess.FILE_TEACHER.getName() + "\".");
            e.printStackTrace();
            return false; //not successfully wrote data
        }
    }

    public static List<Teacher> readTeacherFiles() {
        List<Teacher> teacherList = new ArrayList<>();
        if (!new File(FILE_TEACHER.getAbsolutePath()).exists()) {
            return new ArrayList<>();
        }
        try  {
            String result = Files.readString(Paths.get(FILE_TEACHER.getAbsolutePath()));

            ObjectMapper om = new ObjectMapper();
            Teacher[] teachers = om.readValue(result, Teacher[].class);

            teacherList = Arrays.asList(teachers);
            System.out.println("FileWrite read in \"" + IOAccess.FILE_TEACHER.getName() + "\".");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return teacherList;
    }

}
