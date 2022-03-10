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

    @FXML
    public ComboBox<Day> combofDay = new ComboBox<>();
    public ComboBox<Subject> combofSubject = new ComboBox<>();
    public TextField txtHour;
    private TimetableViewController parent;

    private ObservableList<Day> Days;
    private ObservableList<Subject> AvailableSubjects;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDays(Arrays.asList(Day.values()));
        setAvailableSubjects(Arrays.asList(Subject.values()));
    }

    public TimeTableFormController() {
        System.out.println(Day.values());
        setDays(Arrays.asList(Day.values()));
        setAvailableSubjects(Arrays.asList(Subject.values()));
    }
    @FXML
    public void cancelTimetable(){
        parent.getParent().setClassesOpened(false);
        parent.dismountForm();
    }
    @FXML
    public void submitTimetable(){
        if(!(txtHour.getText().isEmpty() || combofDay.getItems().isEmpty())){
            parent.getSchoolClass().getTimetable().getTimeTableContent().get(getTxtHour()).ChangeHour(getSelectedDay().toString(), getSelectedSubject().toString());
            parent.dismountForm();
            parent.updateList();
          //  System.out.println(parent.getSchoolClass(0).getTimetable().getTimeTableContent().get(0).getTuesday());
        }else{
            System.out.println("Alle Felder ausfüllen");
        }

    }
    public void setParent(TimetableViewController parent) {
        this.parent = parent;
    }

    public void setDays(List<Day> days) {
        Days = FXCollections.observableArrayList(days);
        combofDay.setItems(Days);
    }
    public Day getSelectedDay(){
        System.out.println("Gewählter Tag:" + combofDay.getItems().get(combofDay.getSelectionModel().getSelectedIndex()));
        return combofDay.getItems().get(combofDay.getSelectionModel().getSelectedIndex());
    }
    public Subject getSelectedSubject(){
        System.out.println("Neues Fach:" + combofSubject.getItems().get(combofSubject.getSelectionModel().getSelectedIndex()));
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
