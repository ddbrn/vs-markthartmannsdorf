package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Data
public class ClassViewController implements Initializable {

    @FXML
    public ListView<SchoolClass> classList;

    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<SchoolClass> classes;

    private boolean isEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        classList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        classList.getSelectionModel().selectFirst();

    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    public void setItems(ObservableList<SchoolClass> classes) {
        classList.setItems(classes);
        this.classes = classList.getItems();
    }


    @FXML
    protected void addClass() {
        isEdit = false;
        parent.setClassesOpened(true);
        dismountForm();
        if (((VBox) root.getCenter()).getChildren().size() == 0) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("demo/class-form.fxml"));
                VBox vBox = fxmlLoader.load();
                ((ClassFormController) fxmlLoader.getController()).setParent(this);
                ((ClassFormController) fxmlLoader.getController()).refreshItems();
                ((VBox) root.getCenter()).getChildren().add(vBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void removeClass() {
        ObservableList<Integer> indices = classList.getSelectionModel().getSelectedIndices();
        boolean singular = false;

        if (indices.size() == 0) {
            return;
        } else if (indices.size() == 1) {
            singular = true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("vs-martkhartmannsdorf | LÖSCHEN");
        if (singular) {
            alert.setHeaderText("Wollen Sie die Klasse löschen");
        } else {
            alert.setHeaderText("Wollen Sie die Klassen löschen");
        }
        alert.setContentText("Löschen?");
        ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.getButtonData().equals(ButtonBar.ButtonData.YES)) {

                dismountForm();
                indices.stream().sorted(Comparator.reverseOrder()).forEach(i -> {
                    System.out.println(i);
                    SchoolDB.getInstance().removeSchoolClass(classList.getItems().get(i));
                });


                classList.setItems(SchoolDB.getInstance().getSchoolClasses());
            }
        });


        /*ObservableList<Integer> indices = classList.getSelectionModel().getSelectedIndices();
        System.out.println(classes);
        for (int i = indices.size() - 1; i >= 0; i--){
            parent.removeClasses(classes.get(indices.get(i)));
        }
        classList.setItems(classes);
        final ObservableList<TimetableDay> data = FXCollections.observableArrayList(
                new TimetableDay(0, "", "", "","", "", "")
        );*/
        // parent.getTimetableViewController().getTimeTableView().setItems(data);
    }

    @FXML
    public void editClass() {
        isEdit = true;
        if (classList.getSelectionModel().getSelectedIndices().size() == 1) {
            dismountForm();
            try {
                FXMLLoader fxmlLoader = parent.fxmlLoad("demo/class-form.fxml");
                VBox vBox = fxmlLoader.load();

                ClassFormController controller = fxmlLoader.getController();
                controller.setParent(this);
                controller.refreshItems();
                ((VBox) root.getCenter()).getChildren().add(vBox);


                SchoolClass schoolClass = classList.getSelectionModel().getSelectedItem();
                controller.setItemsIfEdited(schoolClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("vs-martkhartmannsdorf | BEARBEITEN");
            alert.setHeaderText("Bitte nur eine Klasse zum Bearbeiten auswählen!");
            alert.showAndWait();
        }

    }

    public void editClass (SchoolClass oldSchoolClass, String classname, Teacher teacher) {
        SchoolClass schoolClass = new SchoolClass(classname, teacher);
        dismountForm();

        oldSchoolClass.setClassname(schoolClass.getClassname());
        oldSchoolClass.setTeacher(schoolClass.getTeacher());

        updateClasses();
    }

    public void dismountForm() {
        ((VBox) root.getCenter()).getChildren().clear();
    }

    public void submitForm(String classname, Teacher teacher) {
        SchoolClass schoolClass = new SchoolClass(classname, teacher);

        dismountForm();
        SchoolDB.getInstance().addSchoolClass(schoolClass);
        classList.setItems(SchoolDB.getInstance().getSchoolClasses());
    }

    public MainController getParent() {
        return parent;
    }

    public void updateClasses() {
        classList.setItems(SchoolDB.getInstance().getSchoolClasses());
        classList.refresh();
    }

}
