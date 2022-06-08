package at.vs.vsmarkthartmannsdorf;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;
@Data
public class SubjectFormController implements Initializable {

    @FXML
    public TextField txtSubject;
    public ColorPicker clrPicker;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public TextField getTxtSubject() {
        return txtSubject;
    }

    public ColorPicker getClrPicker() {
        return clrPicker;
    }
}


