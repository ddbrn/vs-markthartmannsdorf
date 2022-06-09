package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.*;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Data
public class TeacherViewController implements Initializable {
    @FXML
    public ListView<Teacher> teacherList;

    @FXML
    public BorderPane root;

    public MainController parent;

    private ObservableList<Teacher> teachers;

    private boolean isEdit;

    @FXML
    public ImageView ivAdd, ivRemove, ivEdit;
    private TeacherTimetable visibileTimetable;

    @FXML
    public BorderPane content;
    private HBox topContent;
    private GridPane timetableView;

    @FXML
    public Label lblInfo;
    private boolean blockHours;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacherList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Tooltip on Hover bei ImageViewButtons
        Tooltip.install(ivAdd, new Tooltip("hinzufügen"));
        Tooltip.install(ivRemove, new Tooltip("entfernen"));

        teacherList.setItems(SchoolDB.getInstance().getTeachers());
        blockHours = false;
    }

    public void setParent(MainController parent) {
        this.parent = parent;
    }

    @FXML
    public void addTeacher() {
        isEdit = false;
        dismountForm();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/teacher-form.fxml"));
            VBox vBox = fxmlLoader.load();
            ((TeacherFormController) fxmlLoader.getController()).setParent(this);

            root.setCenter(vBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void removeTeacher() {
        ObservableList<Integer> indices = teacherList.getSelectionModel().getSelectedIndices();
        boolean singular = false;

        if (indices.size() == 0) {
            return;
        } else if (indices.size() == 1) {
            singular = true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("vs-martkhartmannsdorf | LÖSCHEN");
        if (singular) {
            alert.setHeaderText("Wollen Sie den Lehrer löschen");
        } else {
            alert.setHeaderText("Wollen Sie die Lehrer löschen");
        }
        alert.setContentText("Löschen?");
        ButtonType okButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.getButtonData().equals(ButtonBar.ButtonData.YES)) {

                dismountForm();

                indices.stream().sorted(Comparator.reverseOrder()).forEach(i -> {
                    SchoolDB.getInstance().removeTeacher(teacherList.getItems().get(i));
                });

                updateTeacher();
            }
        });

    }

    @FXML
    public void editTeacher() {
        isEdit = true;
        if (teacherList.getSelectionModel().getSelectedIndices().size() == 1) {
            dismountForm();
            try {
                FXMLLoader fxmlLoader = parent.fxmlLoad("demo/teacher-form.fxml");
                VBox vBox = fxmlLoader.load();
                TeacherFormController controller = fxmlLoader.getController();
                controller.setParent(this);

                root.setCenter(vBox);

                Teacher teacher = teacherList.getSelectionModel().getSelectedItem();
                controller.setItemsIfEdited(teacher);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("vs-martkhartmannsdorf | BEARBEITEN");
            alert.setHeaderText("Bitte nur einen Lehrer zum Bearbeiten auswählen!");
            alert.showAndWait();
        }
    }

    public void dismountForm() {
        root.setCenter(null);
    }

    public void submitForm(String firstname, String lastname, String abbrevation, List<Subjectobject> subjects) {
        Teacher teacher = new Teacher(SchoolDB.getInstance().getLastTeacherID(), StringUtils.capitalize(firstname), lastname.toUpperCase(), abbrevation.toUpperCase(), subjects);

        dismountForm();

        SchoolDB.getInstance().addTeacher(teacher);
        updateTeacher();
    }

    public ObservableList<Teacher> getTeachers() {
        return teachers;
    }

    public void updateTeacher() {
        teacherList.setItems(SchoolDB.getInstance().getTeachers());
        teacherList.refresh();
    }

    public void editTeacher(Teacher oldTeacher, String firstname, String lastname, String abbrevation, List<Subjectobject> subjects) {
        Teacher teacher = new Teacher(oldTeacher.getId(), StringUtils.capitalize(firstname), lastname.toUpperCase(), abbrevation.toUpperCase(), subjects);
        dismountForm();
       /* int index = SchoolDB.getInstance().getTeachers().indexOf(oldTeacher);

        SchoolDB.getInstance().getTeachers().set(index, teacher);

        SchoolDB.getInstance().getTeachers().remove(oldTeacher);*/

        oldTeacher.setFirstname(teacher.getFirstname());
        oldTeacher.setSurname(teacher.getSurname());
        oldTeacher.setAbbreviation(teacher.getAbbreviation());
        oldTeacher.setSubjects(teacher.getSubjects());

        updateTeacher();
    }

    @FXML
    public void selectTeacher() {
        Teacher teacher = teacherList.getSelectionModel().getSelectedItem();
        if (teacher != null){
            visibileTimetable = SchoolDB.getInstance().findTeacherTimetableByID(teacher.getId()).get();

            lblInfo.setText(teacher.getSurname().toUpperCase() + " " + teacher.getFirstname());


            buildTimetable();
            setContent();
        }
    }

    private void buildTimetable() {
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
                TeacherLesson teacherLesson = visibileTimetable.getWeeklySubjects().get(Week.A).get(day).get(i);
                if (row == Timetable.MAX_HOURS + 1) {
                    row = 1;
                }

                VBox vBox = new VBox();

                Color color = null;
                if (teacherLesson.getSubject() != null) {
                    color = teacherLesson.getSubject().getColor();
                    vBox.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                if (teacherLesson.isBlocked()){
                    vBox.setStyle("-fx-background-color: #000000");
                    Label label = new Label("BLOCKIERT");
                    label.setStyle("-fx-text-fill: white;-fx-font-weight: bold");
                    vBox.getChildren().add(label);
                }


                vBox.setPadding(new Insets(10, 10, 10, 10));

                Label lblSubject;

                int finalI = i;
                int finalI1 = i;
                vBox.setOnMouseClicked(mouseEvent -> {
                    if (blockHours){
                        if (visibileTimetable.getWeeklySubjects().get(Week.A).get(day).get(finalI1).isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("vs-martkhartmannsdorf | BLOCKIEREN");
                            alert.setHeaderText("Wollen Sie wirklich diese Stunde blockieren?");
                            Optional<ButtonType> answer = alert.showAndWait();

                            if (answer.get().equals(ButtonType.OK)){
                                visibileTimetable.addSubject(day, finalI, new TeacherLesson(true), Week.A);
                                buildTimetable();
                                setContent();
                            }
                        }else if(visibileTimetable.getWeeklySubjects().get(Week.A).get(day).get(finalI1).isBlocked()){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("vs-martkhartmannsdorf | FREIGEBEN");
                            alert.setHeaderText("Wollen Sie wirklich diese Stunde freigeben?");
                            Optional<ButtonType> answer = alert.showAndWait();

                            if (answer.get().equals(ButtonType.OK)){
                                visibileTimetable.addSubject(day, finalI, new TeacherLesson(), Week.A);
                                buildTimetable();
                                setContent();
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("vs-martkhartmannsdorf | BLOCKIEREN");
                            alert.setHeaderText("Blockieren einer belegten Stunde nicht möglich!");
                            alert.show();
                        }
                    }
                });

                lblSubject = teacherLesson.getSubject() == null ? new Label(" ") : new Label(teacherLesson.getSubject().getName());
                if (color != null) {
                    double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
                    if (luminance > 0.002) {
                        lblSubject.setStyle("-fx-text-fill: black;-fx-font-weight: bold");
                    } else {
                        lblSubject.setStyle("-fx-text-fill: white;-fx-font-weight: bold");
                    }

                    vBox.getChildren().add(lblSubject);
                }

                timetableView.add(vBox, column, row);
                row++;
            }
            column++;
        }
        addStyle();
    }

    private void setContent() {
        content.setCenter(timetableView);
        root.setCenter(content);
    }

    public void addStyle() {
        timetableView.setGridLinesVisible(true);
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

    public void clearRoot(){
        root.setCenter(null);
    }

    @FXML
    public void onClickBlock(){
        if (blockHours){
            blockHours = false;

            Teacher teacher = teacherList.getSelectionModel().getSelectedItem();

            lblInfo.setText("Bearbeiten deaktiviert | " + teacher.getSurname().toUpperCase() + " " + teacher.getFirstname());
        } else {
            blockHours = true;

            Teacher teacher = teacherList.getSelectionModel().getSelectedItem();

            lblInfo.setText("Bearbeiten aktiviert | " + teacher.getSurname().toUpperCase() + " " + teacher.getFirstname());
        }
    }

}
