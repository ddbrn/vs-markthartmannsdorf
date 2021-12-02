package at.vs.vsmarkthartmannsdorf.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClass {
    private String classname;
    private Teacher teacher;


    @Override
    public String toString() {
        return String.format("%s, %s", classname.toUpperCase(), teacher.toString());
    }
}
