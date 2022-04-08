package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Data
public class TeacherViewController implements Initializable {
    @FXML
    public ListView<Teacher> teacherList;

    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<Teacher> teachers;

    private boolean isEdit;

    @FXML
    public ImageView ivAdd, ivRemove, ivEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacherList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Tooltip on Hover bei ImageViewButtons
        Tooltip.install(ivAdd, new Tooltip("hinzufügen"));
        Tooltip.install(ivRemove, new Tooltip("entfernen"));

        teacherList.setItems(SchoolDB.getInstance().getTeachers());
    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    @FXML
    public void addTeacher() {
        isEdit = false;
        dismountForm();
        if (((VBox) root.getCenter()).getChildren().size() == 0) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("demo/teacher-form.fxml"));
                VBox vBox = fxmlLoader.load();
                ((TeacherFormController) fxmlLoader.getController()).setParent(this);
                ((VBox) root.getCenter()).getChildren().add(vBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void removeTeacher() {
        ObservableList<Integer> indices = teacherList.getSelectionModel().getSelectedIndices();
        boolean singular = false;

        if (indices.size() == 0) {
            return;
        } else if (indices.size() == 1) {
            singular = true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("vs-martkhartmannsdorf | LÖSCHEN");
        if (singular) {
            alert.setHeaderText("Wollen Sie den Lehrer löschen");
        } else {
            alert.setHeaderText("Wollen Sie die Lehrer löschen");
        }
        alert.setContentText("Löschen?");
        ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.getButtonData().equals(ButtonBar.ButtonData.YES)) {

                dismountForm();

                indices.stream().sorted(Comparator.reverseOrder()).forEach(i -> {
                    SchoolDB.getInstance().removeTeacher(teacherList.getItems().get(i));
                });

                updateTeacher();
            }
        });

    }

    @FXML
    public void editTeacher() {
        isEdit = true;
        if (teacherList.getSelectionModel().getSelectedIndices().size() == 1) {
            dismountForm();
            try {
                FXMLLoader fxmlLoader = parent.fxmlLoad("demo/teacher-form.fxml");
                VBox vBox = fxmlLoader.load();
                TeacherFormController controller =  fxmlLoader.getController();
                controller.setParent(this);

                ((VBox) root.getCenter()).getChildren().add(vBox);

                Teacher teacher = teacherList.getSelectionModel().getSelectedItem();
                controller.setItemsIfEdited(teacher);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("vs-martkhartmannsdorf | BEARBEITEN");
            alert.setHeaderText("Bitte nur einen Lehrer zum Bearbeiten auswählen!");
            alert.showAndWait();
        }
    }

    public void dismountForm() {
        ((VBox) root.getCenter()).getChildren().clear();
    }

    public void submitForm(String firstname, String lastname, String abbrevation, List<Subject> subjects) {
        Teacher teacher = new Teacher(SchoolDB.getInstance().getLastTeacherID(),StringUtils.capitalize(firstname), lastname.toUpperCase(), abbrevation.toUpperCase(), subjects);

        dismountForm();

        SchoolDB.getInstance().addTeacher(teacher);
        updateTeacher();
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }

    public void updateTeacher() {
        teacherList.setItems(SchoolDB.getInstance().getTeachers());
        teacherList.refresh();
    }

    public void editTeacher (Teacher oldTeacher, String firstname, String lastname, String abbrevation, List<Subject> subjects) {
        Teacher teacher = new Teacher(oldTeacher.getId(), StringUtils.capitalize(firstname), lastname.toUpperCase(), abbrevation.toUpperCase(), subjects);
        dismountForm();
       /* int index = SchoolDB.getInstance().getTeachers().indexOf(oldTeacher);

        SchoolDB.getInstance().getTeachers().set(index, teacher);

        SchoolDB.getInstance().getTeachers().remove(oldTeacher);*/

        oldTeacher.setFirstname(teacher.getFirstname());
        oldTeacher.setSurname(teacher.getSurname());
        oldTeacher.setAbbreviation(teacher.getAbbreviation());
        oldTeacher.setSubjects(teacher.getSubjects());

        updateTeacher();

    }
}
