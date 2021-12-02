package at.vs.vsmarkthartmannsdorf.data;

public class SchoolClass {
    private String classname;
    private Teacher teacher;

    public SchoolClass(String classname, Teacher teacher) {
        this.classname = classname;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", classname.toUpperCase(), teacher.toString());
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
