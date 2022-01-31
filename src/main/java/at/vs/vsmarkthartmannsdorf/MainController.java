package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.IOAccess_Excel;
import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.*;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    public BorderPane main;
    public HBox teacherBox, timetableBox, classBox, absenceBox;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<SchoolClass> classes = FXCollections.observableArrayList();
    private ObservableList<Timetable> timetables = FXCollections.observableArrayList();
    private ObservableList<Subject> timetableSubs = FXCollections.observableArrayList();
    private ArrayList<TeacherAbsence> teacherAbsenceList = new ArrayList<>();

    private TeacherViewController teacherViewController;
    private BorderPane teacherView;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/teacher.fxml"));
            teacherView = fxmlLoader.load();
            teacherViewController = fxmlLoader.getController();
            teacherViewController.setParent(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickTeacher(){
        main.setCenter(teacherView);
        main.setBottom(null);
        main.setRight(null);

        teacherBox.setStyle("-fx-background-color: #518ef0;\n" +
                "    -fx-border-radius: 30;\n" +
                "    -fx-background-radius: 10 10 10 10;");

    }

    @FXML
    public void onClickTimetable(){
    }


    @FXML
    public void importAsExcel(){
        IOAccess_Excel.createExcelFile(teachers.stream().toList(), classes.stream().toList());
    }


    public void setTeachers(List<Teacher> teachers) {
        this.teachers = FXCollections.observableArrayList(teachers);
        teacherViewController.setItems(this.teachers);
    }

    public void setClasses(List<SchoolClass> classes) {
        this.classes = FXCollections.observableArrayList(classes);
        // classList.setItems(this.classes);
    }

    public BorderPane getMain() {
        return main;
    }

    public List<Teacher> getTeacher() {
        return teachers;
    }
}
