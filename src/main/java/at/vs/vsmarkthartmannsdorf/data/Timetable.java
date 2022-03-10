package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    private List<TimetableDay> timetableDays = new ArrayList<>();

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
        timetableDays.add(new TimetableDay(1,  "07:45 - 08:35",
                Subject.Mathematik.toString(), Subject.Deutsch.toString(), Subject.Englisch.toString(), Subject.Sachkunde.toString(), Subject.Musik.toString()));

        timetableDays.add(new TimetableDay(2,  "08:40 - 09:30",
                Subject.Sachkunde.toString(), Subject.Mathematik.toString(), Subject.Musik.toString(), Subject.Deutsch.toString(), Subject.Musik.toString()));

        timetableDays.add(new TimetableDay(3,  "09:50 - 10:40",
                Subject.Musik.toString(), Subject.Deutsch.toString(), Subject.Musik.toString(), Subject.Sachkunde.toString(), Subject.Mathematik.toString()));

        timetableDays.add(new TimetableDay(4,  "10:45 - 11:35",
                Subject.Deutsch.toString(), Subject.Deutsch.toString(), Subject.Sachkunde.toString(), Subject.Deutsch.toString(), Subject.Mathematik.toString()));

        timetableDays.add(new TimetableDay(5,  "11:45 - 12:35",
                Subject.Sachkunde.toString(), Subject.Mathematik.toString(), Subject.Musik.toString(), Subject.Deutsch.toString(), ""));

        timetableDays.add(new TimetableDay(6,  "12:45 - 13:35",
              "", "", "","", ""));


    }
    @JsonIgnore
    public List<TimetableDay> getTimeTableContent() {
        return timetableDays;
    }

}