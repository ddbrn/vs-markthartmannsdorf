package at.vs.vsmarkthartmannsdorf.db;

import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SchoolDB {
    private static SchoolDB instance;

    private ObservableList<Teacher> teachers;
    private ObservableList<SchoolClass> schoolClasses;
    private ObservableList<Timetable_new> timetables;
    private ObservableList<TeacherSubject> teacherSubjects;

    private SchoolDB (){
        teachers = FXCollections.observableArrayList();
        schoolClasses = FXCollections.observableArrayList();
        timetables = FXCollections.observableArrayList();
        teacherSubjects = FXCollections.observableArrayList();

        for (Teacher teacher: teachers){
            for (Subject subject: teacher.getSubjects()){
                teacherSubjects.add(new TeacherSubject(teacher, subject));
            }
        }
    }

    public static SchoolDB getInstance (){
        if (instance == null){
            instance = new SchoolDB();
        }
        return instance;
    }

    public void addTeacher(Teacher teacher){
        teachers.add(teacher);
        for (Subject subject: teacher.getSubjects()){
            teacherSubjects.add(new TeacherSubject(teacher, subject));
        }
    }

    public void setTeacher(List<Teacher> teachers){
        this.teachers = FXCollections.observableArrayList();
        this.teacherSubjects = FXCollections.observableArrayList();
        for (Teacher teacher: teachers){
            addTeacher(teacher);
        }
    }

    public void addSchoolClass(SchoolClass schoolClass){
        schoolClasses.add(schoolClass);

        addTimetable(new Timetable_new(schoolClass,
                Integer.parseInt(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.max_stunden.name()))));
    }

    public void removeSchoolClass(ObservableList<Integer> indices){
        for (int i = indices.size() - 1; i >= 0; i--){
            int finalI = i;
            timetables.remove(getTimetables().stream().filter(timetable_new -> timetable_new.getSchoolClass() == getSchoolClasses().get(finalI)).findFirst());
            schoolClasses.remove(i);
        }
    }

    private void addTimetable(Timetable_new timetable_new){
        timetables.add(timetable_new);
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }

    public ObservableList<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public ObservableList<Timetable_new> getTimetables() {
        return timetables;
    }

    public ObservableList<TeacherSubject> getTeacherSubjects() {
        return teacherSubjects;
    }

    public void removeTeacher(Teacher teacher){
        teachers.remove(teacher);
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = FXCollections.observableArrayList();
        for (SchoolClass schoolClass: schoolClasses){
            addSchoolClass(schoolClass);
        }
    }

}
