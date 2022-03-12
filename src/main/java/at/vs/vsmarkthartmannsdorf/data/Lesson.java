package at.vs.vsmarkthartmannsdorf.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Lesson {
    private Subject subject;
    private List<TeacherSubject> teacher;
    private boolean isEmptyLesson;

    public Lesson() {
        isEmptyLesson = true;

        subject = null;
        teacher = new ArrayList<>();
    }

    public Lesson(Subject subject, List<TeacherSubject> teacherSubject) {
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
