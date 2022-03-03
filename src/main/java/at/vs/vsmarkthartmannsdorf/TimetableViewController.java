package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.TimetableDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class TimetableViewController implements Initializable {
    @FXML
    public ListView<SchoolClass> timeTableList;


    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<SchoolClass> classes;

    @FXML
    private TableView timeTableView;
    private TableColumn colTime;
    private TableColumn colMonday;
    private TableColumn colTuesday;
    private TableColumn colWednesday;
    private TableColumn colThursday;
    private TableColumn colFriday;

    public TableView getTimeTableView() {
        return timeTableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colTime = new TableColumn("");
        colMonday = new TableColumn("Monday");
        colTuesday = new TableColumn("Tuesday");
        colWednesday = new TableColumn("Wednesday");
        colThursday = new TableColumn("Thursday");
        colFriday = new TableColumn("Friday");


        colTime.setSortable(false);
        colMonday.setSortable(false);
        colTuesday.setSortable(false);
        colWednesday.setSortable(false);
        colThursday.setSortable(false);
        colFriday.setSortable(false);

        colTime.setReorderable(false);
        colMonday.setReorderable(false);
        colTuesday.setReorderable(false);
        colWednesday.setReorderable(false);
        colThursday.setReorderable(false);
        colFriday.setReorderable(false);

        /*colTime.setResizable(false);
        colMonday.setResizable(false);
        colTuesday.setResizable(false);
        colWednesday.setResizable(false);
        colThursday.setResizable(false);
        colFriday.setResizable(false);*/

        timeTableView.getColumns().clear();

        timeTableView.getColumns().addAll(colTime, colMonday, colTuesday,
                colWednesday, colThursday, colFriday);

        // List of Content
        final ObservableList<TimetableDay> data = FXCollections.observableArrayList(
                new TimetableDay("", "", "", "", "", "", "")
        );
        colTime.setCellValueFactory(new PropertyValueFactory<TimetableDay, String>("time"));
        colMonday.setCellValueFactory(new PropertyValueFactory<TimetableDay, String>("monday"));
        colTuesday.setCellValueFactory(new PropertyValueFactory<TimetableDay, String>("tuesday"));
        colWednesday.setCellValueFactory(new PropertyValueFactory<TimetableDay, String>("wednesday"));
        colThursday.setCellValueFactory(new PropertyValueFactory<TimetableDay, String>("thursday"));
        colFriday.setCellValueFactory(new PropertyValueFactory<TimetableDay, String>("friday"));


        timeTableView.setItems(data);


        /*timeTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);*/


        System.out.println(timeTableView.getColumns().size());

    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    public void setItems(ObservableList<SchoolClass> classes) {
        timeTableList.setItems(classes);
        this.classes = timeTableList.getItems();
    }

    @FXML
    protected void addSubject() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/timetableaddsubject-form.fxml"));
            VBox vBox = fxmlLoader.load();
            // ((TimeTableFormController) fxmlLoader.getController()).setParent(this);
            ((VBox) root.getCenter()).getChildren().clear();
            ((VBox) root.getCenter()).getChildren().add(vBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setParent(TimetableViewController timetableViewController) {
    }

    @FXML
    public void ClickedList() {
        timeTableView.setItems(FXCollections.observableArrayList(timeTableList.getItems().
                get(timeTableList.getSelectionModel().getSelectedIndex()).getTimetable().getTimeTableContent()));
    }

    public void dismountForm() {
        ((VBox) root.getCenter()).getChildren().clear();
    }

    public void submitForm(String classname, Teacher teacher) {
        SchoolClass schoolClass = new SchoolClass(classname, teacher);

        dismountForm();
        parent.addClasses(schoolClass);
        timeTableList.setItems(classes);
    }

    public ObservableList<SchoolClass> getClasses() {
        return classes;
    }

    public MainController getParent() {
        return parent;
    }

    public void onChangeTimeTable() {

    }
}
