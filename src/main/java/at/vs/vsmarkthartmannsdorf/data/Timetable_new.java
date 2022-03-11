package at.vs.vsmarkthartmannsdorf.data;

import at.vs.vsmarkthartmannsdorf.SettingsController;
import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Timetable_new {
    private SchoolClass schoolClass;
    private HashMap<Day, List<TeacherSubject>> subjects;


    public Timetable_new(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;

        subjects = new HashMap<>();
        for (Day day: Day.values()) {
            ArrayList<TeacherSubject> subjectsPerDay = new ArrayList<>();
            for (int i = 0; i < SettingsController.MAX_STUNDEN; i++){
                subjectsPerDay.add(new TeacherSubject(null, null));
            }
            subjects.put(day, subjectsPerDay);
        }
    }

    public void addSubject(Day day, int hour, TeacherSubject teacherSubject){
        subjects.get(day).remove(hour);
        subjects.get(day).add(hour, teacherSubject);
    }

    @Override
    public String toString() {
        return schoolClass.getClassname();
    }
}
