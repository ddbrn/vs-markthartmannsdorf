package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class TeacherViewController implements Initializable {
    @FXML
    public ListView<Teacher> teacherList;

    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<Teacher> teachers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacherList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        teacherList.getSelectionModel().selectFirst();
    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    public void setItems(ObservableList<Teacher> teachers){
        teacherList.setItems(teachers);
        this.teachers = teacherList.getItems();
    }

    @FXML
    protected void addTeacher() {
        if (((VBox) root.getCenter()).getChildren().size() == 0){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("demo/teacher-form.fxml"));
                VBox vBox = fxmlLoader.load();
                ((TeacherFormController) fxmlLoader.getController()).setParent(this);
                ((VBox) root.getCenter()).getChildren().add(vBox);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        }


    @FXML
    protected void removeTeacher(){
        ObservableList<Integer> indices = teacherList.getSelectionModel().getSelectedIndices();
        System.out.println(teachers);
        for (int i = indices.size() - 1; i >= 0; i--){
            teachers.remove(teachers.get(indices.get(i)));
        }
        teacherList.setItems(teachers);
    }

    public void dismountForm(){
        ((VBox) root.getCenter()).getChildren().clear();
    }

    public void submitForm(String firstname, String lastname, String abbrevation, ObservableList<Subject> subjects){
        Teacher teacher = new Teacher(firstname, lastname, abbrevation.toUpperCase(), subjects);
        teachers.add(teacher);
        teacherList.setItems(teachers);

        dismountForm();
        parent.setTeachers(teachers);
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }
}
