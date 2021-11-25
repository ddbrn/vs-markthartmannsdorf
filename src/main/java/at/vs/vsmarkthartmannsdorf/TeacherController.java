package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherController {

    @FXML
    public TextField firstname;
    public TextField surname;
    public TextField abbreviation;
    public ListView<Subject> availableSubjects;
    public ListView<Subject> assignedSubjects;

    private ObservableList<Subject> olAvailableSubjects;
    private ObservableList<Subject> olAssignedSubjects;

    public TextField getFirstname() {
        return firstname;
    }

    public TextField getSurname() {
        return surname;
    }

    public TextField getAbbreviation() {
        return abbreviation;
    }

    public void setSubjects(List<Subject> subjectsAvailable){
        olAvailableSubjects = FXCollections.observableArrayList(subjectsAvailable);
        olAssignedSubjects = FXCollections.observableArrayList();

        availableSubjects.setItems(olAvailableSubjects);
        assignedSubjects.setItems(olAssignedSubjects);
    }

    @FXML
    public void addSubject(){
        Subject subject = availableSubjects.getSelectionModel().getSelectedItem();

        if (subject != null){
            olAvailableSubjects.remove(subject);
            olAssignedSubjects.add(subject);
        }

        availableSubjects.setItems(olAvailableSubjects);
        assignedSubjects.setItems(olAssignedSubjects);
    }

    @FXML
    public void removeSubject(){
        Subject subject = assignedSubjects.getSelectionModel().getSelectedItem();

        if (subject != null){
            olAssignedSubjects.remove(subject);
            olAvailableSubjects.add(subject);
        }

        availableSubjects.setItems(olAvailableSubjects);
        assignedSubjects.setItems(olAssignedSubjects);
    }

    public List<Subject> getAssignedSubjects(){
        return olAssignedSubjects.stream().collect(Collectors.toList());
    }
}
