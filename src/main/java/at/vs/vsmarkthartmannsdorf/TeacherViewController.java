package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
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
            SchoolDB.getInstance().removeTeacher(teacherList.getItems().get(i));
        }

        updateTeacher();
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

        SchoolDB.getInstance().addTeacher(teacher);
        updateTeacher();
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }

    public void updateTeacher(){
        teacherList.setItems(SchoolDB.getInstance().getTeachers());
    }
}
