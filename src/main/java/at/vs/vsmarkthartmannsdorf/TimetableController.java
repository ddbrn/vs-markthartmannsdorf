package at.vs.vsmarkthartmannsdorf;

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
//

    public ListView<Teacher> availableTeacher, assignedTeacher;
    private ObservableList<Teacher> olAvailableTeacher,olAssignedTeacher;

    public void setTeacher(List<Teacher> TeacherAvailable){
        olAvailableTeacher = FXCollections.observableArrayList(TeacherAvailable);
        olAssignedTeacher = FXCollections.observableArrayList();

        availableTeacher.setItems(olAvailableTeacher);
        assignedTeacher.setItems(olAssignedTeacher);
    }
    @FXML
    public void addTeacher(){
         Teacher teacher = availableTeacher.getSelectionModel().getSelectedItem();

        if (teacher != null){
            olAvailableTeacher.remove(teacher);
            olAssignedTeacher.add(teacher);
        }

        availableTeacher.setItems(olAvailableTeacher);
        assignedTeacher.setItems(olAssignedTeacher);
    }

    @FXML
    public void removeTeacher(){
        Teacher teacher = assignedTeacher.getSelectionModel().getSelectedItem();

        if (teacher != null){
            olAssignedTeacher.remove(teacher);
            olAvailableTeacher.add(teacher);
        }

        availableTeacher.setItems(olAvailableTeacher);
        assignedTeacher.setItems(olAssignedTeacher);
    }

    public List<Teacher> getAssignedTeacher() {
        return olAssignedTeacher.stream().collect(Collectors.toList());
    }
}
