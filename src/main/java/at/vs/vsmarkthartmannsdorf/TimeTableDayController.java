package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TimeTableDayController {

    public ListView<Teacher> chosenDay, chosenSubject;
    public TextField chosenHour;
    private ObservableList<Teacher> Days,Hours, AvailableSubjects;


}
