package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    public BorderPane main;


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
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickTeacher(){
        main.setCenter(teacherView);
        main.setBottom(null);
        main.setRight(null);
    }


    @FXML
    public void importAsExcel(){

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
}
