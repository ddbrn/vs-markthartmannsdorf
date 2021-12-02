package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.Timetable;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.util.*;

public class StartController {

    @FXML
    public ListView<Teacher> teacherList;
    public ListView<SchoolClass> classList;
    public ListView<Timetable> timetableList;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<SchoolClass> classes = FXCollections.observableArrayList();
    private ObservableList<Timetable> timetables = FXCollections.observableArrayList();

    @FXML
    protected void onAddTeacher() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("teacher-dialog.fxml"));
            DialogPane teacherDialog = fxmlLoader.load();

            TeacherController teacherController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(teacherDialog);
            dialog.setTitle("Lehrer hinzufügen");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.APPLY) {
                teachers.add(new Teacher(teacherController.getFirstname().getText(),
                        teacherController.getSurname().getText(),
                        teacherController.getAbbreviation().getText(), null));
            }
        } catch (IOException exception) {

        }

        teacherList.setItems(teachers);
    }

    @FXML
    protected void onRemoveTeacher() {
        int index = teacherList.getSelectionModel().getSelectedIndex();

        teachers.remove(index);
        teacherList.setItems(teachers);
    }

    @FXML
    protected void onAddClass() {
        try {
            FXMLLoader classLoader = new FXMLLoader();
            classLoader.setLocation(getClass().getResource("class-dialog.fxml"));

            DialogPane classPane = classLoader.load();
            ClassController classController = classLoader.getController();


            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(classPane);
            dialog.setTitle("Klasse hinzufügen");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.get() == ButtonType.APPLY){
                classes.add(new SchoolClass(classController.getClassName().getText(),
                       teachers.get(0)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        classList.setItems(classes);
    }

    @FXML
    protected void onRemoveClass() {
        int index = classList.getSelectionModel().getSelectedIndex();

        classes.remove(index);
        classList.setItems(classes);
    }

    @FXML
    protected void onAddTimetable() {
        Timetable timetable1 = new Timetable("AHIF18", null);

        timetables.add(timetable1);

        timetableList.setItems(timetables);
    }

    @FXML
    protected void onRemoveTimetable() {
        int index = timetableList.getSelectionModel().getSelectedIndex();

        timetables.remove(index);
        timetableList.setItems(timetables);
    }
}