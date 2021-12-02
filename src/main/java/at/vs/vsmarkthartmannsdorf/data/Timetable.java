package at.vs.vsmarkthartmannsdorf.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    SchoolClass classname;
    List<Teacher> tlist ;

    public Timetable(SchoolClass classname) {
        this.classname = classname;
    }

    public void setContent(List<Teacher> tlist){
        this.tlist = tlist;
    }

    public void deleteContent(){
        tlist.clear();
    }

    @Override
    public String toString() {
        return String.format("%s, %s", classname.getClassname().toUpperCase(), classname.getTeacher().toString());
    }

    public String getteachersubjects(){
        List<Subject> subjects = classname.getTeacher().getSubjects();
        return "Liste der F des Lehrers" + subjects.toString();
        /*for(int i = 0;i<tlist.size();i++){
            for(int j = 0;j<tlist.get(i).getSubjects().size();j++){
                if(subjects.contains(tlist.get(i).getSubjects().get(j)))
                {
                    subjects.add(tlist.get(i).getSubjects().get(j));
                }
            }

        }
        return String.valueOf(subjects);*/
    }
}