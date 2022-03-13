package at.vs.vsmarkthartmannsdorf.data;

import javafx.scene.control.CheckBox;
import lombok.Data;

@Data
public class TeacherCheckbox extends CheckBox {
    private TeacherSubject teacherSubject;
    public TeacherCheckbox(TeacherSubject teacherSubject) {
        this.teacherSubject = teacherSubject;
        setText(teacherSubject.getTeacher().getSurname() + " " + teacherSubject.getTeacher().getFirstname());
    }
}
