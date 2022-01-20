package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.controller.StartController;
import at.vs.vsmarkthartmannsdorf.bl.IOAccess;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class StartApplication extends Application {

    StartController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Stundenplaner");
        stage.setScene(scene);
        stage.show();

        controller = fxmlLoader.getController();
        controller.setTeachers(IOAccess.readTeacherFiles());
        controller.setClasses(IOAccess.readClassFiles());
        //controller.setTimetableList(IOAccess.readTimetableFiles());
    }

    @Override
    public void stop() throws Exception {
        System.out.println("CLOSED WINDOW");

        IOAccess.storeTeacherFiles(controller.getTeacher());
        IOAccess.storeClassFiles(controller.getClasses());
        //IOAccess.storeTimetableFiles(controller.getTimetables());
    }

    public static void main(String[] args) {
        launch();
    }
}