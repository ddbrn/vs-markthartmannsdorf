package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.util.*;

public class StartController {

    @FXML
    public ListView<Teacher> teacherList;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    @FXML
    protected void onAddTeacher(){
        Teacher teacher = new Teacher("Simon", "Schöggler", "SS", null);

        teachers.add(teacher);

        teacherList.setItems(teachers);
    }

    @FXML
    protected void onRemoveTeacher(){
        int index = teacherList.getSelectionModel().getSelectedIndex();

        teachers.remove(index);
        teacherList.setItems(teachers);
    }
}