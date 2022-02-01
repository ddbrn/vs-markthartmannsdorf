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
    private List<TimetableDay> timeTable = new ArrayList<>();

    public void AddTimeTableDefault() {//Parameter Amount of Hours per Day
        int j = 6;
        for (int i = 7; i <= 13; i++) {
            j++;
            if (j >= 10) {
                timeTable.add(new TimetableDay(String.valueOf(i), String.valueOf(j) + ":00",
                        Subject.Mathematik.toString(), "", "", Subject.Mathematik.toString(), ""));
            } else {
                timeTable.add(new TimetableDay(String.valueOf(i), "0" + String.valueOf(j) + ":00",
                        Subject.Mathematik.toString(), "", "", "", ""));
            }
        }
        //Output Data
        System.out.println(timeTable.get(1).getId() + " " + timeTable.get(4).getTime());
    }

    public List<TimetableDay> getTimeTableContent() {
        return timeTable;
    }
}