package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherFormController implements Initializable {
    @FXML
    public TextField firstname, surname, abbreviation;
    public ListView<Subject> availableSubjects, assignedSubjects;
    public Label info;

    private ObservableList<Subject> olAvailableSubjects;
    private ObservableList<Subject> olAssignedSubjects;

    private TeacherViewController parent;
    private boolean isEdit;

    public TeacherFormController() {
        this.olAvailableSubjects = FXCollections.observableArrayList();;
        this.olAssignedSubjects = FXCollections.observableArrayList();;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olAvailableSubjects.addAll(Subject.values());
        availableSubjects.setItems(olAvailableSubjects);

        info.setVisible(false);
        availableSubjects.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assignedSubjects.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setParent(TeacherViewController parent) {
        this.parent = parent;
    }

    @FXML
    public void addSubject(){
        ObservableList<Integer> indices = availableSubjects.getSelectionModel().getSelectedIndices();
        indices.forEach(i -> olAssignedSubjects.add(olAvailableSubjects.get(i)));

        for(int i = indices.size() - 1; i > -1; i--){
            olAvailableSubjects.remove(olAvailableSubjects.get(indices.get(i)));
        }

        assignedSubjects.setItems(olAssignedSubjects);
        availableSubjects.setItems(olAvailableSubjects);
    }

    @FXML
    public void removeSubject(){
        ObservableList<Integer> indices = assignedSubjects.getSelectionModel().getSelectedIndices();
        indices.forEach(i -> olAvailableSubjects.add(olAssignedSubjects.get(i)));

        for(int i = indices.size() - 1; i > -1; i--){
            olAssignedSubjects.remove(olAssignedSubjects.get(indices.get(i)));
        }

        assignedSubjects.setItems(olAssignedSubjects);
        availableSubjects.setItems(olAvailableSubjects);
    }

    @FXML
    public void cancel(){
        parent.dismountForm();
    }

    @FXML
    public void submit(){
        info.setVisible(false);
        if(!(firstname.getText().isEmpty() || surname.getText().isEmpty() || abbreviation.getText().isEmpty() || olAssignedSubjects.size() == 0)){
            if(parent.getTeachers().stream().noneMatch(teacher -> teacher.getAbbreviation().equalsIgnoreCase(abbreviation.getText()))){
               parent.submitForm(firstname.getText(), surname.getText().toUpperCase(), abbreviation.getText(), olAssignedSubjects);
            }else{
                info.setText("Dieses Kürzel gibt es bereits");
                info.setVisible(true);
            }
        }else{
            info.setText("Bitte füllen Sie alle Felder aus!");
            info.setVisible(true);
        }
    }

    public void setStatus(boolean isEdit){
        this.isEdit = isEdit;
    }
}
