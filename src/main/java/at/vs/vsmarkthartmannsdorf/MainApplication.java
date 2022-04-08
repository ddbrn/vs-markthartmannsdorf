package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.bl.*;
import at.vs.vsmarkthartmannsdorf.data.TeacherAbsence;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;

public class MainApplication extends Application {

    MainController controller;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("demo/start.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("demo/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Stundenplaner");
        stage.setScene(scene);
        stage.show();

        controller = fxmlLoader.getController();
        controller.setTeachers(IOAccess.readTeacherFiles());
        controller.setClasses(IOAccess.readClassFiles());
        controller.setStage(stage);

        IOAccess_Absence.setStage(stage);
        IOAccess_Excel.setStage(stage);
        IOAccess_PDF.setStage(stage);
        IOAccess_Print.setStage(stage);
        IOAccess.readTimetableFiles();
        IOAccess.readAbsenceFiles();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("CLOSED WINDOW");

        IOAccess.storeTeacherFiles(SchoolDB.getInstance().getTeachers());
        IOAccess.storeClassFiles(SchoolDB.getInstance().getSchoolClasses());
        IOAccess.storeTimetableFiles();
        IOAccess.storeAbsenceFiles();

        //IOAccess_Excel.createExcelFile(controller.getTeacher(), controller.getClasses());
    }
}
