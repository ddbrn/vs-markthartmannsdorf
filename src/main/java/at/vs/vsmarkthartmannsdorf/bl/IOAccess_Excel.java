package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class IOAccess_Excel {

    private static final DirectoryChooser directoryChooser = new DirectoryChooser();
    private static Stage stage;

    public static void setStage(Stage stage) {
        IOAccess_Excel.stage = stage;
    }

    public static void createExcelFile(List<Teacher> teacherList, List<SchoolClass> schoolClassList) {
        directoryChooser.setInitialDirectory(new File("."));
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

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);

        XSSFRow row = sheet.createRow(rowCount++);
        for (String head: header) {
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(head);
            cell.setCellStyle(style);
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
            StringBuilder subjects = new StringBuilder();
            for (Subject subject:teacher.getSubjects()) {
                subjects.append(subject).append(";");
            }
            cell.setCellValue(subjects.substring(0, subjects.length() - 1));
        }


        sheet = wb.createSheet("Klassen");


        String[] headerClass = {
                "Name", "Klassenvorstand"
        };

        rowCount = 0;
        columCount = 0;

        row = sheet.createRow(rowCount++);
        for (String head: headerClass) {
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(head);
            cell.setCellStyle(style);
        }

        for (SchoolClass schoolClass:schoolClassList) {
            row = sheet.createRow(rowCount++);
            columCount = 0;
            XSSFCell cell = row.createCell(columCount++);
            cell.setCellValue(schoolClass.getClassname());

            cell = row.createCell(columCount);
            cell.setCellValue(schoolClass.getFormattedTeacher());

        }

        int numberOfSheets = wb.getNumberOfSheets();

        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheetS = wb.getSheetAt(i);
            if (sheetS.getPhysicalNumberOfRows() > 0) {
                Row rowS = sheetS.getRow(sheetS.getFirstRowNum());
                Iterator< Cell > cellIterator = rowS.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheetS.autoSizeColumn(columnIndex);
                }
            }
        }


        try (FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "\\vs-markthartmannsdorf-data.xlsx")) {
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
