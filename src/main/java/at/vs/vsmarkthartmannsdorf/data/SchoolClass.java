package at.vs.vsmarkthartmannsdorf.data;

import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass {
    private int id;
    private String classname;
    private int teacherID;

    @JsonIgnore
    public String getFormattedTeacher() {
        Optional<Teacher> teacher = SchoolDB.getInstance().getTeacherByID(teacherID);
        if (teacher.isPresent()){
            return teacher.get().getFirstname() + " " + teacher.get().getSurname() + " (" + teacher.get().getAbbreviation() + ")";
        }
        return "Kein Lehrer gefunden";
    }


    @Override
    public String toString() {
        Optional<Teacher> teacher = SchoolDB.getInstance().getTeacherByID(teacherID);
        String teacherAbbreviation = "";
        if (teacher.isPresent()){
            teacherAbbreviation = teacher.get().getAbbreviation();
        }
        return String.format("%s, %s", classname.toUpperCase(), teacherAbbreviation);
    }
}
