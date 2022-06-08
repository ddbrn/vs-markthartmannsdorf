package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

@Data
public class TeacherLesson {
    private Subject subject;
    private int classID;
    private boolean isEmpty;
    private boolean isBlocked;

    public TeacherLesson(Subject subject, int classID) {
        this.subject = subject;
        this.classID = classID;
        this.isEmpty = false;
        this.isBlocked = false;
    }

    public TeacherLesson(){
        this.isEmpty = true;
        this.isBlocked = false;
    }

    public TeacherLesson(boolean isBlocked){
        this.isBlocked = isBlocked;
    }
}
