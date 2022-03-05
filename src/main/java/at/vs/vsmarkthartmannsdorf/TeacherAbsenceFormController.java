package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TeacherAbsenceFormController implements Initializable {
    @FXML
    public Label lblName;
    public DatePicker dPFrom;
    public DatePicker dPTo;

    private TeacherAbsence teacherAbsence;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dPTo.setDayCellFactory(datePicker -> new DateCell(){
            @Override
            public void updateItem(LocalDate localDate, boolean b) {
                super.updateItem(localDate, b);
                LocalDate today = LocalDate.now();

                setDisable(b || localDate.compareTo(today) < 0);
            }
        });
        dPFrom.setDayCellFactory(datePicker -> new DateCell(){
            @Override
            public void updateItem(LocalDate localDate, boolean b) {
                super.updateItem(localDate, b);
                LocalDate today = LocalDate.now();

                setDisable(b || localDate.compareTo(today) < 0);
            }
        });

        dPFrom.setValue(LocalDate.now());
        dPTo.setValue(LocalDate.now());
    }

    public void setTeacher(TeacherAbsence teacherAbsence){
        this.teacherAbsence = teacherAbsence;

        lblName.setText(teacherAbsence.getTeacher().getSurname() + " " + teacherAbsence.getTeacher().getFirstname());
    }
}
