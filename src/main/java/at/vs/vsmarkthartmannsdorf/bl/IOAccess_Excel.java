package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class IOAccess_Excel {

    private static DirectoryChooser directoryChooser = new DirectoryChooser();
    private static Stage stage;

    public static void setStage(Stage stage) {
        IOAccess_Excel.stage = stage;
    }

    public static void createExcelFile(List<Teacher> teacherList, List<SchoolClass> schoolClassList) {
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            return;
        }

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet("Lehrer");

        String[] header = {
                "Vorname", "Nachname", "Kürzel", "Fächer"
        };

        int rowCount = 0;
        int columCount = 0;

        XSSFRow row = sheet.createRow(rowCount++);
        for (String head: header) {
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(head);
        }

        for (Teacher teacher:teacherList) {
            row = sheet.createRow(rowCount++);
            columCount = 0;
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(teacher.getFirstname());

            cell = row.createCell(columCount++);
            cell.setCellValue(teacher.getSurname());

            cell = row.createCell(columCount++);
            cell.setCellValue(teacher.getAbbreviation());

            cell = row.createCell(columCount);
            cell.setCellValue(teacher.getSubjectsForExcel());
        }


        sheet = wb.createSheet("Klassen");


        String[] headerClass = {
                "Klassen Name", "Klassenvorstand"
        };

        rowCount = 0;
        columCount = 0;

        row = sheet.createRow(rowCount++);
        for (String head: headerClass) {
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(head);
        }

        for (SchoolClass schoolClass:schoolClassList) {
            row = sheet.createRow(rowCount++);
            columCount = 0;
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(schoolClass.getClassname());

            cell = row.createCell(columCount);
            cell.setCellValue(schoolClass.getFormattedTeacher());

        }


        try (FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "\\vs-markthartmannsdorf-data.xlsx")) {
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
