package at.vs.vsmarkthartmannsdorf.db;

import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchoolDB {
    private static SchoolDB instance;

    private ObservableList<Teacher> teachers;
    private ObservableList<SchoolClass> schoolClasses;
    private ObservableList<Timetable> timetables;
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

        addTimetable(new Timetable(schoolClass));
    }

    public void removeSchoolClass(ObservableList<Integer> indices){
        for (int i = indices.size() - 1; i >= 0; i--){
            int finalI = i;
            timetables.remove(getTimetables().stream().filter(timetable -> timetable.getSchoolClass() == getSchoolClasses().get(finalI)).findFirst());
            schoolClasses.remove(i);
        }
    }

    private void addTimetable(Timetable timetable){
        timetables.add(timetable);
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }

    public ObservableList<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public ObservableList<Timetable> getTimetables() {
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

    public Optional<Timetable> findTimetableByClass(SchoolClass schoolClass){
        return timetables.stream().filter(timetable -> timetable.getSchoolClass().equals(schoolClass)).findFirst();
    }

    public void addSubject(Day day, int hour, TeacherSubject teacherSubject, Timetable timetable){
        getTimetables().get(getTimetables().indexOf(timetable)).addSubject(day, hour, new Lesson(teacherSubject.getSubject(), Arrays.asList(teacherSubject)));
    }

    public List<TeacherSubject> getTeacherBySubject(Subject subject){
        return SchoolDB.getInstance().getTeacherSubjects().stream().filter(teacherSubject ->
                teacherSubject.getSubject() == subject).collect(Collectors.toList());
    }

    public void switchLessons(Day sourceDay, Day targetDay, int sourceHour, int targetHour, Timetable timetable){
        Lesson sourceTeacherLesson = timetable.getSubjects().get(sourceDay).get(sourceHour);
        Lesson targetTeacherLesson = timetable.getSubjects().get(targetDay).get(targetHour);

        timetables.stream()
                .filter(timetable_new -> timetable_new.getSchoolClass()
                        .equals(timetable_new.getSchoolClass())).findFirst().get().addSubject(sourceDay, sourceHour, targetTeacherLesson);
        timetables.stream()
                .filter(timetable_new -> timetable_new.getSchoolClass()
                        .equals(timetable_new.getSchoolClass())).findFirst().get().addSubject(targetDay, targetHour, sourceTeacherLesson);
    }
}
