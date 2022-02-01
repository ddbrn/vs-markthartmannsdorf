package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ClassViewController implements Initializable {

    @FXML
    public ListView<SchoolClass> classList;

    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<SchoolClass> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        classList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        classList.getSelectionModel().selectFirst();
    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    public void setItems(ObservableList<SchoolClass> classes){
        classList.setItems(classes);
        this.classes = classList.getItems();
    }

    @FXML
    protected void addClass(){
        if (((VBox) root.getCenter()).getChildren().size() == 0){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("demo/class-form.fxml"));
                VBox vBox = fxmlLoader.load();
                ((ClassFormController) fxmlLoader.getController()).setParent(this);
                ((VBox) root.getCenter()).getChildren().add(vBox);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void removeClass(){
        ObservableList<Integer> indices = classList.getSelectionModel().getSelectedIndices();
        System.out.println(classes);
        for (int i = indices.size() - 1; i >= 0; i--){
            parent.removeClasses(classes.get(indices.get(i)));
        }
        classList.setItems(classes);
    }
    public void dismountForm(){
        ((VBox) root.getCenter()).getChildren().clear();
    }

    public void submitForm(String classname, Teacher teacher){
        SchoolClass schoolClass = new SchoolClass(classname, teacher);

        dismountForm();
        parent.addClasses(schoolClass);
        classList.setItems(classes);
    }

    public ObservableList<SchoolClass> getClasses() {
        return classes;
    }

    public MainController getParent() {
        return parent;
    }
}
