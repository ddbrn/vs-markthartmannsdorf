package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class ClassController {

    @FXML
    public TextField className;
    public ChoiceBox<Teacher> teachers;

    public TextField getClassName() {
        return className;
    }

    public ChoiceBox<Teacher> getTeachers() {
        return teachers;
    }
}
