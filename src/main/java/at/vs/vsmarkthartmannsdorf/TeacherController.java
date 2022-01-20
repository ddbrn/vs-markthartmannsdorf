package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherController {

    @FXML
    public TextField firstname, surname, abbreviation;
    public ListView<Subject> availableSubjects, assignedSubjects;
    public Label firstnameInfo, surnameInfo, abbreviationInfo, assignedSubjectsInfo;

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

    public void setFirstnameVisibility(boolean v){
        firstnameInfo.setVisible(v);
    }

    public void setSurnameVisibility(boolean v){
        surnameInfo.setVisible(v);
    }

    public void setAbbreviationVisibility(boolean v){
        abbreviationInfo.setVisible(v);
    }

    public void setAssignedSubjectsVisibility(boolean v){
        assignedSubjectsInfo.setVisible(v);
    }

    public void setFirstnameText(String text){
        firstname.setText(text);
    }

    public void setSurnameText(String text){
        surname.setText(text);
    }

    public void setAbbreviationText(String text){
        abbreviation.setText(text);
    }

    public void setAssignedSubjects(List<Subject> subjects){
        ObservableList<Subject> os = FXCollections.observableArrayList(subjects);

        olAvailableSubjects.removeAll(subjects);
        availableSubjects.setItems(olAvailableSubjects);

        olAssignedSubjects.addAll(subjects);
        assignedSubjects.setItems(os);
    }
    public void setAbbreviationLabelText(String text){
        abbreviationInfo.setText(text);
    }
}
