package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
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
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    BorderPane main;

    private List<Teacher> teacherList;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/teacher.fxml"));
            BorderPane borderPane = fxmlLoader.load();

            GridPane gridPane = new GridPane();
            borderPane.setCenter(gridPane);

            // fxmlLoader.setLocation(getClass().getResource("absenceitem.fxml"));
            // VBox vBox = fxmlLoader.load();

            main.setCenter(borderPane);
            main.setBottom(null);
            main.setRight(null);



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void importAsExcel(){

    }
}
