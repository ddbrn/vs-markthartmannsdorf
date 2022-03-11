package at.vs.vsmarkthartmannsdorf.db;

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

        addTeacher(new Teacher("Simon", "Schoeggler", "SS", new ArrayList<>()));
        addSchoolClass(new SchoolClass("4AHIF", teachers.get(0)));
        timetables.add(new Timetable_new(schoolClasses.get(0)));

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

        addTimetable(new Timetable_new(schoolClass));
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
}
