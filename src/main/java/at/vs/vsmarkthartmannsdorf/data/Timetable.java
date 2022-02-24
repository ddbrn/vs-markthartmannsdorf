package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        int j = 7;
        /*for (int i = 8; i <= 14; i++) {
            j++;
            if (j >= 10) {
                timeTable.add(new TimetableDay(String.valueOf(i), String.valueOf(j) + ":00",
                        Subject.Mathematik.toString(), "", "", Subject.Mathematik.toString(), ""));
            } else {
                timeTable.add(new TimetableDay(String.valueOf(i), "0" + String.valueOf(j) + ":00",
                        Subject.Mathematik.toString(), "", "", "", ""));
            }
        }*/
        timeTable.add(new TimetableDay(String.valueOf(8),  "08:00",
                Subject.Mathematik.toString(), Subject.Deutsch.toString(), Subject.Englisch.toString(), Subject.Sachkunde.toString(), Subject.Musik.toString()));

        timeTable.add(new TimetableDay(String.valueOf(9),  "09:00",
                Subject.Sachkunde.toString(), Subject.Mathematik.toString(), Subject.Musik.toString(), Subject.Deutsch.toString(), Subject.Musik.toString()));

        timeTable.add(new TimetableDay(String.valueOf(9),  "10:00",
                Subject.Musik.toString(), Subject.Deutsch.toString(), Subject.Musik.toString(), Subject.Sachkunde.toString(), Subject.Mathematik.toString()));

        timeTable.add(new TimetableDay(String.valueOf(9),  "11:00",
                Subject.Deutsch.toString(), Subject.Deutsch.toString(), Subject.Sachkunde.toString(), Subject.Deutsch.toString(), Subject.Mathematik.toString()));

        timeTable.add(new TimetableDay(String.valueOf(9),  "12:00",
                Subject.Sachkunde.toString(), Subject.Mathematik.toString(), Subject.Musik.toString(), Subject.Deutsch.toString(), ""));


    }


    @JsonIgnore
    public List<TimetableDay> getTimeTableContent() {
        return timeTable;
    }
}