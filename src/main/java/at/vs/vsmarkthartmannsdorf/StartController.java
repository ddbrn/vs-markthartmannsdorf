package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
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

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();

    @FXML
    protected void onAddTeacher(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("teacher-dialog.fxml"));
            DialogPane teacherDialog = fxmlLoader.load();

            TeacherController teacherController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(teacherDialog);
            dialog.setTitle("Lehrer hinzufügen");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.APPLY){
                teachers.add(new Teacher(teacherController.getFirstname().getText(),
                        teacherController.getSurname().getText(),
                        teacherController.getAbbreviation().getText(), null));
            }
        }catch(IOException exception){

        }

        teacherList.setItems(teachers);
    }

    @FXML
    protected void onRemoveTeacher(){
        int index = teacherList.getSelectionModel().getSelectedIndex();

        teachers.remove(index);
        teacherList.setItems(teachers);
    }
}