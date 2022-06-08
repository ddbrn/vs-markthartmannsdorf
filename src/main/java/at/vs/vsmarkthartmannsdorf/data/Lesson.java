package at.vs.vsmarkthartmannsdorf.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Lesson {
    private Subjectobject subject;
    private List<TeacherSubject> teacher;
    private boolean isEmptyLesson;

    public Lesson() {
        isEmptyLesson = true;

        subject = null;
        teacher = new ArrayList<>();
    }

    public Lesson(Subjectobject subject, List<TeacherSubject> teacherSubject) {
        isEmptyLesson = false;

        this.teacher = new ArrayList<>();

        this.subject = subject;
        this.teacher.addAll(teacherSubject);
    }

    public void addTeacher(TeacherSubject teacherSubject){
        teacher.add(teacherSubject);
    }

    public void removeTeacher(TeacherSubject teacherSubject){
        teacher.remove(teacherSubject);
    }


}
