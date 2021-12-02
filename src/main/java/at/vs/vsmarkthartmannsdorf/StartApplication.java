package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.IOAccess;
import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Stundenplaner");
        stage.setScene(scene);
        stage.show();

        List<SchoolClass> schoolClassList = new ArrayList<>();

        schoolClassList.add(new SchoolClass("AHIF", new Teacher("Simon", "Schöggler", "SGR", new ArrayList<>())));
        schoolClassList.add(new SchoolClass("BHIF", new Teacher("David", "Brannan", "BRN", new ArrayList<>())));
        //we
        System.out.println(schoolClassList);

        IOAccess.storeClassFiles(schoolClassList);
        System.out.println(IOAccess.readClassFiles());



    }

    public static void main(String[] args) {
        launch();
    }
}