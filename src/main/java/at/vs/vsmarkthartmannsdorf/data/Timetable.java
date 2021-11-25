package at.vs.vsmarkthartmannsdorf.data;

import javafx.scene.control.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Timetable {
    private String Classname;
    ListView<Subject> tlist ;
    public Timetable(String classname, ListView<Subject> tlist) {
        Classname = classname;
        this.tlist = tlist;
    }

    @Override
    public String toString() {
        return Classname ;
    }



    //ArrayList<Subject>

}