package at.vs.vsmarkthartmannsdorf.data;

import java.util.HashMap;

public class TeacherTimetable {
    private int teacherID;
    private HashMap<Week, HashMap<Day, HashMap<Integer, Subject>>> weeklySubjects;
    public static final int MAX_HOURS = 8;

    public TeacherTimetable(int teacherID) {
        this.teacherID = teacherID;

        weeklySubjects = new HashMap<>();
        addWeek(Week.A);
    }

    public void addSubject(Day day, int hour, Subject subject, Week week){
        if (!weeklySubjects.containsKey(week)){
            addWeek(week);
        }
        weeklySubjects.get(day).get(day).put(hour, subject);
    }

    public void addWeek(Week week){
        HashMap<Day, HashMap<Integer, Subject>> subjects = new HashMap<>();
        for (Day day: Day.values()){
            HashMap<Integer, Subject> dailySubjects = new HashMap<>();
            for (int i = 1; i <= MAX_HOURS; i++){
                dailySubjects.put(i, null);
            }
            subjects.put(day, dailySubjects);
        }
        weeklySubjects.put(week, subjects);
    }
}


