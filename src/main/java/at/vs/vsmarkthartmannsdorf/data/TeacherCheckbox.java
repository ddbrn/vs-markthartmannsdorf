package at.vs.vsmarkthartmannsdorf.data;

import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.scene.control.CheckBox;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherCheckbox extends CheckBox {
    private TeacherSubject teacherSubject;
    public TeacherCheckbox(TeacherSubject teacherSubject) {
        this.teacherSubject = teacherSubject;

        setText(SchoolDB.getInstance().getTeacherByID(teacherSubject.getTeacherId()).get().getSurname()
                + " " + SchoolDB.getInstance().getTeacherByID(teacherSubject.getTeacherId()).get().getFirstname());
    }
}
