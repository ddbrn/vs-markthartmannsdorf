package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

@Data
public class TeacherLesson {
    private Subjectobject subject;
    private int classID;
    private boolean isEmpty;
    private boolean isBlocked;

    public TeacherLesson(Subjectobject subject, int classID) {
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
