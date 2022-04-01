package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;

@Data
public class IOAccess_Absence {
    private static Stage stage;

    public static void setStage(Stage stage) {
        IOAccess_Absence.stage = stage;
    }

    public static void storeAbsence () {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(PropertiesLoader.getInstance().getProperties().get(PropertyName.export_folder.name()).toString()));
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            return;
        }

        System.out.println(SchoolDB.getInstance().getTeacherAbsences());
        System.out.println(IOAccess.storeAbsenceFiles());
    }
}
