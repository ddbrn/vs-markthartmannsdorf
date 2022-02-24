package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.PropertiesLoader;
import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfDirectory.setText(PropertiesLoader.getInstance().getProperties().getProperty(PropertyName.export_folder.toString()));

        for (Subject subject: Subject.values()){
            HBox hbox = new HBox();

            hbox.getChildren().add(new Label(subject.name()));
            hbox.getChildren().add(new ColorPicker());
            vbProperties.getChildren().add(hbox);
        }
    }

    @FXML
    public void setDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(stage);

        if (dir != null){
            tfDirectory.setText(dir.toString());
            PropertiesLoader.getInstance().addProperty(PropertyName.export_folder, dir.toString());
        }
    }


}
