package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
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
    protected void onAddTeacher(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("teacher-dialog.fxml"));
            DialogPane teacherDialog = fxmlLoader.load();

            TeacherController teacherController = fxmlLoader.getController();

            // Verfügbare Fächer setzen
            List<Subject> subjects = new ArrayList<>();
            subjects.add(Subject.Mathe);
            subjects.add(Subject.Sport);
            teacherController.setSubjects(subjects);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(teacherDialog);
            dialog.setTitle("Lehrer hinzufügen");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.APPLY){
                teachers.add(new Teacher(teacherController.getFirstname().getText(),
                        teacherController.getSurname().getText(),
                        teacherController.getAbbreviation().getText(), teacherController.getAssignedSubjects()));
            }
        }catch(IOException exception){
            System.out.println("Datei nicht vorhanden");
        }

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
    @FXML
    protected void onAddTimetable(){
        Timetable timetable1 = new Timetable("AHIF18", null);

        timetables.add(timetable1);

        timetableList.setItems(timetables);
    }

    @FXML
    protected void onRemoveTimetable(){
        int index = timetableList.getSelectionModel().getSelectedIndex();

        timetables.remove(index);
        timetableList.setItems(timetables);
    }
}