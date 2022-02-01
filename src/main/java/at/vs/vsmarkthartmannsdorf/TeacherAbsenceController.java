package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

public class TeacherAbsenceController {
    @FXML
    private Label teacherName;
    @FXML
    private RadioButton isAbsent;
    @FXML
    private VBox pane;

    private MainController parent;
    private TeacherAbsence teacherAbsence;

    @FXML
    private void setIsAbsent() {
        pane.setStyle("-fx-background-color: #dde2eb");
        teacherAbsence.setAbsent(isAbsent.isSelected() ? true : false);
        if (!isAbsent.isSelected()){
            pane.setStyle("-fx-background-color: #ff0000");
        }
        parent.teacherChangedAbsentStatus(teacherAbsence);
    }

    public void setData(TeacherAbsence teacherAbsence) {
        this.teacherAbsence = teacherAbsence;

        teacherName.setText(teacherAbsence.getTeacher().getSurname() + " " + teacherAbsence.getTeacher().getFirstname());
        // isPresent.setStyle("-fx-background-color: #008000");
    }

    public void setStartController(MainController mainController) {
        this.parent = mainController;
    }
}
