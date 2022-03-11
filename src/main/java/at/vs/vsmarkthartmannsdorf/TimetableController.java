package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class TimetableController {

    public ListView<Subject> availableSubjects, assignedSubjects;
    private ObservableList<Subject> olAvailableSubjects,olAssignedSubjects;

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
