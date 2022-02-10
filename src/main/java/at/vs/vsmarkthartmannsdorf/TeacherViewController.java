package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TeacherViewController implements Initializable {
    @FXML
    public ListView<Teacher> teacherList;

    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<Teacher> teachers;

    @FXML
    public ImageView ivAdd, ivRemove, ivEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacherList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        teacherList.getSelectionModel().selectFirst();

        // Tooltip on Hover bei ImageViewButtons
        Tooltip.install(ivAdd, new Tooltip("hinzufügen"));
        Tooltip.install(ivRemove, new Tooltip("entfernen"));
        Tooltip.install(ivEdit, new Tooltip("bearbeiten"));
    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    public void setItems(ObservableList<Teacher> teachers) {
        teacherList.setItems(teachers);
        this.teachers = teacherList.getItems();
    }

    @FXML
    public void addTeacher() {
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
        dismountForm();
        ObservableList<Integer> indices = teacherList.getSelectionModel().getSelectedIndices();
        System.out.println(teachers);
        for (int i = indices.size() - 1; i >= 0; i--) {
            parent.removeTeacher(teachers.get(indices.get(i)));
        }

        teacherList.setItems(teachers);
    }

    @FXML
    public void editTeacher(){
        if(teacherList.getSelectionModel().getSelectedIndices().size() != 0){
            dismountForm();
            try {
                FXMLLoader fxmlLoader = parent.fxmlLoad("demo/teacher-form.fxml");
                VBox vBox = fxmlLoader.load();
                ((TeacherFormController) fxmlLoader.getController()).setParent(this);
                ((VBox) root.getCenter()).getChildren().add(vBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dismountForm() {
        ((VBox) root.getCenter()).getChildren().clear();
    }

    public void submitForm(String firstname, String lastname, String abbrevation, ObservableList<Subject> subjects) {
        Teacher teacher = new Teacher(firstname, lastname, abbrevation.toUpperCase(), subjects);

        dismountForm();
        parent.addTeacher(teacher);
        teacherList.setItems(teachers);
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }
}
