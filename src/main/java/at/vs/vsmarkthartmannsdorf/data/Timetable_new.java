package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Timetable_new {
    private SchoolClass schoolClass;
    private HashMap<Day, HashMap<Integer, TeacherSubject>> subjects;
    private int maxHours;


    public Timetable_new(SchoolClass schoolClass, int max_hours) {
        this.schoolClass = schoolClass;
        this.maxHours = max_hours;

        subjects = new HashMap<>();
        for (Day day: Day.values()) {
            HashMap<Integer, TeacherSubject> subjectsPerDay = new HashMap<>();
            for (int i = 0; i < max_hours; i++){
                subjectsPerDay.put(i + 1, new TeacherSubject(null, null));
            }
            subjects.put(day, subjectsPerDay);
        }
    }

    public void addSubject(Day day, int hour, TeacherSubject teacherSubject){
        subjects.get(day).put(hour, teacherSubject);
    }

    @Override
    public String toString() {
        return schoolClass.getClassname();
    }
}
