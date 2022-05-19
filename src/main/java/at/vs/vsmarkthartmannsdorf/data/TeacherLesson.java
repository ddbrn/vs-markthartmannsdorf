package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

@Data
public class TeacherLesson {
    private Subject subject;
    private int classID;
    private boolean isEmpty;

    public TeacherLesson(Subject subject, int classID) {
        this.subject = subject;
        this.classID = classID;
        this.isEmpty = false;
    }

    public TeacherLesson(){
        this.isEmpty = true;
    }
}
