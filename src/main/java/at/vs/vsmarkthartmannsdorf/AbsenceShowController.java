package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AbsenceShowController implements Initializable  {
    public Label lblName;
    public ListView<TeacherAbsence> lstAbsence;
    private ObservableList<TeacherAbsence> absences;

    private Teacher teacher;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;

        lblName.setText(teacher.getFirstname() + " " + teacher.getSurname());

        lstAbsence.setItems(FXCollections.observableList(SchoolDB.getInstance().getTeacherAbsences()
                .stream().filter(teacherAbsence -> teacherAbsence.getTeacherID() == teacher.getId()).toList()));
        this.absences = lstAbsence.getItems();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
