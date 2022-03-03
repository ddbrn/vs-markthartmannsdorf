package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Day;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TimeTableFormController implements Initializable {
    public ComboBox<Day> combofDay = new ComboBox<Day>();
    public ComboBox<Subject> combofSubject = new ComboBox<Subject>();
    public TextField txtHour;
    private ObservableList<Day> Days;
    private ObservableList<Subject> AvailableSubjects;


    public TimeTableFormController() {
        setDays(Arrays.asList(Day.values()));
        setAvailableSubjects(Arrays.asList(Subject.values()));
    }


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
