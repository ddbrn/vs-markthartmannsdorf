package at.vs.vsmarkthartmannsdorf;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    BorderPane main;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/teacher.fxml"));
            BorderPane borderPane = fxmlLoader.load();

            main.setCenter(borderPane);
            main.setBottom(null);
            main.setRight(null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
