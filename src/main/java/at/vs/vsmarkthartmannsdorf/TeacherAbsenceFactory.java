package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TeacherAbsenceFactory implements Callback<ListView<Teacher>, ListCell<Teacher>> {
    @Override
    public ListCell<Teacher> call(ListView<Teacher> param){
        return new TeacherAbsenceCell();
    }
}
