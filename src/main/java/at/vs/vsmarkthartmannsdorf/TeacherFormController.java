package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Subjectobject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TeacherFormController implements Initializable {
    @FXML
    public TextField firstname, surname, abbreviation;
    public Label info;
    public VBox teacherVBox;

    private TeacherViewController parent;

    private List<JFXCheckBox> cbs;
    private List<Subjectobject> selectedSubjects;

    private Teacher oldTeacher;

    public TeacherFormController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.BASELINE_CENTER);
        gp.prefWidthProperty().bind(teacherVBox.widthProperty());

        gp.setVgap(10);

        cbs = new ArrayList<>();


        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger j = new AtomicInteger(0);

        SchoolDB.getInstance().getSubjects().stream().forEach(subjectobject -> {
            if (j.get() == (int) Math.ceil(SchoolDB.getInstance().getSubjects().size() / 3.0)) {
                i.incrementAndGet();
                j.set(0);
            }

            JFXCheckBox cb = new JFXCheckBox(subjectobject.getName());
            cb.setAlignment(Pos.CENTER_LEFT);
            cbs.add(cb);
            gp.add(cb, i.get(), j.getAndIncrement());
        });



        IntStream.range(0, 3).mapToObj(k -> new ColumnConstraints()).forEach(cc -> {
            cc.setHgrow(Priority.ALWAYS);
            gp.getColumnConstraints().add(cc);
        });

        teacherVBox.getChildren().add(gp);


        info.setVisible(false);

        if (SchoolDB.getInstance().getSubjects().isEmpty()) {
            info.setText("Es sind noch keine Fächer erstellt werden. Erstellen Sie neue Fächer unter dem Punkt Einstellungen");
            info.setVisible(true);
        }
    }

    public void setItemsIfEdited(Teacher teacher) {
        firstname.setText(teacher.getFirstname());
        surname.setText(teacher.getSurname());
        abbreviation.setText(teacher.getAbbreviation());

        oldTeacher = teacher;

        cbs
                .stream()
                .filter(checkBox -> teacher.getSubjects()
                        .stream()
                        .map(Subjectobject::getName)
                        .toList()
                        .contains(checkBox.getText()))
                .forEach(checkBox -> checkBox.setSelected(true));

        selectedSubjects = new ArrayList<>();
        cbs.forEach(checkBox -> {
            if (checkBox.isSelected()) {
                selectedSubjects.add(SchoolDB.getInstance().getSubjects().stream().filter(subjectobject -> subjectobject.getName().equals(checkBox.getText())).findFirst().get());
            }
        });

    }

    public void setParent(TeacherViewController parent) {
        this.parent = parent;
    }

    @FXML
    public void cancel(){
        parent.dismountForm();
    }

    @FXML
    public void submit(){
        info.setVisible(false);

        selectedSubjects = new ArrayList<>();
        cbs.forEach(checkBox -> {
            if (checkBox.isSelected()) {
                selectedSubjects.add(SchoolDB.getInstance().getSubjects().stream().filter(subjectobject -> subjectobject.getName().equals(checkBox.getText())).findFirst().get());
            }
        });

        if (parent.isEdit()) {
            if(!(firstname.getText().isEmpty() || surname.getText().isEmpty() || abbreviation.getText().isEmpty() || selectedSubjects.size() == 0)){
                System.out.println(SchoolDB.getInstance().getTeachers().stream().filter(teacher -> !teacher.equals(oldTeacher)).toList());
                if(SchoolDB.getInstance().getTeachers().stream().filter(teacher -> !teacher.equals(oldTeacher)).noneMatch(teacherDB -> teacherDB.getAbbreviation().equalsIgnoreCase(abbreviation.getText()))){
                    parent.editTeacher(oldTeacher, firstname.getText(), surname.getText(), abbreviation.getText(), selectedSubjects);
                }else{
                    info.setText("Dieses Kürzel gibt es bereits");
                    info.setVisible(true);
                }
            }else{
                info.setText("Bitte füllen Sie alle Felder aus!");
                info.setVisible(true);
            }


            parent.updateTeacher();
            return;
        }

        if(!(firstname.getText().isEmpty() || surname.getText().isEmpty() || abbreviation.getText().isEmpty() || selectedSubjects.size() == 0)){
            if(SchoolDB.getInstance().getTeachers().stream().noneMatch(teacher -> teacher.getAbbreviation().equalsIgnoreCase(abbreviation.getText()))){
                parent.submitForm(firstname.getText(), surname.getText(), abbreviation.getText(), selectedSubjects);
            }else{
                info.setText("Dieses Kürzel gibt es bereits");
                info.setVisible(true);
            }
        }else{
            info.setText("Bitte füllen Sie alle Felder aus!");
            info.setVisible(true);
        }



    }
}
