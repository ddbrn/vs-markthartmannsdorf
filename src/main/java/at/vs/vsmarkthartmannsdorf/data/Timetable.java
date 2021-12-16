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
    List<Subject> subjects = new ArrayList<>();


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

    public List<Subject> getteachersubjects() {


        tlist.forEach((n) -> {
            n.getSubjects().forEach((e) -> {
                if (!(e == null)) {
                    if (!subjects.contains(Subject.valueOf(e.toString()))) {
                        System.out.println(e.toString());
                      subjects.add(Subject.valueOf(e.toString()));
                    }
                }

            });
        });

       /* for(int i = 0;i<tlist.size()-1;i++){
            System.out.println(tlist.get(1).getSubjects().size()-1);
            for(int j = 0;j<tlist.get(i).getSubjects().size();j++){
                    System.out.println(tlist.get(i).getSubjects().get(j));
            }
    }*/
        return subjects;
    }
}