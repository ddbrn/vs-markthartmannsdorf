package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

import java.util.HashMap;

@Data
public class TeacherTimetable {
    private int teacherID;
    private HashMap<Week, HashMap<Day, HashMap<Integer, TeacherLesson>>> weeklySubjects;
    public static final int MAX_HOURS = 8;

    public TeacherTimetable(int teacherID) {
        this.teacherID = teacherID;

        weeklySubjects = new HashMap<>();
        addWeek(Week.A);
    }

    public void addSubject(Day day, int hour, TeacherLesson teacherLesson, Week week){
        if (!weeklySubjects.containsKey(week)){
            addWeek(week);
        }
        weeklySubjects.get(week).get(day).put(hour, teacherLesson);
    }

    public void addWeek(Week week){
        HashMap<Day, HashMap<Integer, TeacherLesson>> subjects = new HashMap<>();
        for (Day day: Day.values()){
            HashMap<Integer, TeacherLesson> dailySubjects = new HashMap<>();
            for (int i = 1; i <= MAX_HOURS; i++){
                dailySubjects.put(i, new TeacherLesson());
            }
            subjects.put(day, dailySubjects);
        }
        weeklySubjects.put(week, subjects);
    }
}


