package at.vs.vsmarkthartmannsdorf.bl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import at.vs.vsmarkthartmannsdorf.Main;
import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.Timetable;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import com.fasterxml.jackson.databind.ObjectMapper;

// 25.11.2021 Simon: create "storeClassFiles", "readClassFiles" Functions
// 02.12.2021 Simon: fixed JSON /read and /write
// 04.12.2021 Simon: add "storeTeacherFiles", "readTeacherFiles" Functions
// 09.12.2021 Simon: add "storeTimetableFiles", "readTimetableFiles" Functions

//TODO: IOAccess.class.getClassLoader().getResourceAsStream("");
//TODO: Fix Problem with storage in JAR
public class IOAccess {

    private static final File FILE_CLASS = Paths.get("", "data", "class.json").toFile();
    private static final File FILE_TEACHER = Paths.get("", "data", "teacher.json").toFile();
    private static final File FILE_TIMETABLE = Paths.get("", "data", "timetable.json").toFile();


    public static synchronized boolean storeClassFiles(List<SchoolClass> schoolClassList) {
        try {
            FILE_CLASS.getParentFile().mkdirs();
            ObjectMapper om = new ObjectMapper();
            String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(schoolClassList);

            FileWriter fileWriter = new FileWriter(IOAccess.FILE_CLASS.getAbsolutePath(), StandardCharsets.UTF_8);
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

    public static synchronized List<SchoolClass> readClassFiles() {
        List<SchoolClass> schoolClassList = new ArrayList<>();
        if (!new File(FILE_CLASS.getAbsolutePath()).exists()) {
            return new ArrayList<>();
        }
        try {
            String result = Files.readString(Paths.get(FILE_CLASS.getAbsolutePath()), StandardCharsets.UTF_8);

            if (result.isEmpty()) {
                return new ArrayList<>();
            }

            ObjectMapper om = new ObjectMapper();
            SchoolClass[] schoolClasses = om.readValue(result, SchoolClass[].class);



           Arrays.stream(schoolClasses).forEach(schoolClass -> {
               Optional<Teacher> teacherLink = SchoolDB.getInstance().getTeachers().stream().filter(teacher -> teacher.getAbbreviation().equals(schoolClass.getTeacher().getAbbreviation())).findFirst();

               teacherLink.ifPresent(schoolClass::setTeacher);

            });

            schoolClassList = Arrays.asList(schoolClasses);
            System.out.println("FileWrite read in \"" + IOAccess.FILE_CLASS.getName() + "\".");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolClassList;
    }

    public static synchronized boolean storeTeacherFiles(List<Teacher> teacherList) {
        try {
            FILE_TEACHER.getParentFile().mkdirs();


            ObjectMapper om = new ObjectMapper();
            String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(teacherList);

            FileWriter fileWriter = new FileWriter(IOAccess.FILE_TEACHER.getAbsolutePath(), StandardCharsets.UTF_8);
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

    public static synchronized List<Teacher> readTeacherFiles() {
        List<Teacher> teacherList = new ArrayList<>();
        if (!new File(FILE_TEACHER.getAbsolutePath()).exists()) {
            return new ArrayList<>();
        }
        try {
            String result = Files.readString(Paths.get(FILE_TEACHER.getAbsolutePath()), StandardCharsets.UTF_8);

            if (result.isEmpty()) {
                return new ArrayList<>();
            }

            ObjectMapper om = new ObjectMapper();
            Teacher[] teachers = om.readValue(result, Teacher[].class);

            teacherList = Arrays.asList(teachers);
            System.out.println("FileWrite read in \"" + IOAccess.FILE_TEACHER.getName() + "\".");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    public static synchronized boolean storeTimetableFiles(List<Timetable> timetableList) {
        try {
            FILE_TIMETABLE.getParentFile().mkdirs();

            ObjectMapper om = new ObjectMapper();
            String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(timetableList);

            FileWriter fileWriter = new FileWriter(IOAccess.FILE_TIMETABLE.getAbsolutePath(), StandardCharsets.UTF_8);
            fileWriter.write(jsonStr);
            fileWriter.close();
            System.out.println("FileWrite wrote in \"" + IOAccess.FILE_TIMETABLE.getName() + "\".");

            return true; //successfully wrote data

        } catch (IOException e) {
            System.out.println("Failed to write in the File: \"" + IOAccess.FILE_TIMETABLE.getName() + "\".");
            e.printStackTrace();
            return false; //not successfully wrote data
        }
    }

    public static synchronized List<Timetable> readTimetableFiles() {
        List<Timetable> timetableList = new ArrayList<>();

        if (!new File(FILE_TIMETABLE.getAbsolutePath()).exists()) {
            return new ArrayList<>();
        }
        try {
            String result = Files.readString(Paths.get(FILE_TIMETABLE.getAbsolutePath()), StandardCharsets.UTF_8);

            if (result.isEmpty()) {
                return new ArrayList<>();
            }

            ObjectMapper om = new ObjectMapper();
            Timetable[] timetables = om.readValue(result, Timetable[].class);

            timetableList = Arrays.asList(timetables);
            System.out.println("FileWrite read in \"" + IOAccess.FILE_TIMETABLE.getName() + "\".");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return timetableList;
    }

}
