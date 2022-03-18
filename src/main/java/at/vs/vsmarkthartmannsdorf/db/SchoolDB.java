package at.vs.vsmarkthartmannsdorf.db;

import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class SchoolDB {
    private static SchoolDB instance;

    private ObservableList<Teacher> teachers;
    private ObservableList<SchoolClass> schoolClasses;
    private ObservableList<Timetable> timetables;
    private ObservableList<TeacherSubject> teacherSubjects;

    private List<TeacherAbsence> teacherAbsences;

    private SchoolDB() {
        teachers = FXCollections.observableArrayList();
        schoolClasses = FXCollections.observableArrayList();
        timetables = FXCollections.observableArrayList();
        teacherSubjects = FXCollections.observableArrayList();

        teacherAbsences = new ArrayList<>();

        for (Teacher teacher : teachers) {
            for (Subject subject : teacher.getSubjects()) {
                teacherSubjects.add(new TeacherSubject(teacher, subject));
            }
        }
    }

    public static SchoolDB getInstance() {
        if (instance == null) {
            instance = new SchoolDB();
        }
        return instance;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        for (Subject subject : teacher.getSubjects()) {
            teacherSubjects.add(new TeacherSubject(teacher, subject));
        }
    }

    public void setTeacher(List<Teacher> teachers) {
        this.teachers = FXCollections.observableArrayList();
        this.teacherSubjects = FXCollections.observableArrayList();
        for (Teacher teacher : teachers) {
            addTeacher(teacher);
        }
    }

    public void addSchoolClass(SchoolClass schoolClass) {
        schoolClasses.add(schoolClass);
        addTimetable(new Timetable(schoolClass, Week.A));
    }

    public void removeSchoolClass(SchoolClass schoolClass) {
        schoolClasses.remove(schoolClass);

        Optional<Timetable> timetableToRemove =
                timetables.stream().filter(timetable -> timetable.getSchoolClass().getClassname().equals(schoolClass.getClassname())).findFirst();
        System.out.println(timetableToRemove.get().getSchoolClass().toString());
        timetableToRemove.ifPresent(timetable -> timetables.remove(timetable));
    }

    public void setTimetables(List<Timetable> timetables){
        this.timetables = FXCollections.observableArrayList(timetables);
    }

    private void addTimetable(Timetable timetable) {
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

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = FXCollections.observableArrayList();
        this.schoolClasses.addAll(schoolClasses);
    }

    public Optional<Timetable> findTimetableByClass(SchoolClass schoolClass) {
        return timetables.stream().filter(timetable -> timetable.getSchoolClass().equals(schoolClass)).findFirst();
    }

    public void addSubject(Day day, int hour, Lesson lesson, Timetable timetable) {
        getTimetables().get(getTimetables().indexOf(timetable)).addSubject(day, hour, lesson);
    }

    public List<TeacherSubject> getTeacherBySubject(Subject subject) {
        return SchoolDB.getInstance().getTeacherSubjects().stream().filter(teacherSubject ->
                teacherSubject.getSubject() == subject).collect(Collectors.toList());
    }

    public void switchLessons(Day sourceDay, Day targetDay, int sourceHour, int targetHour, Timetable timetable) {
        Lesson sourceTeacherLesson = timetable.getSubjects().get(sourceDay).get(sourceHour);
        Lesson targetTeacherLesson = timetable.getSubjects().get(targetDay).get(targetHour);

        timetables.stream()
                .filter(t -> t.getSchoolClass()
                        .equals(timetable.getSchoolClass())).findFirst().get().addSubject(sourceDay, sourceHour, targetTeacherLesson);
        timetables.stream()
                .filter(t -> t.getSchoolClass()
                        .equals(timetable.getSchoolClass())).findFirst().get().addSubject(targetDay, targetHour, sourceTeacherLesson);
    }

    public void removeSubject(Day day, int hour, Timetable timetable){
        timetables.stream().filter(t -> t.getSchoolClass().equals(timetable.getSchoolClass())).findFirst().get().removeSubject(day, hour);
    }

    public void addTeacherToLesson(Day day, int hour, Timetable timetable, TeacherSubject teacherSubject){
        if (!timetable.getSubjects().get(day).get(hour).getTeacher().contains(teacherSubject)) {
            timetables.stream().filter(t -> t.getSchoolClass().equals(timetable.getSchoolClass()))
                    .findFirst()
                    .get()
                    .getSubjects()
                    .get(day)
                    .get(hour)
                    .addTeacher(teacherSubject);
        }
    }

    public void removeTeacherFromLesson(Day day, int hour, Timetable timetable, TeacherSubject teacherSubject){
        timetables.stream().filter(t -> t.getSchoolClass().equals(timetable.getSchoolClass()))
                .findFirst()
                .get()
                .getSubjects()
                .get(day)
                .get(hour)
                .removeTeacher(teacherSubject);
    }

    public boolean checkIfTeacherContainsInLesson(Day day, int hour, Timetable timetable, TeacherSubject teacherSubject){
        return timetables.stream()
                .filter(t -> t.getSchoolClass().equals(timetable.getSchoolClass()))
                .findFirst()
                .get()
                .getSubjects()
                .get(day)
                .get(hour)
                .getTeacher()
                .contains(teacherSubject);
    }

    public List<Timetable> getTimetablesFromClass(SchoolClass schoolClass){
        return timetables.stream()
                .filter(t -> t.getSchoolClass().equals(schoolClass)).collect(Collectors.toList());
    }

    public List<Week> getWeeksFromSchoolClass(SchoolClass schoolClass){
        List<Week> weeks = new ArrayList<>();
        for (Timetable timetable: getTimetablesFromClass(schoolClass)){
            weeks.add(timetable.getWeek());
        }
        return weeks;
    }
}
