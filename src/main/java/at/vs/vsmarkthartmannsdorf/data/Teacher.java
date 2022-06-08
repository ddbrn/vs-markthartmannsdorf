package at.vs.vsmarkthartmannsdorf.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private int id;
    private String firstname;
    private String surname;
    private String abbreviation;
    private List<Subjectobject> subjects;

    @Override
    public String toString() {
        return String.format("%s %s (%s)", surname.toUpperCase(), firstname, abbreviation.toUpperCase());
    }
}
