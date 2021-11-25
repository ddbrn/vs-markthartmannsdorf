package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
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
    public ListView<SchoolClass> classList;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<SchoolClass> classes = FXCollections.observableArrayList();

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

    @FXML
    protected void onAddClass(){
        SchoolClass class1 = new SchoolClass("AHIF18", teachers.get(0));

        classes.add(class1);

        classList.setItems(classes);
    }

    @FXML
    protected void onRemoveClass(){
        int index = classList.getSelectionModel().getSelectedIndex();

        classes.remove(index);
        classList.setItems(classes);
    }
}