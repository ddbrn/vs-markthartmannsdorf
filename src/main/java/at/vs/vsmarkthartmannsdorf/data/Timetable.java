package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Timetable {
    private SchoolClass schoolClass;
    private HashMap<Day, HashMap<Integer, TeacherSubject>> subjects;
    public static final int MAX_HOURS = 8;


    public Timetable(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;

        subjects = new HashMap<>();
        for (Day day: Day.values()) {
            HashMap<Integer, TeacherSubject> subjectsPerDay = new HashMap<>();
            for (int i = 0; i < MAX_HOURS; i++){
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
