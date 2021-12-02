package at.vs.vsmarkthartmannsdorf;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import at.vs.vsmarkthartmannsdorf.data.Timetable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.*;

public class StartController {

    @FXML
    public ListView<Teacher> teacherList;
    public ListView<SchoolClass> classList;
    public ListView<Timetable> timetableList;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<SchoolClass> classes = FXCollections.observableArrayList();
    private ObservableList<Timetable> timetables = FXCollections.observableArrayList();

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

                    String finalAbbreviation = abbreviation;
                    alreadyContainsAbbreviation = teachers.stream().anyMatch(teacher -> teacher.getAbbreviation().equals(finalAbbreviation));

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
    }

    @FXML
    protected void onRemoveTeacher() {
        int index = teacherList.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            teachers.remove(index);
            teacherList.setItems(teachers);
        }
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
            classController.setTeachers(teachers);

            do {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(classPane);
                dialog.setTitle("Klasse hinzufügen");
                Optional<ButtonType> clickedButton = dialog.showAndWait();

                if (clickedButton.get() == ButtonType.APPLY) {
                    className = classController.getClassName().getText();
                    teacher = classController.getTeachers().getValue();
                    if (!className.isEmpty()
                            && teacher != null) {
                        classes.add(new SchoolClass(classController.getClassName().getText(),
                                classController.getTeachers().getValue()));
                    } else {
                        if (className.isEmpty()) {
                            classController.getClassLabel().setVisible(true);
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
                    }
                }
                if (clickedButton.get() == ButtonType.CANCEL) {
                    break;
                }
            } while (classController.getClassName().getText().isEmpty() || classController.getTeachers().getValue() == null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        classList.setItems(classes);
    }

    @FXML
    protected void onRemoveClass() {
        int index = classList.getSelectionModel().getSelectedIndex();

        classes.remove(index);
        classList.setItems(classes);
    }

    @FXML
    protected void onAddTimetable() {
        Timetable timetable1 = new Timetable("AHIF18", null);

        timetables.add(timetable1);

        timetableList.setItems(timetables);
    }

    @FXML
    protected void onRemoveTimetable() {
        int index = timetableList.getSelectionModel().getSelectedIndex();

        timetables.remove(index);
        timetableList.setItems(timetables);
    }
}