package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StartController {

    @FXML
    public ListView<Teacher> teacherList;
    public ListView<SchoolClass> classList;
    public ListView<Timetable> timetableList;
    public ListView<Subject> timetableSubjects;
    public GridPane teacherAbsence;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<SchoolClass> classes = FXCollections.observableArrayList();
    private ObservableList<Timetable> timetables = FXCollections.observableArrayList();
    private ObservableList<Subject> timetableSubs = FXCollections.observableArrayList();
    private ArrayList<TeacherAbsence> teacherAbsenceList = new ArrayList<>();

    @FXML
    protected void onAddTeacher() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("teacher-dialog.fxml"));
            DialogPane teacherDialog = fxmlLoader.load();


            List<Subject> subjects = Arrays.asList(Subject.values());

            TeacherController teacherController = fxmlLoader.getController();
            teacherController.setSubjects(subjects);

            String firstname = "";
            String surname = "";
            String abbreviation = "";
            List<Subject> assignedSubjects = new ArrayList<>();
            boolean alreadyContainsAbbreviation = false;

            do {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(teacherDialog);
                dialog.setTitle("Lehrer hinzufügen");

                Optional<ButtonType> clickedButton = dialog.showAndWait();
                if (clickedButton.get() == ButtonType.APPLY) {
                    firstname = teacherController.getFirstname().getText();
                    surname = teacherController.getSurname().getText();
                    abbreviation = teacherController.getAbbreviation().getText();
                    assignedSubjects = teacherController.getAssignedSubjects();

                    String finalAbbreviation = abbreviation.toLowerCase();
                    alreadyContainsAbbreviation = teachers.stream().anyMatch(teacher -> teacher.getAbbreviation().toLowerCase().equals(finalAbbreviation));

                    if (!(firstname.isEmpty() || surname.isEmpty() || abbreviation.isEmpty() || assignedSubjects.isEmpty() || alreadyContainsAbbreviation)) {
                        teachers.add(new Teacher(firstname, surname, abbreviation, assignedSubjects));
                    } else {
                        if (firstname.isEmpty()) {
                            teacherController.setFirstnameVisibility(true);
                        } else {
                            teacherController.setFirstnameVisibility(false);
                            teacherController.setFirstnameText(firstname);
                        }

                        if (surname.isEmpty()) {
                            teacherController.setSurnameVisibility(true);
                        } else {
                            teacherController.setSurnameVisibility(false);
                            teacherController.setSurnameText(surname);
                        }
                        if (abbreviation.isEmpty()) {
                            teacherController.setAbbreviationVisibility(true);
                        } else {
                            teacherController.setAbbreviationVisibility(false);
                            teacherController.setAbbreviationText(abbreviation);
                        }
                        if (assignedSubjects.isEmpty()) {
                            teacherController.setAssignedSubjectsVisibility(true);
                        } else {
                            teacherController.setAssignedSubjectsVisibility(false);
                            teacherController.setAssignedSubjects(assignedSubjects);
                        }
                        if (alreadyContainsAbbreviation) {
                            teacherController.setAbbreviationVisibility(true);
                            teacherController.setAbbreviationLabelText("Dieses Kürzel gibt es bereits!");
                        }
                    }
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    break;
                }
            } while ((firstname.isEmpty() || surname.isEmpty() || abbreviation.isEmpty() || assignedSubjects.isEmpty() || alreadyContainsAbbreviation));
        } catch (IOException exception) {
            System.out.println("Datei nicht gefunden");
        }

        teacherList.setItems(teachers);
        loadAbsence();
    }

    @FXML
    protected void onRemoveTeacher() {
        int index = teacherList.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            teachers.remove(index);
            teacherList.setItems(teachers);
        }
        loadAbsence();
    }

    @FXML
    protected void onAddClass() {
        try {
            FXMLLoader classLoader = new FXMLLoader();
            classLoader.setLocation(getClass().getResource("class-dialog.fxml"));

            DialogPane classPane = classLoader.load();
            ClassController classController = classLoader.getController();

            String className = "";
            Teacher teacher = null;
            boolean classExists = false;
            classController.setTeachers(teachers);

            do {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(classPane);
                dialog.setTitle("Klasse hinzufügen");
                Optional<ButtonType> clickedButton = dialog.showAndWait();

                if (clickedButton.get() == ButtonType.APPLY) {
                    className = classController.getClassName().getText();
                    teacher = classController.getTeachers().getValue();

                    String finalClassName = className;
                    classExists = classes.stream().anyMatch(schoolClass -> schoolClass.getClassname().equals(finalClassName));

                    if (!className.isEmpty()
                            && teacher != null && !classExists) {
                        classes.add(new SchoolClass(classController.getClassName().getText(),
                                classController.getTeachers().getValue()));
                        timetables.add(new Timetable(new SchoolClass(classController.getClassName().getText(),
                                classController.getTeachers().getValue())));
                    } else {
                        if (className.isEmpty()) {
                            classController.getClassLabel().setVisible(true);
                            classController.getClassLabel().setText("Klassenname darf nicht leer sein!");
                        } else {
                            classController.getClassName().setText(className);
                            classController.getClassLabel().setVisible(false);
                        }
                        if (teacher == null) {
                            classController.getTeacherLabel().setVisible(true);
                        } else {
                            classController.getTeachers().setValue(teacher);
                            classController.getTeacherLabel().setVisible(false);
                        }
                        if (classExists) {
                            classController.getClassLabel().setVisible(true);
                            classController.getClassLabel().setText("Klassenname existiert bereits!");
                        }
                    }
                }
                if (clickedButton.get() == ButtonType.CANCEL) {
                    break;
                }
            } while (classController.getClassName().getText().isEmpty() || classController.getTeachers().getValue() == null || classExists);

        } catch (IOException e) {
            e.printStackTrace();
        }
        classList.setItems(classes);
        timetableList.setItems(timetables);
    }

    @FXML
    protected void onRemoveClass() {
        int index = classList.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            classes.remove(index);
            classList.setItems(classes);
        }

    }

    @FXML
    protected void onChangeTimetable() {

        if (timetableList.getSelectionModel().isEmpty()) {
            System.out.println("Keine Klasse ausgewählt bzw. keine erstellt");
        } else {

            try {

                FXMLLoader TableLoader = new FXMLLoader();
                TableLoader.setLocation(getClass().getResource("timetable-dialog.fxml"));
                DialogPane timeTableDialog = TableLoader.load();

                TimetableController timetableController = TableLoader.getController();
                timetableController.setTeacher(teachers);

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(timeTableDialog);

                dialog.setTitle("Stundenplan bearbeiten");
                Optional<ButtonType> clickedButton = dialog.showAndWait();
                if (clickedButton.get() == ButtonType.APPLY) {
                    timetables.get(timetableList.getSelectionModel().getSelectedIndex()).setContent(timetableController.getAssignedTeacher());
                    System.out.println("Timetable wurde erstellt");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Subject> usedSubjects = timetables.get(timetableList.getSelectionModel().getSelectedIndex())
                    .getteachersubjects();
            System.out.println("Alle verfügbaren Fächer von Timetable " +
                    timetableList.getSelectionModel().getSelectedIndex()+1 + ":" + usedSubjects);
          //  timetableSubjectsList.setItems((ObservableList<Subject>) usedSubjects); Liste links / Alle verfügbaren Fächer anzeigen lassen

        }
    }

    @FXML
    protected void deleteContent() {
        int index = timetableList.getSelectionModel().getSelectedIndex();
        try {
            timetables.get(index).deleteContent();
            System.out.println(timetables.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = FXCollections.observableArrayList(teachers);
        teacherList.setItems(this.teachers);
    }

    public void setClasses(List<SchoolClass> classes) {
        this.classes = FXCollections.observableArrayList(classes);
        classList.setItems(this.classes);
    }

    public List<Teacher> getTeacher() {
        return teachers.stream().toList();
    }

    public List<SchoolClass> getClasses() {
        return classes.stream().toList();
    }

    @FXML
    public void onEditTeacher() {
        int index = teacherList.getSelectionModel().getSelectedIndex();
        Teacher teacher = teacherList.getSelectionModel().getSelectedItem();

        if(teacher != null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("teacher-dialog.fxml"));
                DialogPane teacherDialog = fxmlLoader.load();

                List<Subject> subjects = Arrays.asList(Subject.values());

                TeacherController teacherController = fxmlLoader.getController();
                teacherController.setSubjects(subjects);

                teacherController.setFirstnameText(teacher.getFirstname());
                teacherController.setSurnameText(teacher.getSurname());
                teacherController.setAbbreviationText(teacher.getAbbreviation());
                teacherController.setAssignedSubjects(teacher.getSubjects());

                String firstname = "";
                String surname = "";
                String abbreviation = "";
                List<Subject> assignedSubjects = new ArrayList<>();
                boolean alreadyContainsAbbreviation = false;

                do {
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(teacherDialog);
                    dialog.setTitle("Lehrer bearbeiten");

                    Optional<ButtonType> clickedButton = dialog.showAndWait();
                    if(clickedButton.get() == ButtonType.APPLY){
                        firstname = teacherController.getFirstname().getText();
                        surname = teacherController.getSurname().getText();
                        abbreviation = teacherController.getAbbreviation().getText();
                        assignedSubjects = teacherController.getAssignedSubjects();
                        String finalAbbreviation = abbreviation.toLowerCase();

                        alreadyContainsAbbreviation = teachers.stream().anyMatch(t -> {
                            if(teacher.getAbbreviation().toLowerCase().equals(finalAbbreviation)){
                                return false;
                            }else if(t.getAbbreviation().toLowerCase().equals(finalAbbreviation)){
                                return true;
                            }
                            return false;
                        });

                        if (!(firstname.isEmpty() || surname.isEmpty() || abbreviation.isEmpty() || assignedSubjects.isEmpty() || alreadyContainsAbbreviation)) {
                            teacher.setFirstname(firstname);
                            teacher.setSurname(surname);
                            teacher.setAbbreviation(abbreviation);
                            teacher.setSubjects(assignedSubjects);

                            teachers.remove(index);
                            teachers.add(index, teacher);
                        } else {
                            checkTeacherInput(teacherController, firstname, surname, abbreviation, alreadyContainsAbbreviation, assignedSubjects);
                        }
                    }
                    else if (clickedButton.get() == ButtonType.CANCEL){
                        break;
                    }
                } while (firstname.isEmpty() || surname.isEmpty() || abbreviation.isEmpty() || assignedSubjects.isEmpty() || alreadyContainsAbbreviation);
            } catch (IOException exception) {
                System.out.println("Datei nicht gefunden");
            }

            teacherList.setItems(teachers);
        }
    }

    private void checkTeacherInput(TeacherController teacherController, String firstname, String surname,
                                   String abbreviation, boolean alreadyContainsAbbreviation,
                                   List<Subject> assignedSubjects) {
        if (firstname.isEmpty()) {
            teacherController.setFirstnameVisibility(true);
        } else {
            teacherController.setFirstnameVisibility(false);
            teacherController.setFirstnameText(firstname);
        }

        if (surname.isEmpty()) {
            teacherController.setSurnameVisibility(true);
        } else {
            teacherController.setSurnameVisibility(false);
            teacherController.setSurnameText(surname);
        }
        if (abbreviation.isEmpty()) {
            teacherController.setAbbreviationVisibility(true);
        } else {
            teacherController.setAbbreviationVisibility(false);
            teacherController.setAbbreviationText(abbreviation);
        }
        if (assignedSubjects.isEmpty()) {
            teacherController.setAssignedSubjectsVisibility(true);
        } else {
            teacherController.setAssignedSubjectsVisibility(false);
            teacherController.setAssignedSubjects(assignedSubjects);
        }
        if (alreadyContainsAbbreviation) {
            teacherController.setAbbreviationVisibility(true);
            teacherController.setAbbreviationLabelText("Dieses Kürzel gibt es bereits!");
        }
    }

    @FXML
    protected void onChangeClass() {
        int index = classList.getSelectionModel().getSelectedIndex();

        try {
            FXMLLoader classLoader = new FXMLLoader();
            classLoader.setLocation(getClass().getResource("class-dialog.fxml"));

            DialogPane classPane = classLoader.load();
            ClassController classController = classLoader.getController();

            SchoolClass schoolClass = classes.get(index);
            String oldClassName = classes.get(index).getClassname();
            Teacher oldTeacher = classes.get(index).getTeacher();
            boolean classExists = false;

            String newClassname = "";
            Teacher newTeacher = null;
            classController.setTeachers(teachers);
            classController.setSelectedTeacher(oldTeacher);
            classController.setClassName(oldClassName);

            do {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(classPane);
                dialog.setTitle("Klasse ändern");
                Optional<ButtonType> clickedButton = dialog.showAndWait();

                if (clickedButton.get() == ButtonType.APPLY) {
                    newClassname = classController.getClassName().getText();
                    newTeacher = classController.getTeachers().getValue();

                    String finalClassName = newClassname;
                    classExists = classes.stream().anyMatch(schoolClass1 -> schoolClass1.getClassname().equals(finalClassName));


                    if (!newClassname.isEmpty() && newTeacher != null && !classExists) {
                        schoolClass.setClassname(classController.getClassName().getText());
                        schoolClass.setTeacher(classController.getTeachers().getValue());

                        classList.setItems(classes);
                        classList.refresh();
                    }else{
                        if(newClassname.isEmpty()){
                            classController.getClassLabel().setVisible(true);
                            classController.getClassLabel().setText("Klassenname darf nicht leer sein!");
                        } else {
                            classController.getClassName().setText(newClassname);
                            classController.getClassLabel().setVisible(false);
                        }
                        if(newTeacher == null){
                            classController.getTeacherLabel().setVisible(true);
                        }else{
                            classController.getTeachers().setValue(newTeacher);
                            classController.getTeacherLabel().setVisible(false);
                        }
                        if(classExists){
                            classController.getClassLabel().setVisible(true);
                            classController.getClassLabel().setText("Klassenname existiert bereits!");
                        }
                    }

                }
                if(clickedButton.get() == ButtonType.CANCEL){
                    break;
                }

            } while (newClassname.isEmpty() || newTeacher == null || classExists);
        } catch (Exception e) {

        }
    }

    public void loadAbsence() {
        teacherAbsenceList.clear();
        for (Teacher teacher : teachers) {
            teacherAbsenceList.add(new TeacherAbsence(teacher, false));
        }

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < teacherAbsenceList.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("absenceitem.fxml"));
                VBox vBox = fxmlLoader.load();

                TeacherAbsenceController teacherAbsenceController = fxmlLoader.getController();
                teacherAbsenceController.setStartController(this);
                teacherAbsenceController.setData(teacherAbsenceList.get(i));

                if (column == 5) {
                    column = 0;
                    row++;
                }

                teacherAbsence.add(vBox, column++, row);
                GridPane.setMargin(vBox, new Insets(5));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void teacherChangedAbsentStatus(TeacherAbsence teacherAbsence){
        teacherAbsenceList.stream().filter(t -> t.getTeacher()
                .getAbbreviation() == teacherAbsence.getTeacher()
                .getAbbreviation()).findFirst().get()
                .setAbsent(teacherAbsence.isAbsent());
    }
}