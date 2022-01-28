package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TeacherViewController implements Initializable {
    @FXML
    public ListView<Teacher> teacherList;

    @FXML
    public BorderPane root;

    private ObservableList<Teacher> teachers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacherList.getSelectionModel().selectFirst();

        teachers = teacherList.getItems();
    }


    public void setItems(ObservableList<Teacher> teachers){
        teacherList.setItems(teachers);
    }

    @FXML
    protected void onAddTeacher() {
        System.out.println("Moin");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/teacher-form.fxml"));
            VBox vBox = fxmlLoader.load();

            ((VBox) root.getCenter()).getChildren().add(vBox);
    }catch (Exception e){
            e.printStackTrace();
        }
    }
}
