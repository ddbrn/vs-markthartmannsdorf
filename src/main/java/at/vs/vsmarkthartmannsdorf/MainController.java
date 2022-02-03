package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.IOAccess_Excel;
import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public BorderPane main;
    public HBox teacherBox, timetableBox, classBox, absenceBox;

    private List<HBox> navbar = Arrays.asList(teacherBox, timetableBox, classBox, absenceBox);

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<SchoolClass> classes = FXCollections.observableArrayList();
    private ObservableList<Timetable> timetables = FXCollections.observableArrayList();
    private ObservableList<Subject> timetableSubs = FXCollections.observableArrayList();
    private ArrayList<TeacherAbsence> teacherAbsenceList = new ArrayList<>();

    private TeacherViewController teacherViewController;
    private ClassViewController classViewController;
    private TimetableViewController timetableViewController;
    private BorderPane teacherView;
    private BorderPane classView;
    private BorderPane timetableView;
    private GridPane absenceView;

    private boolean classesOpened = false;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Load TeacherView
            FXMLLoader teacherLoader = fxmlLoad("demo/teacher.fxml");
            teacherView = teacherLoader.load();
            teacherViewController = teacherLoader.getController();
            teacherViewController.setParent(this);


            //Load ClassView
            FXMLLoader classLoader = fxmlLoad("demo/class.fxml");
            classView = classLoader.load();
            classViewController = classLoader.getController();
            classViewController.setParent(this);

            //Load TimetableView
            FXMLLoader timetableLoader = fxmlLoad("demo/timetable.fxml");
            timetableView = timetableLoader.load();
            timetableViewController = timetableLoader.getController();
            timetableViewController.setParent(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load fxml
    public FXMLLoader fxmlLoad(String location) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(location));
        return fxmlLoader;
    }

    @FXML
    public void onClickTeacher() {
        setHighlightedNav(teacherBox);

        main.setCenter(teacherView);
        main.setBottom(null);
        main.setRight(null);
    }

    @FXML
    public void onClickClasses() {
        setHighlightedNav(classBox);

        main.setCenter(classView);
        main.setBottom(null);
        main.setRight(null);
        if(classesOpened){
            classViewController.dismountForm();
            classViewController.addClass();
        }else{
            classViewController.dismountForm();
        }

    }

    @FXML
    public void onClickTimetable() {
        setHighlightedNav(timetableBox);

        main.setCenter(timetableView);
        main.setBottom(null);
        main.setRight(null);

    }

    @FXML
    public void onClickAbsence() {
        setHighlightedNav(absenceBox);

        absenceView = new GridPane();

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < teacherAbsenceList.size(); i++) {
                FXMLLoader fxmlAbsenceLoader = fxmlLoad("demo/absenceitem.fxml");
                HBox hBox = fxmlAbsenceLoader.load();

                TeacherAbsenceController teacherAbsenceController = fxmlAbsenceLoader.getController();
                teacherAbsenceController.setStartController(this);
                teacherAbsenceController.setData(teacherAbsenceList.get(i));

                if (column == 5) {
                    column = 0;
                    row++;
                }

                absenceView.add(hBox, column++, row);
                GridPane.setMargin(hBox, new Insets(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setCenter(null);
        main.setCenter(absenceView);
    }

    // used to highlight selected field in navbar
    public void setHighlightedNav(HBox hBox) {
        List<HBox> navbar = Arrays.asList(timetableBox, teacherBox, absenceBox, classBox);
        navbar.forEach(hBox1 -> hBox1.setStyle(null));
        hBox.setStyle("-fx-background-color: #518ef0;\n" +
                "    -fx-border-radius: 30;\n" +
                "    -fx-background-radius: 10 10 10 10;");
    }

    public void teacherChangedAbsentStatus(TeacherAbsence teacherAbsence) {
        teacherAbsenceList.stream().filter(t -> t.getTeacher()
                .getAbbreviation() == teacherAbsence.getTeacher()
                .getAbbreviation()).findFirst().get()
                .setAbsent(teacherAbsence.isAbsent());
    }

    @FXML
    public void importAsExcel() {
        IOAccess_Excel.createExcelFile(teachers.stream().toList(), classes.stream().toList());
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = FXCollections.observableArrayList(teachers);
        teacherViewController.setItems(this.teachers);
        loadAbsence();
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacherViewController.setItems(teachers);
        teacherAbsenceList.add(new TeacherAbsence(teacher, false));

    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        teacherViewController.setItems(teachers);
        teacherAbsenceList.removeIf(teacherAbsence -> teacherAbsence.getTeacher().equals(teacher));
    }

    public void setClasses(List<SchoolClass> classes) {
        this.classes = FXCollections.observableArrayList(classes);
        classViewController.setItems(this.classes);
        timetableViewController.setItems(this.classes);
    }

    public void addClasses(SchoolClass klasse) {
        classes.add(klasse);
        classViewController.setItems(classes);
        timetableViewController.setItems(classes);
    }

    public void removeClasses(SchoolClass klasse) {
        classes.remove(klasse);
        classViewController.setItems(classes);
        timetableViewController.setItems(classes);
    }


    public BorderPane getMain() {
        return main;
    }

    public List<Teacher> getTeacher() {
        return teachers;
    }

    public List<SchoolClass> getClasses() {
        return classes;
    }


    public void loadAbsence() {
        for (Teacher teacher : teachers) {
            teacherAbsenceList.add(new TeacherAbsence(teacher, false));
        }
    }

    public void setClassesOpened(boolean classesOpened) {
        this.classesOpened = classesOpened;
    }
}
