package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

    @Override
    public String toString() {
        return String.format("%s %s (%s)", surname.toUpperCase(), firstname, abbreviation.toUpperCase());
    }
}
