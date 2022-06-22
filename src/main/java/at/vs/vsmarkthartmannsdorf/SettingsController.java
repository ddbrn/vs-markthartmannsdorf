package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Data
public class SettingsController implements Initializable {
    private Stage stage;

    @FXML
    public TextField tfDirectory;
    public VBox vbProperties;
    public Button deleteButton;
    public HBox hBox = new HBox();
    public GridPane gridPane = new GridPane();
    public ColorPicker colorPicker = new ColorPicker();
    public Label lblDirectory = new Label("Standardordner");
    public TextField txtDirectory = new TextField(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.export_folder.toString()));
    public Button btnDirectory = new Button("auswählen");
    public Button addButton;

    public static final int MAX_STUNDEN = 9;

    private ObservableList<Integer> olHours = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
        /*
        for (Subject subject: Subject.values()){
            HBox hBox = new HBox();

            GridPane gridPane = new GridPane();

            gridPane.setAlignment(Pos.CENTER);
            gridPane.add(new Label(subject.name()), 0, 0);

            // Initialize ColorPicker
            ColorPicker colorPicker = new ColorPicker();
            String property = PropertiesLoader.getInstance().getProperties().getProperty(subject.name());
            if (property != null){
                colorPicker.setValue(Color.valueOf(property));
            }
            colorPicker.setOnAction(actionEvent -> changedColor(colorPicker, subject));

            gridPane.add(colorPicker, 1, 0);

            hBox.prefWidthProperty().bind(vbProperties.widthProperty());
            gridPane.prefWidthProperty().bind(hBox.widthProperty());
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(50);
            gridPane.getColumnConstraints().add(columnConstraints);
            hBox.getChildren().add(gridPane);


            vbProperties.getChildren().add(hBox);
        }
        */
    }

    private void deleteSubject(int index) {
        if(!SchoolDB.getInstance().subjectexistsinanytimetable(SchoolDB.getInstance().getSubjects().get(index)))
        SchoolDB.getInstance().getSubjects().remove(index);
        for(int i = 0;i<SchoolDB.getInstance().getSubjects().size();i++)
            System.out.println(SchoolDB.getInstance().getSubjects().get(i).getName());
        update();
    }

    @FXML
    public void setDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(stage);

        if (dir != null){
            System.out.println(dir.toString());
            txtDirectory.setText(dir.toString());
            PropertiesLoader.getInstance().addProperty(PropertyName.export_folder.name(), dir.toString());
        }
    }
    public void update(){
        hBox.getChildren().clear();
        gridPane.getChildren().clear();
        vbProperties.getChildren().clear();
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(44.0);
        hBox.setPrefWidth(200.0);
        hBox.setSpacing(20.0);
        btnDirectory.setOnAction(actionEvent -> setDirectory());
        hBox.getChildren().add(lblDirectory);
        hBox.getChildren().add(txtDirectory);
        hBox.getChildren().add(btnDirectory);
        vbProperties.getChildren().add(hBox);
        for (int i = 0;i<SchoolDB.getInstance().getSubjects().size();i++){
            hBox = new HBox();
            gridPane = new GridPane();
            deleteButton = new Button("-");
            gridPane.setAlignment(Pos.CENTER);
            gridPane.add(new Label(SchoolDB.getInstance().getSubjects().get(i).getName()), 0, 0);
            System.out.println(SchoolDB.getInstance().getSubjects().get(i).getName());
            int j;
            // Initialize ColorPicker
            ColorPicker colorPicker = new ColorPicker();
            String property = PropertiesLoader.getInstance().getProperties().getProperty(SchoolDB.getInstance().getSubjects().get(i).getName());
            if (property != null){
                colorPicker.setValue(Color.valueOf(property));
            } else {
                colorPicker.setValue(SchoolDB.getInstance().getSubjects().get(i).getColor());
            }
            j = i;
            colorPicker.setOnAction(actionEvent -> changedColor(colorPicker, j));
            deleteButton.setOnAction(actionEvent -> deleteSubject(j));
            gridPane.add(colorPicker, 1, 0);
            gridPane.add(deleteButton, 2,0);

            hBox.prefWidthProperty().bind(vbProperties.widthProperty());
            gridPane.prefWidthProperty().bind(hBox.widthProperty());
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(50);
            gridPane.getColumnConstraints().add(columnConstraints);
            hBox.getChildren().add(gridPane);
            vbProperties.getChildren().add(hBox);
        }
        hBox = new HBox();
        addButton = new Button("+");
        hBox.setAlignment(Pos.CENTER);
        addButton.setOnAction(actionEvent -> {try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("demo/subject-dialog.fxml"));
            DialogPane addSubjectDialog = fxmlLoader.load();

            SubjectFormController subjectFormController = fxmlLoader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(addSubjectDialog);
            dialog.setTitle("Fach hinzufügen");
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.OK) {
                if(SchoolDB.getInstance().subjectalreadyexist(subjectFormController.getTxtSubject().getText())){
                }else
                    SchoolDB.getInstance().addSubject(subjectFormController.getTxtSubject().getText(),
                            subjectFormController.getClrPicker().getValue().getRed(),
                            subjectFormController.getClrPicker().getValue().getGreen(),
                            subjectFormController.getClrPicker().getValue().getBlue());
                PropertiesLoader.getInstance().addProperty(subjectFormController.getTxtSubject().getText(),
                        subjectFormController.getClrPicker().getValue().toString());
                update();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        });
        hBox.prefWidthProperty().bind(vbProperties.widthProperty());
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        hBox.getChildren().add(addButton);
        vbProperties.getChildren().add(hBox);
    }

    public void changedColor(ColorPicker colorPicker, int index){
        PropertiesLoader.getInstance().addProperty(SchoolDB.getInstance().getSubjects().get(index).getName(), colorPicker.getValue().toString());
        SchoolDB.getInstance().getSubjects().get(index).setColor(colorPicker.getValue());
    }
}
