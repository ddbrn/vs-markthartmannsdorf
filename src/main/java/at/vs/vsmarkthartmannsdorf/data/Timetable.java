package at.vs.vsmarkthartmannsdorf.data;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private String Classname;
    List<Teacher> tlist ;


    public void setContent(List<Teacher> tlist, String Classname){
        this.Classname = Classname;
        this.tlist = tlist;
    }

    public void deleteContent(){
        tlist.clear();
    }

    @Override
    public String toString() {
        return Classname ;
    }



    //ArrayList<Subject>

}