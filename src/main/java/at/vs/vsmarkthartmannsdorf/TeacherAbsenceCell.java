package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TeacherAbsenceCell extends ListCell<Teacher> {

    @FXML
    public Label teacherName;
    public Button isPresent;
    public Button isAbsent;
    public HBox hBox;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Teacher teacher, boolean b) {
        super.updateItem(teacher, b);


        if (b || teacher == null){
            setText(null);
            setGraphic(null);
        }else{
            if (fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("teacherabsence.fxml"));
                fxmlLoader.setController(this);
            }

            try{
                fxmlLoader.load();
            }catch(IOException e){
                e.printStackTrace();
            }

            teacherName.setText(String.format("%s %s (%s)", teacher.getSurname(),
                    teacher.getFirstname(), teacher.getAbbreviation()));

            setText(null);
            setGraphic(hBox);
        }
    }
}
