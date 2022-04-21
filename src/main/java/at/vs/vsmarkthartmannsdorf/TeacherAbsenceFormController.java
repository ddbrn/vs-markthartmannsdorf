package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.Data;

import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

@Data
public class TeacherAbsenceFormController implements Initializable {
    @FXML
    public Label lblName;
    public DatePicker dPFrom;
    public DatePicker dPTo;
    public TextArea txtReason;

    @FXML
    private GridPane gpAbsence;

    private Teacher teacher;

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

    public void setTeacher(Teacher teacher){
        this.teacher = teacher;

        lblName.setText(teacher.getSurname() +
                " " + teacher.getFirstname());
        //txtReason.setText(teacherAbsence.getReason());
        //txtReason.setText(teacherAbsence.getReason());
    }
}
