package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClassFormController implements Initializable {

    @FXML
    public TextField classname;
    public ChoiceBox<Teacher> classTeacher;
    public Label info;

    private ObservableList<Teacher> teachers;

    private ClassViewController parent;

    public ClassFormController() {
        this.teachers = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        info.setVisible(false);
    }

    public void setParent(ClassViewController parent) {
        this.parent = parent;
        teachers.addAll(this.parent.getParent().getTeacher());
        classTeacher.setItems(teachers);
    }
    @FXML
    public void cancelClasses(){
        parent.dismountForm();
    }

    @FXML
    public void submitClasses(){
        info.setVisible(false);
        if(!(classname.getText().isEmpty() || classTeacher.getValue() == null)){
            if(parent.getClasses().stream().noneMatch(klasse -> klasse.getClassname().equalsIgnoreCase(classname.getText()))){
                parent.submitForm(classname.getText(), classTeacher.getValue());
            }else{
                info.setText("Dieses Klasse existiert bereits!");
                info.setVisible(true);
            }
        }else{
            info.setText("Bitte alle Felder ausfüllen!");
            info.setVisible(true);
        }

    }

}
