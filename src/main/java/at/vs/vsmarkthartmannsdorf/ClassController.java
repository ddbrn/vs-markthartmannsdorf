package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class ClassController {

    @FXML
    public TextField className;
    public ChoiceBox<Teacher> teachers;
    public Label classLabel;
    public Label teacherLabel;
    public Label classExists;

    public TextField getClassName() {
        return className;
    }

    public void setTeachers(ObservableList<Teacher> olTeachers){
        teachers.setItems(olTeachers);
    }

    public ChoiceBox<Teacher> getTeachers() {
        return teachers;
    }

    public Label getClassLabel() {
        return classLabel;
    }

    public Label getTeacherLabel() {
        return teacherLabel;
    }

    public Label getClassExists() {
        return classExists;
    }
    public void setSelectedTeacher(Teacher teacher){
        teachers.setValue(teacher);
    }
    public void setClassName(String name){
        className.setText(name);
    }
}
