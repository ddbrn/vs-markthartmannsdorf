package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.databind.util.EnumValues;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    SchoolClass classname;
    List<Teacher> tlist;

    public Timetable(SchoolClass classname) {
        this.classname = classname;
    }

    public void setContent(List<Teacher> tlist) {
        this.tlist = tlist;
    }

    public void deleteContent() {
        tlist.clear();
    }

    @Override
    public String toString() {
        return String.format("%s, %s", classname.getClassname().toUpperCase(), classname.getTeacher().toString());
    }
}