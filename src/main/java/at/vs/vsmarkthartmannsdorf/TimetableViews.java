package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.Day;
import at.vs.vsmarkthartmannsdorf.data.Lesson;
import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Timetable;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class TimetableViews implements Initializable {

    @FXML
    public BorderPane root;
    public ComboBox<String> cbDay;

    private GridPane timetableView;
    private ObservableList<String> days;
    private Timetable visibleTimetable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        days = FXCollections.observableArrayList();
        visibleTimetable = null;

        days.addAll(Arrays.asList(Day.Montag.name(), Day.Dienstag.name(), Day.Mittwoch.name(),
                Day.Donnerstag.name(), Day.Freitag.name()));

        cbDay.setItems(days);

        cbDay.setValue(days.get(0));
    }

    private GridPane buildTimetableView(Day day) {
        timetableView = new GridPane();
        List<SchoolClass> klassen = new ArrayList<>(SchoolDB.getInstance().getSchoolClasses());

        for (int i = 0; i < klassen.size(); i++) {
            int finalI = i;
            Collections.sort(klassen, Comparator.comparing(o1 -> o1.getClassname().compareTo
                    (klassen.get(finalI).getClassname())));
        }
        int column = 1;
        int row = 1;

        VBox firstBox = new VBox();
        firstBox.setStyle("-fx-background-color: #e1d9d9");
        timetableView.add(firstBox, 0, 0);

        for (SchoolClass schoolClass : klassen) {
            visibleTimetable = SchoolDB.getInstance().getTimetablesFromClass(schoolClass).get(0);
            System.out.println(visibleTimetable.getSchoolClass());

            for (int i = 1; i <= Timetable.MAX_HOURS; i++) {
                VBox vBox = new VBox();
                Label label = new Label(i + "");
                label.setStyle("-fx-font-weight: bold;-fx-font-size: 15");
                vBox.getChildren().add(label);

                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-background-color: #e1d9d9");
                vBox.setPrefWidth(30);

                timetableView.add(vBox, 0, i);
            }
            VBox classNames = new VBox();

            Label label = new Label(schoolClass.getClassname());
            label.setStyle("-fx-font-weight: bold;-fx-font-size: 17");

            classNames.getChildren().add(label);
            classNames.setAlignment(Pos.CENTER);
            classNames.setStyle("-fx-background-color: #e1d9d9");

            timetableView.add(classNames, column, 0);
            for (int i = 1; i <= Timetable.MAX_HOURS; i++) {

                Lesson lesson = visibleTimetable.getSubjects().get(day).get(i);
                if (row == Timetable.MAX_HOURS + 1) {
                    row = 1;
                }

                VBox vBox = new VBox();

                Color color = null;
                if (lesson.getSubject() != null) {
                    String colorHex = PropertiesLoader.getInstance().getProperties().getProperty(lesson.getSubject().name());
                    color = Color.valueOf(colorHex);
                    vBox.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                vBox.setPadding(new Insets(10, 10, 10, 10));
                vBox.setPrefWidth(100);

                Label lblSubject;
                Label lblTeacher = new Label("");

                if (!lesson.getTeacher().isEmpty()) {
                    StringBuilder teacher = new StringBuilder();
                    for (int k = 0; k < lesson.getTeacher().size(); k++) {
                        if (k == 0) {
                            teacher = new StringBuilder(SchoolDB.getInstance().getTeacherByID(lesson.getTeacher().get(k).getTeacherId()).get().getAbbreviation());
                        } else {
                            teacher.append(" | ").append(SchoolDB.getInstance()
                                    .getTeacherByID(lesson.getTeacher().get(k).getTeacherId())
                                    .get()
                                    .getAbbreviation());
                        }
                    }
                    lblTeacher.setText(teacher.toString());
                }

                lblSubject = lesson.getSubject() == null ? new Label(" ") : new Label(lesson.getSubject().name());
                if (color != null) {
                    double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
                    if (luminance > 0.002) {
                        lblTeacher.setStyle("-fx-text-fill: black");
                        lblSubject.setStyle("-fx-text-fill: black;-fx-font-weight: bold");
                    } else {
                        lblTeacher.setStyle("-fx-text-fill: white");
                        lblSubject.setStyle("-fx-text-fill: white;-fx-font-weight: bold");
                    }
                }

                vBox.getChildren().add(lblSubject);
                vBox.getChildren().add(lblTeacher);

                timetableView.add(vBox, column, row);
                row++;
            }
            column++;
        }


        return timetableView;
    }

    public void loadTimetable() {
        root.getCenter().setVisible(true);
        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().clear();
        ((BorderPane) root.getCenter()).getTop().setVisible(true);

        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren()
                .add(buildTimetableView(Day.valueOf(cbDay.getSelectionModel().getSelectedItem())));
    }

    @FXML
    public void onChangeDay() {
        loadTimetable();
    }
}