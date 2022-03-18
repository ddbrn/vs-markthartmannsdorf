package at.vs.vsmarkthartmannsdorf.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    private SchoolClass schoolClass;
    private HashMap<Day, HashMap<Integer, Lesson>> subjects;
    public static final int MAX_HOURS = 8;
    private Week week;


    public Timetable(SchoolClass schoolClass, Week week) {
        this.schoolClass = schoolClass;

        subjects = new HashMap<>();
        for (Day day: Day.values()) {
            HashMap<Integer, Lesson> subjectsPerDay = new HashMap<>();
            for (int i = 0; i < MAX_HOURS; i++){
                subjectsPerDay.put(i + 1, new Lesson());
            }
            subjects.put(day, subjectsPerDay);
        }
        this.week = week;
    }

    public Timetable(Timetable timetable){
        this.schoolClass = timetable.getSchoolClass();
        this.subjects = timetable.getSubjects();
        this.week = timetable.getWeek();
    }

    public void addSubject(Day day, int hour, Lesson lesson){
        subjects.get(day).put(hour, lesson);
    }

    @Override
    public String toString() {
        return schoolClass.getClassname();
    }

    public void removeSubject(Day day, int hour){
        subjects.get(day).put(hour, new Lesson());
    }
}
