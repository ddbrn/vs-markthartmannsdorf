package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class ClassFormController implements Initializable {

    @FXML
    public ChoiceBox<Teacher> classTeacher;
    public ChoiceBox<Integer> inputYear;
    public ChoiceBox<Character> inputClass;
    public Label info;

    private ObservableList<Teacher> teachers;
    private final ObservableList<Integer> schoolYears;
    private final ObservableList<Character> schoolClass;

    private ClassViewController parent;
    private SchoolClass oldSchoolClass;

    public ClassFormController() {
        this.teachers = FXCollections.observableArrayList();
        this.schoolYears = FXCollections.observableArrayList();
        this.schoolClass = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        info.setVisible(false);

        IntStream.rangeClosed(1, 4).forEach(schoolYears::add);
        IntStream.rangeClosed(0, 3).forEach(i -> schoolClass.add(((char) ('A' + i))));
        inputYear.setItems(schoolYears);
        inputClass.setItems(schoolClass);
    }

    public void setItemsIfEdited(SchoolClass schoolClass) {
        classTeacher.getSelectionModel().select(schoolClass.getTeacher());
        inputYear.getSelectionModel().select(Integer.valueOf(schoolClass.getClassname().substring(0, 1)));
        inputClass.getSelectionModel().select(Character.valueOf(schoolClass.getClassname().charAt(1)));

        oldSchoolClass = schoolClass;
    }

    public void setParent(ClassViewController parent) {
        this.parent = parent;
        refreshItems();
    }

    @FXML
    public void cancelClasses() {
        parent.getParent().setClassesOpened(false);
        parent.dismountForm();
    }


    @FXML
    public void submitClasses() {
        String classname = "" + inputYear.getValue() + "" + inputClass.getValue();
        info.setVisible(false);
        if (parent.isEdit()) {
            if (!(classname.isBlank() || classTeacher.getValue() == null || inputYear.getValue() == null || inputClass.getValue() == null)) {
                System.out.println(SchoolDB.getInstance().getSchoolClasses().stream().filter(schoolClass1 -> !schoolClass1.equals(oldSchoolClass)).toList());
                if (SchoolDB.getInstance().getSchoolClasses().stream().filter(schoolClass1 -> !schoolClass1.equals(oldSchoolClass)).noneMatch(klasse -> klasse.getClassname().equalsIgnoreCase(classname))) {
                    parent.editClass(oldSchoolClass, classname, SchoolDB.getInstance().getTeachers().stream().filter(teacher -> teacher.getAbbreviation().equals(classTeacher.getValue().getAbbreviation())).findFirst().get());
                } else {
                    info.setText("Dieses Klasse existiert bereits!");
                    info.setVisible(true);
                }
            } else {
                info.setText("Bitte alle Felder ausfüllen!");
                info.setVisible(true);
            }
        } else {
            if (!(classname.isBlank() || classTeacher.getValue() == null || inputYear.getValue() == null || inputClass.getValue() == null)) {
                if (SchoolDB.getInstance().getSchoolClasses().stream().noneMatch(klasse -> klasse.getClassname().equalsIgnoreCase(classname))) {
                    parent.submitForm(classname, SchoolDB.getInstance().getTeachers().stream().filter(teacher -> teacher.getAbbreviation().equals(classTeacher.getValue().getAbbreviation())).findFirst().get());
                } else {
                    info.setText("Dieses Klasse existiert bereits!");
                    info.setVisible(true);
                }
            } else {
                info.setText("Bitte alle Felder ausfüllen!");
                info.setVisible(true);
            }
        }

    }

    public void refreshItems() {
        this.teachers = FXCollections.observableArrayList();
        teachers.addAll(SchoolDB.getInstance().getTeachers());
        classTeacher.setItems(teachers);
    }

}
