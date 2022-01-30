package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class TimeTableDayController {

    public ListView<Teacher> chosenDay, chosenHour, chosenSubject;
    private ObservableList<Teacher> Days,Hours, AvailableSubjects;
}
