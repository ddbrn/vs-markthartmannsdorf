package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TimetableController_new implements Initializable {

    @FXML
    public ListView<Timetable_new> lvTimetables;
    public BorderPane root;

    private Timetable_new visibleTimetable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvTimetables.setItems(SchoolDB.getInstance().getTimetables());
        visibleTimetable = null;
    }

    public void load(){
        ((VBox) root.getCenter()).getChildren().clear();

        visibleTimetable = SchoolDB.getInstance().getTimetables().get(0);
        ((VBox) root.getCenter()).getChildren().add(buildTimetable(visibleTimetable));
    }

    public GridPane buildTimetable(Timetable_new timetable) {
        GridPane timeTableView = new GridPane();

        int column = 1;
        int row = 1;

        for (int i = 1; i <= Integer.parseInt(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.max_stunden.name())); i++) {
            timeTableView.add(new Label(i + ""), 0, i);
        }
        for (Day day : Day.values()) {
            timeTableView.add(new Label(day.name()), column, 0);
            column++;
        }

        column = 1;

        HashMap<Day, List<TeacherSubject>> subjects = timetable.getSubjects();
        for (Day day : Day.values()) {
            if (column == Day.values().length + 1) {
                column = 1;
            }
            for (TeacherSubject teacherSubject : subjects.get(day)) {
                if (row == Integer.parseInt(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.max_stunden.name())) + 1) {
                    row = 1;
                }

                VBox vBox = new VBox();

                Color color = null;
                if (teacherSubject.getSubject() != null){
                    String colorHex = PropertiesLoader.getInstance().getProperties().getProperty(teacherSubject.getSubject().name());
                    color = Color.valueOf(colorHex);

                    vBox.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                vBox.setPadding(new Insets(10, 10, 10, 10));

                Label lblSubject;
                Label lblTeacher;

                lblTeacher = teacherSubject.getTeacher() == null ? new Label(" ") : new Label(teacherSubject.getTeacher().getAbbreviation());
                lblSubject = teacherSubject.getSubject() == null ? new Label(" ") : new Label(teacherSubject.getSubject().name());

                if (color != null){
                    if (color.getBrightness() < 0.9){
                        lblTeacher.setStyle("-fx-text-fill: white");
                        lblSubject.setStyle("-fx-text-fill: white");
                    }
                }

                vBox.getChildren().add(lblSubject);
                vBox.getChildren().add(lblTeacher);

                vBox.setOnMouseClicked(mouseEvent -> {
                    System.out.println("Klick");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("demo/absence-dialog.fxml"));
                    DialogPane absenceDialog = null;
                    try {
                        absenceDialog = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(absenceDialog);
                    dialog.setTitle("Abwesenheit");
                    dialog.showAndWait();
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

    @FXML
    public void addSubject() {

    }

    @FXML
    public void onSelectClass() {
        ((VBox) root.getCenter()).getChildren().clear();

        visibleTimetable = lvTimetables.getSelectionModel().getSelectedItem();
        visibleTimetable.addSubject(Day.Dienstag, 1, new TeacherSubject(SchoolDB.getInstance().getTeachers().get(0), Subject.Deutsch));

        ((VBox) root.getCenter()).getChildren().add(buildTimetable(visibleTimetable));
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
}
