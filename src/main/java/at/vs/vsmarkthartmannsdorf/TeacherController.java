package at.vs.vsmarkthartmannsdorf;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TeacherController {

    @FXML
    public TextField firstname;
    public TextField surname;
    public TextField abbreviation;

    public TextField getFirstname() {
        return firstname;
    }

    public TextField getSurname() {
        return surname;
    }

    public TextField getAbbreviation() {
        return abbreviation;
    }


}
