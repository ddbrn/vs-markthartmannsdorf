package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private String firstname;
    private String surname;
    private String abbreviation;
    private List<Subject> subjects;

    public String getSubjectsForExcel () {
        String sub = "";

        for (Subject subject:subjects) {
            sub += subject.name() + ";";
        }

        return sub.substring(0, sub.length() - 1);
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", surname.toUpperCase(), firstname, abbreviation.toUpperCase());
    }
}
