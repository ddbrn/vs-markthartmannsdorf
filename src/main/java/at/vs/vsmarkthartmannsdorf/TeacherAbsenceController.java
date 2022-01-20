package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherAbsenceController {
    @FXML
    private Label teacherName;
    @FXML
    private Button isPresent;
    @FXML
    private Button isAbsent;

    private TeacherAbsence teacherAbsence;

    @FXML
    private void setIsAbsent(){
        teacherAbsence.setAbsent(true);

        isPresent.setStyle(null);
        isAbsent.setStyle("-fx-background-color: #008000");
    }

    @FXML
    private void setIsPresent () {
        teacherAbsence.setAbsent(false);

        isPresent.setStyle("-fx-background-color: #008000");
        isAbsent.setStyle(null);
    }

    public void setData(TeacherAbsence teacherAbsence){
        this.teacherAbsence = teacherAbsence;

        teacherName.setText(teacherAbsence.getTeacher().getSurname() + " " + teacherAbsence.getTeacher().getFirstname());
        isPresent.setStyle("-fx-background-color: #ff0000");
    }
}
