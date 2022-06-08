package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.IOAccess_PDF;
import at.vs.vsmarkthartmannsdorf.bl.IOAccess_Print;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.*;
import com.fasterxml.jackson.databind.node.ValueNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Data
public class TimetableController implements Initializable {

    @FXML
    public ListView<SchoolClass> lvTimetables;
    public BorderPane root;
    public Label lblInfo;
    public VBox vbSidePanel;
    public ComboBox<Week> cbWeek;
    public Button btnAddWeek;
    public Button btnRemoveWeek;

    private boolean isEdit;
    private Timetable visibleTimetable;
    private GridPane timetableView;
    private HBox hbTeacher;
    private HBox hbSubjects;
    private TimetableViews controller;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ((BorderPane) root.getCenter()).getTop().setVisible(false);

        lvTimetables.setItems(SchoolDB.getInstance().getSchoolClasses());
        visibleTimetable = null;

        isEdit = false;

        lblInfo.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
        vbSidePanel.setVisible(false);
        timetableView = null;

        btnAddWeek.setTooltip(new Tooltip("Woche hinzufügen"));
        btnRemoveWeek.setTooltip(new Tooltip("Woche entfernen"));
    }

    public void load() {
        isEdit = false;
        vbSidePanel.getChildren().clear();

        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().clear();
        ((BorderPane) root.getCenter()).getTop().setVisible(false);
        vbSidePanel.setVisible(false);

        lvTimetables.setItems(SchoolDB.getInstance().getSchoolClasses());
        lvTimetables.refresh();
        root.getCenter().setVisible(false);
    }

    public GridPane buildTimetable() {
        timetableView = new GridPane();

        VBox firstBox = new VBox();
        firstBox.setStyle("-fx-background-color: #e1d9d9");
        timetableView.add(firstBox, 0, 0);

        int column = 1;
        int row = 1;

        for (int i = 1; i <= Timetable.MAX_HOURS; i++) {
            VBox vBox = new VBox();
            Label label = new Label(i + "");
            label.setStyle("-fx-font-weight: bold;-fx-font-size: 15");
            vBox.getChildren().add(label);

            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-background-color: #e1d9d9");

            timetableView.add(vBox, 0, i);
        }
        for (Day day : Day.values()) {
            VBox vBox = new VBox();

            Label label = new Label(day.name());
            label.setStyle("-fx-font-weight: bold;-fx-font-size: 17");

            vBox.getChildren().add(label);
            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-background-color: #e1d9d9");

            timetableView.add(vBox, column, 0);
            column++;
        }

        column = 1;

        for (Day day : Day.values()) {
            if (column == Day.values().length + 1) {
                column = 1;
            }
            for (int i = 1; i <= Timetable.MAX_HOURS; i++) {

                Lesson lesson = visibleTimetable.getSubjects().get(day).get(i);
                System.out.println(lesson);
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

                vBox.setOnDragDetected(mouseDragEvent -> {
                    if (isEdit) {
                        Dragboard db = vBox.startDragAndDrop(TransferMode.ANY);
                        db.setDragView(vBox.snapshot(null, null), mouseDragEvent.getX(), mouseDragEvent.getY());
                        ClipboardContent clipboardContent = new ClipboardContent();

                        int sourceColumnIndex = GridPane.getColumnIndex(vBox);
                        int sourceRowIndex = GridPane.getRowIndex(vBox);
                        String content = String.format("%d,%d", sourceColumnIndex, sourceRowIndex);

                        clipboardContent.putString(content);
                        db.setContent(clipboardContent);
                        mouseDragEvent.consume();
                    }
                });

                vBox.setOnDragOver(dragEvent -> {
                    if (isEdit) {
                        if (dragEvent.getGestureSource() != vBox &&
                                dragEvent.getDragboard().hasString()) {
                            dragEvent.acceptTransferModes(TransferMode.MOVE);
                        }

                        dragEvent.consume();
                    }
                });

                int finalRow = row;
                vBox.setOnDragDropped(dragEvent -> {
                    if (isEdit) {
                        Dragboard db = dragEvent.getDragboard();
                        boolean success = false;

                        if (db.hasString()) {
                            if (db.getString().contains(",")) {
                                String[] split = db.getString().split(",");
                                int source_column = Integer.parseInt(split[0]);
                                int source_row = Integer.parseInt(split[1]);

                                Node sourceNode = getNodeByRowColumnIndex(source_row, source_column);
                                switchNodes(sourceNode, vBox, timetableView);
                            } else {
                                Subject subject = Subject.valueOf(db.getString());
                                addSubject(day, finalRow, new Lesson(subject, List.of()));
                            }
                            success = true;
                        }
                        dragEvent.setDropCompleted(success);
                        dragEvent.consume();
                    }
                });

                vBox.setBorder(null);

                int finalRow1 = row;
                vBox.setOnMouseClicked(mouseEvent -> {
                    hbTeacher = new HBox();
                    if (isEdit) {
                        hbTeacher.setVisible(true);
                        GridPane teacherGrid = new GridPane();
                        List<TeacherSubject> availableTeacher = SchoolDB.getInstance().getTeacherBySubject(lesson.getSubject());
                        int k = 0;
                        int j = 0;
                        for (TeacherSubject teacherSubject : availableTeacher) {
                            if (k == (int) Math.ceil(availableTeacher.size() / 3.0)) {
                                j++;
                                k = 0;
                            }

                            TeacherCheckbox cb = new TeacherCheckbox(teacherSubject);
                            cb.setSelected(SchoolDB.getInstance().checkIfTeacherContainsInLesson(day, finalRow1, visibleTimetable, teacherSubject));

                            cb.setOnAction(actionEvent -> {
                                if (cb.isSelected()) {
                                    SchoolDB.getInstance().addTeacherToLesson(day, finalRow1, visibleTimetable, cb.getTeacherSubject());
                                } else {
                                    SchoolDB.getInstance().removeTeacherFromLesson(day, finalRow, visibleTimetable, cb.getTeacherSubject());
                                }
                                reload();
                                setContent();
                            });
                            cb.setAlignment(Pos.CENTER_LEFT);
                            teacherGrid.add(cb, k, j++);
                        }
                        teacherGrid.setVgap(5);
                        hbTeacher.getChildren().clear();
                        hbTeacher.getChildren().add(teacherGrid);
                    } else {
                        vBox.setBorder(null);
                    }

                    if (vbSidePanel.getChildren().size() == 2) {
                        vbSidePanel.getChildren().remove(1);
                    }
                    vbSidePanel.getChildren().add(hbTeacher);
                });

                timetableView.add(vBox, column, row);
                row++;
            }
            column++;
        }

        timetableView.setGridLinesVisible(true);
        addStyle(timetableView);

        return timetableView;
    }

    private void switchNodes(Node source, Node target, GridPane pane) {
        int sourceColumnIndex = GridPane.getColumnIndex(source);
        int sourceRowIndex = GridPane.getRowIndex(source);
        int targetColumnIndex = GridPane.getColumnIndex(target);
        int targetRowIndex = GridPane.getRowIndex(target);

        pane.getChildren().removeAll(source, target);

        pane.add(source, targetColumnIndex, targetRowIndex);
        pane.add(target, sourceColumnIndex, sourceRowIndex);

        boolean switched = SchoolDB.getInstance().switchLessons(Day.values()[sourceColumnIndex - 1],
                Day.values()[targetColumnIndex - 1],
                sourceRowIndex, targetRowIndex, visibleTimetable);

        reload();
        setContent();

        if (!switched) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("vs-martkhartmannsdorf | Stunde");
            alert.setHeaderText("Stundenwechsel nicht möglich, da der Lehrer schon belegt ist!");
            alert.show();
        }
    }

    public Node getNodeByRowColumnIndex(int row, int column) {
        Node result = null;
        ObservableList<Node> childrens = timetableView.getChildren();


        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public void addSubject(Day day, int hour, Lesson lesson) {
        SchoolDB.getInstance().addSubject(day, hour, lesson, visibleTimetable);
        reload();
        setContent();
        refreshTimetableViews();
    }

    public void removeSubject(Day day, int hour) {
        SchoolDB.getInstance().removeSubject(day, hour, visibleTimetable);
        reload();
        setContent();
        refreshTimetableViews();
    }

    @FXML
    public void onSelectClass() {
        if (!lvTimetables.getSelectionModel().isEmpty()) {
            if (hasEmptyLessons() && ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().size() != 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("vs-martkhartmannsdorf | Bearbeiten");
                alert.setHeaderText("Es gibt noch Stunden ohne Lehrer!");
                alert.showAndWait();
            }
            visibleTimetable = SchoolDB.getInstance().getTimetablesFromClass(lvTimetables.getSelectionModel().getSelectedItem()).get(0);
            isEdit = false;
            vbSidePanel.getChildren().clear();
            vbSidePanel.setVisible(false);
            changeLabelText();
            cbWeek.setItems(FXCollections.observableList(SchoolDB
                    .getInstance()
                    .getWeeksFromSchoolClass(visibleTimetable.getSchoolClass())));
            cbWeek.getSelectionModel().select(Week.A);
            setContent();
        }
    }

    public void addStyle(GridPane timetableView) {
        List<ColumnConstraints> columnConstraintsList = new ArrayList<>();
        List<RowConstraints> rowConstraintsList = new ArrayList<>();


        for (int i = 0; i < timetableView.getColumnCount(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            if (i == 0) {
                columnConstraints.setPercentWidth(5);
            } else {
                columnConstraints.setPercentWidth(50);
            }
            columnConstraintsList.add(columnConstraints);
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

    public void setContent() {
        root.getCenter().setVisible(true);
        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().clear();
        ((BorderPane) root.getCenter()).getTop().setVisible(true);

        ((VBox) ((BorderPane) root.getCenter()).getCenter()).getChildren().add(buildTimetable());
        refreshTimetableViews();
    }

    public void reload() {
        SchoolClass schoolClass = visibleTimetable.getSchoolClass();
        Optional<Timetable> timetable = SchoolDB.getInstance()
                .findTimetableByClass(schoolClass, visibleTimetable.getWeek());
        timetable.ifPresent(value -> visibleTimetable = value);
        refreshTimetableViews();
    }

    @FXML
    public void onEditTimetable() {
        vbSidePanel.getChildren().clear();
        if (isEdit) {
            if (visibleTimetable.hasEmptyLesson()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("vs-martkhartmannsdorf | Bearbeiten");
                alert.setHeaderText("Es gibt noch Stunden ohne Lehrer!");
                alert.show();
            }
            isEdit = false;
            vbSidePanel.setVisible(false);
            setContent();
        } else {
            isEdit = true;
            vbSidePanel.setVisible(true);
        }
        changeLabelText();

        if (isEdit) {
            hbSubjects = new HBox();
            hbTeacher = new HBox();

            GridPane subjects = new GridPane();
            hbSubjects.setPadding(new Insets(10, 10, 10, 10));
            subjects.setAlignment(Pos.BASELINE_CENTER);
            subjects.setHgap(10);
            subjects.setVgap(10);

            int i = 0;
            int j = 0;
            for (Subject subject : Subject.values()) {
                VBox vBox = new VBox();

                String colorHex = PropertiesLoader.getInstance().getProperties().getProperty(subject.name());
                Color color = Color.valueOf(colorHex);

                Label label = new Label(subject.name());
                double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
                if (luminance > 0.002) {
                    label.setStyle("-fx-text-fill: black");
                } else {
                    label.setStyle("-fx-text-fill: white");
                }

                vBox.getChildren().add(label);
                vBox.setPadding(new Insets(5, 5, 5, 5));
                vBox.setBackground(new Background(new BackgroundFill(color, new CornerRadii(5), Insets.EMPTY)));
                vBox.setPadding(new Insets(10, 10, 10, 10));
                vBox.setAlignment(Pos.CENTER);

                if (i == (int) Math.ceil(Subject.values().length / 4.0)) {
                    i = 0;
                    j += 1;
                }

                vBox.setOnDragDetected(mouseDragEvent -> {
                    Dragboard db = vBox.startDragAndDrop(TransferMode.ANY);
                    db.setDragView(vBox.snapshot(null, null), mouseDragEvent.getX(), mouseDragEvent.getY());
                    ClipboardContent clipboardContent = new ClipboardContent();
                    String content = subject.name();
                    clipboardContent.putString(content);
                    db.setContent(clipboardContent);
                    mouseDragEvent.consume();
                });

                vBox.setOnDragOver(dragEvent -> {
                    if (dragEvent.getGestureSource() != vBox &&
                            dragEvent.getDragboard().hasString()) {
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                    }

                    dragEvent.consume();
                });

                subjects.add(vBox, i++, j);
            }

            hbSubjects.setOnDragOver(dragEvent -> {
                if (isEdit) {
                    if (dragEvent.getGestureSource() != hbSubjects &&
                            dragEvent.getDragboard().hasString() && dragEvent.getDragboard().getString().contains(",")) {
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                    }

                    dragEvent.consume();
                }
            });

            hbSubjects.setOnDragDropped(dragEvent -> {
                Dragboard db = dragEvent.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    if (db.getString().contains(",")) {
                        String[] split = db.getString().split(",");
                        int sourceColumn = Integer.parseInt(split[0]);
                        int sourceRow = Integer.parseInt(split[1]);

                        removeSubject(Day.values()[sourceColumn - 1], sourceRow);
                        reload();
                        setContent();
                    }
                    success = true;
                }
                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            });

            hbSubjects.getChildren().add(subjects);
            vbSidePanel.getChildren().add(hbSubjects);
        }
        refreshTimetableViews();
    }

    @FXML
    public void onChangeWeek() {
        SchoolClass schoolClass = visibleTimetable.getSchoolClass();
        Week week = cbWeek.getSelectionModel().getSelectedItem();
        if (week != null) {
            visibleTimetable = SchoolDB.getInstance()
                    .getTimetablesFromClass(schoolClass)
                    .stream()
                    .filter(timetable -> timetable.getWeek().equals(week)).findFirst().get();
        }
        reload();
        setContent();
        refreshTimetableViews();
    }

    @FXML
    public void onAddWeek() {
        if (cbWeek.getItems().size() != Week.values().length) {
            Week lastWeek = cbWeek.getItems().get(cbWeek.getItems().size() - 1);
            ObservableList<Week> weeks = cbWeek.getItems();
            weeks.add(Arrays.asList(Week.values()).get(Arrays.asList(Week.values()).indexOf(lastWeek) + 1));
            cbWeek.setItems(weeks);
            SchoolDB.getInstance().addWeekToTimetable(visibleTimetable.getSchoolClass());
            cbWeek.getSelectionModel().select(weeks.get(weeks.size() - 1));
        }
    }

    @FXML
    public void onRemoveWeek() {
        Week week = cbWeek.getSelectionModel().getSelectedItem();
        if (!week.equals(Week.A)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("vs-martkhartmannsdorf | LÖSCHEN");
            alert.setHeaderText(String.format("Wollen sie wirklich die %s-Woche löschen?", week.name()));
            alert.setContentText("Löschen?");
            ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    SchoolClass schoolClass = visibleTimetable.getSchoolClass();
                    SchoolDB.getInstance().removeWeekFromTimetable(schoolClass, visibleTimetable.getWeek());
                    visibleTimetable = SchoolDB.getInstance().getTimetablesFromClass(schoolClass).get(0);
                    cbWeek.getItems().remove(week);
                    setContent();
                    reload();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("vs-martkhartmannsdorf | WOCHE ENTFERNEN");
            alert.setHeaderText("Sie können die A-Woche nicht entfernen!");
            alert.showAndWait();
        }

    }

    private void changeLabelText() {
        if (isEdit) {
            lblInfo.setText(String.format("Bearbeitungsmodus | %s",
                    visibleTimetable.getSchoolClass().getClassname()));
        } else {
            lblInfo.setText(String.format("%s",
                    visibleTimetable.getSchoolClass().getClassname()));
        }
    }

    @FXML
    public void onPrintTimetable() {
        SchoolDB.getInstance().setPrintTimetables(timetableView);
        IOAccess_Print.printTest();
    }

    public boolean hasEmptyLessons() {
        if (visibleTimetable == null) {
            return false;
        }
        return visibleTimetable.hasEmptyLesson();
    }

    @FXML
    public void onShowTimetableViews() {
        FXMLLoader timetabledayLoader = new FXMLLoader();
        timetabledayLoader.setLocation(getClass().getResource("demo/timetableviews.fxml"));

        try {
            DialogPane pane = new DialogPane();
            pane.setContent(timetabledayLoader.load());
            controller = timetabledayLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);

            dialog.setTitle("Tagesstundenpläne");
            pane.getButtonTypes().add(ButtonType.CLOSE);
            pane.setPrefWidth(1200);
            pane.setPrefHeight(700);
            dialog.initModality(Modality.WINDOW_MODAL);

            controller.loadTimetable();
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTimetableViews(){
        if(controller != null){
            controller.loadTimetable();
        }
    }

    public TimetableViews getController(){
        return controller;
    }
}
