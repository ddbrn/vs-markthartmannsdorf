package at.vs.vsmarkthartmannsdorf.data;

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
    private List<TimetableDay> timeTable = new ArrayList<>();

    public SchoolClass(String classname, Teacher teacher) {
        this.classname = classname;
        this.teacher = teacher;
        AddTimeTableDefault();
       System.out.println(timeTable.get(1).getId() + " " + timeTable.get(4).getTime());

    }
    private void AddTimeTableDefault(){
        int j = 7;
        for(int i=0; i<=13;i++){
            if(j >=10) {
                timeTable.add(new TimetableDay(String.valueOf(i), String.valueOf(j) + ":00",
                        "", "", "", "", ""));
                j++;
            }else {
                timeTable.add(new TimetableDay(String.valueOf(i), "0" + String.valueOf(j) + ":00",
                        "", "", "", "", ""));
                j++;
            }
        }
    }

    public List<TimetableDay> getTimeTable() {
        return timeTable;
    }

    public String getFormattedTeacher() {
        return teacher.getFirstname() + " " + teacher.getSurname();
    }


    @Override
    public String toString() {
        return String.format("%s, %s", classname.toUpperCase(), teacher.getAbbreviation());
    }

}
