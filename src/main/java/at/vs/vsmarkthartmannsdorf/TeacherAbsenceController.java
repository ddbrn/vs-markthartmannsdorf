package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherAbsenceController implements Initializable {
    @FXML
    private Label lbSurname;
    @FXML
    private Label lbFirstname;
    @FXML
    private ImageView iv;
    @FXML
    private HBox container;

    private MainController parent;
    private TeacherAbsence teacherAbsence;
    private boolean isAbsent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isAbsent = false;
    }

    @FXML
    private void setIsAbsent() {
        if (!isAbsent){
            isAbsent = true;
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/cancel.png"))));
            container.setStyle("-fx-background-color: #b4aeae");
        }else{
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/checked.png"))));
            isAbsent = false;
            container.setStyle("-fx-background-color: #ffffff");
        }
        teacherAbsence.setAbsent(isAbsent ? true : false);
        parent.teacherChangedAbsentStatus(teacherAbsence);
    }

    public void setData(TeacherAbsence teacherAbsence) {
        this.teacherAbsence = teacherAbsence;

        lbFirstname.setText(teacherAbsence.getTeacher().getFirstname());
        lbSurname.setText(teacherAbsence.getTeacher().getSurname());
        if(teacherAbsence.isAbsent()){
            isAbsent = true;
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/cancel.png"))));
            container.setStyle("-fx-background-color: #b4aeae");
        }else{
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/checked.png"))));
            isAbsent = false;
        }
    }

    public void setStartController(MainController mainController) {
        this.parent = mainController;
    }
}
