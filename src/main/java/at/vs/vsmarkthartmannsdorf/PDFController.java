package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class PDFController {
    @FXML
    public ChoiceBox<SchoolClass> classes;

    public void setClasses(ObservableList<SchoolClass> classes) {
        this.classes.setItems(classes);
    }
}
