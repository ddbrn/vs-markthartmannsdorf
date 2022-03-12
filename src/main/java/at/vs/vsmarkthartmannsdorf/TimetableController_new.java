package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

public class TimetableController_new implements Initializable {

    @FXML
    public ListView<Timetable_new> lvTimetables;
    public BorderPane root;
    public ComboBox<Integer> cbHours;

    private boolean isEdit;
    private Timetable_new visibleTimetable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((BorderPane) root.getCenter()).getTop().setVisible(false);

        lvTimetables.setItems(SchoolDB.getInstance().getTimetables());
        visibleTimetable = null;

        ObservableList<Integer> olHours = FXCollections.observableArrayList();
        for (int i = 1; i <= Integer.parseInt(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.max_stunden.name())); i++) {
            olHours.add(i);
        }
        cbHours.setItems(olHours);

        isEdit = false;
    }

    public void load() {
        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().clear();
        ((BorderPane) root.getCenter()).getTop().setVisible(false);

        lvTimetables.setItems(SchoolDB.getInstance().getTimetables());

        // visibleTimetable = SchoolDB.getInstance().getTimetables().get(0);
        // ((VBox) root.getCenter()).getChildren().add(buildTimetable(visibleTimetable));
    }

    public GridPane buildTimetable(Timetable_new timetable) {
        cbHours.getSelectionModel().select((Integer) visibleTimetable.getMaxHours());

        GridPane timeTableView = new GridPane();

        int column = 1;
        int row = 1;

        for (int i = 1; i <= visibleTimetable.getMaxHours(); i++) {
            timeTableView.add(new Label(i + ""), 0, i);
        }
        for (Day day : Day.values()) {
            timeTableView.add(new Label(day.name()), column, 0);
            column++;
        }

        column = 1;

        HashMap<Day, HashMap<Integer, TeacherSubject>> subjects = timetable.getSubjects();
        for (Day day : Day.values()) {
            if (column == Day.values().length + 1) {
                column = 1;
            }
            for (int i = 1; i <= visibleTimetable.getMaxHours(); i++) {
                TeacherSubject teacherSubject = visibleTimetable.getSubjects().get(day).get(i);
                if (row == visibleTimetable.getMaxHours() + 1) {
                    row = 1;
                }

                VBox vBox = new VBox();

                Color color = null;
                if (teacherSubject.getSubject() != null) {
                    String colorHex = PropertiesLoader.getInstance().getProperties().getProperty(teacherSubject.getSubject().name());
                    color = Color.valueOf(colorHex);

                    vBox.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                vBox.setPadding(new Insets(10, 10, 10, 10));

                Label lblSubject;
                Label lblTeacher;

                lblTeacher = teacherSubject.getTeacher() == null ? new Label(" ") : new Label(teacherSubject.getTeacher().getAbbreviation());
                lblSubject = teacherSubject.getSubject() == null ? new Label(" ") : new Label(teacherSubject.getSubject().name());

                if (color != null) {
                    if (color.getBrightness() < 0.9) {
                        lblTeacher.setStyle("-fx-text-fill: white");
                        lblSubject.setStyle("-fx-text-fill: white");
                    }
                }

                vBox.getChildren().add(lblSubject);
                vBox.getChildren().add(lblTeacher);

                int finalRow = row;
                vBox.setOnMouseClicked(mouseEvent -> {
                    if (isEdit) {
                        System.out.println("Day " + day + " Row " + finalRow);
                        addSubject(day, finalRow, SchoolDB.getInstance().getTeacherSubjects().get(0));
                    }
                });

                timeTableView.add(vBox, column, row);
                row++;
            }
            column++;
        }

        timeTableView.setGridLinesVisible(true);

        addStyle(timeTableView);

        return timeTableView;
    }

    public void addSubject(Day day, int hour, TeacherSubject teacherSubject) {
        SchoolDB.getInstance().addSubject(day, hour, teacherSubject, visibleTimetable);
        reload();
        setContent();
    }

    @FXML
    public void onSelectClass() {
        visibleTimetable = lvTimetables.getSelectionModel().getSelectedItem();
        setContent();
    }

    public void addStyle(GridPane timetableView) {
        List<ColumnConstraints> columnConstraintsList = new ArrayList<>();
        List<RowConstraints> rowConstraintsList = new ArrayList<>();


        for (int i = 0; i < timetableView.getColumnCount(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            if (i == 0) {
                columnConstraints.setPercentWidth(5);
                columnConstraintsList.add(columnConstraints);
            } else {
                columnConstraints.setPercentWidth(50);
                columnConstraintsList.add(columnConstraints);
            }
        }

        RowConstraints rowConstraints = new RowConstraints();
        for (int i = 0; i < timetableView.getRowCount(); i++) {
            rowConstraints.setPercentHeight(50);
            rowConstraintsList.add(rowConstraints);
        }

        timetableView.getColumnConstraints().addAll(columnConstraintsList);
        timetableView.getRowConstraints().addAll(rowConstraintsList);

        GridPane.setVgrow(timetableView, Priority.ALWAYS);
    }

    @FXML
    public void onChangeHours() {
        int selectedHours = cbHours.getSelectionModel().getSelectedIndex() + 1;
        if (selectedHours != visibleTimetable.getMaxHours()) {
            SchoolDB.getInstance().updateTimetable(visibleTimetable, selectedHours);
            reload(); // Loads new changed timetable from SchoolDB
            setContent();
        }
    }

    public void setContent() {
        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().clear();
        ((BorderPane) root.getCenter()).getTop().setVisible(true);
        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().add(buildTimetable(visibleTimetable));
    }

    public void reload() {
        SchoolClass schoolClass = visibleTimetable.getSchoolClass();
        visibleTimetable = SchoolDB.getInstance().findTimetableByClass(schoolClass).get();
    }

    @FXML
    public void onEditTimetable() {
        if (isEdit == true) {
            isEdit = false;
            setContent();
        } else {
            isEdit = true;
        }

        if (isEdit) {
            HBox hBox = new HBox();

            hBox.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            VBox subjects = new VBox();
            subjects.getChildren().add(new Label("Fächer"));

            VBox teachers = new VBox();

            ToggleGroup subjectGroup = new ToggleGroup();
            ToggleGroup teacherGroup = new ToggleGroup();
            for (Subject subject : Subject.values()) {
                RadioButton radioButton = new RadioButton(subject.name());
                radioButton.setToggleGroup(subjectGroup);

                radioButton.setOnAction(actionEvent -> {
                    teachers.getChildren().clear();
                    teachers.getChildren().add(new Label("Lehrer"));
                    for (TeacherSubject teacherSubject: SchoolDB.getInstance().getTeacherBySubject(subject)){
                        RadioButton radioButton1 = new RadioButton(teacherSubject.getTeacher().getSurname() + " " +
                                teacherSubject.getTeacher().getFirstname());
                        radioButton1.setToggleGroup(teacherGroup);
                        teachers.getChildren().add(radioButton1);
                    }
                });

                subjects.getChildren().add(radioButton);
            }

            hBox.setPadding(new Insets(10, 10, 10, 10));
            subjects.setPadding(new Insets(10, 10, 10, 10));
            teachers.setPadding(new Insets(10, 10, 10, 10));

            subjects.setSpacing(10);

            HBox.setHgrow(hBox, Priority.ALWAYS);
            HBox.setHgrow(subjects, Priority.ALWAYS);
            HBox.setHgrow(teachers, Priority.ALWAYS);

            hBox.getChildren().add(subjects);
            hBox.getChildren().add(teachers);
            ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().add(hBox);
        }
    }
}
