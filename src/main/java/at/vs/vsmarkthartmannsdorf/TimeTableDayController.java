package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Day;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

public class TimeTableDayController {

    public ComboBox<Day> combofDay;
    public ComboBox<Subject> combofSubject;
    public TextField txtHour;
    private ObservableList<Day> Days;
    private ObservableList<Subject> AvailableSubjects;


    public void setDays(List<Day> days) {
        Days = FXCollections.observableArrayList(days);
        combofDay.setItems(Days);
    }
    public Day getSelectedDay(){
        System.out.println(combofDay.getItems().get(combofDay.getSelectionModel().getSelectedIndex()));
        return combofDay.getItems().get(combofDay.getSelectionModel().getSelectedIndex());
    }
    public Subject getSelectedSubject(){
        System.out.println(combofSubject.getItems().get(combofSubject.getSelectionModel().getSelectedIndex()));
        return combofSubject.getItems().get(combofSubject.getSelectionModel().getSelectedIndex());
    }

    public void setAvailableSubjects(List<Subject> availableSubjects) {
        AvailableSubjects = FXCollections.observableArrayList(availableSubjects);
        combofSubject.setItems(AvailableSubjects);
    }

    public Integer getTxtHour() {
        return Integer.parseInt(txtHour.getText())-1;
    }
}
