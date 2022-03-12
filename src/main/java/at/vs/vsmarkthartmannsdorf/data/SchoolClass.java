package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor

public class SchoolClass {
    private String classname;
    private Teacher teacher;


    public SchoolClass(String classname, Teacher teacher) {
        this.classname = classname;
        this.teacher = teacher;
    }

    @JsonIgnore
    public String getFormattedTeacher() {
        return teacher.getFirstname() + " " + teacher.getSurname() + " (" + teacher.getAbbreviation() + ")";
    }


    @Override
    public String toString() {
        return String.format("%s, %s", classname.toUpperCase(), teacher.getAbbreviation());
    }

}
