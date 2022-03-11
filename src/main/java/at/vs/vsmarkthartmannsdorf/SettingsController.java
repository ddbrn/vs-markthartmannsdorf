package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Data
public class SettingsController implements Initializable {
    private Stage stage;

    @FXML
    public TextField tfDirectory;
    public VBox vbProperties;
    public ComboBox cbHours;

    public static final int MAX_STUNDEN = 9;

    private ObservableList<Integer> olHours = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfDirectory.setText(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.export_folder.toString()));

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

        for (int i = 1; i <= MAX_STUNDEN; i++){
            olHours.add(i);
        }
        cbHours.setItems(olHours);
        cbHours.getSelectionModel().select(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.max_stunden.name()));
    }

    @FXML
    public void setDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(stage);

        if (dir != null){
            tfDirectory.setText(dir.toString());
            PropertiesLoader.getInstance().addProperty(PropertyName.export_folder.name(), dir.toString());
        }
    }


    public void changedColor(ColorPicker colorPicker, Subject subject){
        PropertiesLoader.getInstance().addProperty(subject.name(), colorPicker.getValue().toString());
    }

    @FXML
    public void onSelectHours(){
       int selectedItem = (Integer) cbHours.getSelectionModel().getSelectedItem();
       PropertiesLoader.getInstance().addProperty(PropertyName.max_stunden.name(), selectedItem + "");
    }
}
