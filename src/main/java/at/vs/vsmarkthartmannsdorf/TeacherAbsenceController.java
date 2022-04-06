package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

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
        if (!isAbsent) {
            isAbsent = true;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("demo/absence-dialog.fxml"));
                DialogPane absenceDialog = fxmlLoader.load();

                TeacherAbsenceFormController teacherAbsenceFormController = fxmlLoader.getController();
                teacherAbsenceFormController.setTeacher(teacherAbsence);


                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(absenceDialog);
                dialog.setTitle("Abwesenheit");

                Optional<ButtonType> clickedButton = dialog.showAndWait();
                if (clickedButton.get() == ButtonType.APPLY){
                    iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/cancel.png"))));
                    container.setStyle("-fx-background-color: #b4aeae");

                    LocalDate fromDate = teacherAbsenceFormController.getDPFrom().getValue();
                    LocalDate toDate = teacherAbsenceFormController.getDPTo().getValue();
                    String reason = teacherAbsenceFormController.getTxtReason().getText();

                    teacherAbsence.setFromDate(fromDate);
                    teacherAbsence.setToDate(toDate);
                    teacherAbsence.setReason(reason);


                    System.out.println(teacherAbsence);
                }


            } catch (IOException exception) {
                exception.printStackTrace();
                System.out.println("Datei nicht gefunden");
            }
        } else {
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/checked.png"))));
            isAbsent = false;
            container.setStyle("-fx-background-color: #ffffff");
        }
        teacherAbsence.setAbsent(isAbsent);
        parent.teacherChangedAbsentStatus(teacherAbsence);

    }

    public void setData(TeacherAbsence teacherAbsence) {
        this.teacherAbsence = teacherAbsence;

        lbFirstname.setText(teacherAbsence.getTeacher().getFirstname());
        lbSurname.setText(teacherAbsence.getTeacher().getSurname());
        if (teacherAbsence.isAbsent()) {
            isAbsent = true;
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/cancel.png"))));
            container.setStyle("-fx-background-color: #b4aeae");
        } else {
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/checked.png"))));
            isAbsent = false;
        }
    }

    public void setStartController(MainController mainController) {
        this.parent = mainController;
    }
}
