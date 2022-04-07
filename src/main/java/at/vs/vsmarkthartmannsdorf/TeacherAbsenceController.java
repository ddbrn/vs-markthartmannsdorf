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
    private Teacher teacher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void setIsAbsent() {
        if (!SchoolDB.getInstance().isTeacherAbsence(teacher)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("demo/absence-dialog.fxml"));
                DialogPane absenceDialog = fxmlLoader.load();

                TeacherAbsenceFormController teacherAbsenceFormController = fxmlLoader.getController();
                teacherAbsenceFormController.setTeacher(teacher);


                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(absenceDialog);
                dialog.setTitle("Abwesenheit");

                Optional<ButtonType> clickedButton = dialog.showAndWait();
                if (clickedButton.get() == ButtonType.APPLY) {
                    iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/cancel.png"))));
                    container.setStyle("-fx-background-color: #b4aeae");

                    LocalDate fromDate = teacherAbsenceFormController.getDPFrom().getValue();
                    LocalDate toDate = teacherAbsenceFormController.getDPTo().getValue();
                    String reason = teacherAbsenceFormController.getTxtReason().getText();

                    SchoolDB.getInstance().setNewTeacherAbsence(new TeacherAbsence(teacher.getId(), fromDate, toDate, reason));
                }


            } catch (IOException exception) {
                exception.printStackTrace();
                System.out.println("Datei nicht gefunden");
            }
        } else {
            SchoolDB.getInstance().removeAbsenceFromTeacher(teacher);
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/checked.png"))));
            container.setStyle("-fx-background-color: #ffffff");
        }

    }

    public void setData(Teacher teacher) {
        this.teacher = teacher;

        lbFirstname.setText(teacher.getFirstname());
        lbSurname.setText(teacher.getSurname());
        if (SchoolDB.getInstance().isTeacherAbsence(teacher)) {
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/cancel.png"))));
            container.setStyle("-fx-background-color: #b4aeae");
        } else {
            iv.setImage(new Image(String.valueOf(getClass().getResource("demo/icons/checked.png"))));
        }
    }

    public void setStartController(MainController mainController) {
        this.parent = mainController;
    }
}
