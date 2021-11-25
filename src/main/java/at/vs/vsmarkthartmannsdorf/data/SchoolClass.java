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
}
