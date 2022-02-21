package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.Main;
import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class IOAccess_Excel {

    private static final DirectoryChooser directoryChooser = new DirectoryChooser();
    private static final FileChooser fileChooser = new FileChooser();

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


        try (FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "\\vs-markthartmannsdorf-data.xlsx")) {

            XSSFWorkbook wb = new XSSFWorkbook();

            XSSFSheet sheet = wb.createSheet("Lehrer");


            int firstRow = 0;
            int lastRow = 5;
            int firstCol = 0;
            int lastCol = 3;

            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));


            String[] header = {
                    "Vorname", "Nachname", "Kürzel", "Fächer"
            };

            int rowCount = 7;
            int columCount = 0;

            CellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);

            XSSFRow row = sheet.createRow(rowCount++);
            for (String head : header) {
                XSSFCell cell = row.createCell(columCount++);
                cell.setCellValue(head);
                cell.setCellStyle(style);
            }

            for (Teacher teacher : teacherList) {
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
                for (Subject subject : teacher.getSubjects()) {
                    subjects.append(subject).append(";");
                }
                cell.setCellValue(subjects.substring(0, subjects.length() - 1));
            }


            sheet = wb.createSheet("Klassen");


            String[] headerClass = {
                    "Name", "Klassenvorstand"
            };

            rowCount = 7;
            columCount = 0;

            row = sheet.createRow(rowCount++);
            for (String head : headerClass) {
                XSSFCell cell = row.createCell(columCount++);
                cell.setCellValue(head);
                cell.setCellStyle(style);
            }

            for (SchoolClass schoolClass : schoolClassList) {
                row = sheet.createRow(rowCount++);
                columCount = 0;
                XSSFCell cell = row.createCell(columCount++);
                cell.setCellValue(schoolClass.getClassname());

                cell = row.createCell(columCount);
                cell.setCellValue(schoolClass.getFormattedTeacher());

            }

            int numberOfSheets = wb.getNumberOfSheets();

            for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet sheetS = wb.getSheetAt(i);

                if (sheetS.getPhysicalNumberOfRows() > 0) {
                    Row rowS = sheetS.getRow(sheetS.getFirstRowNum());
                    Iterator<Cell> cellIterator = rowS.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        int columnIndex = cell.getColumnIndex();
                        sheetS.autoSizeColumn(columnIndex);
                    }
                }

                final FileInputStream stream =
                        new FileInputStream(Main.class.getClassLoader().getResource("Logo_Volksschule_MH.jpg").getFile().replace("%20", " "));
                final CreationHelper helper = wb.getCreationHelper();
                Drawing<XSSFShape> drawing = sheetS.createDrawingPatriarch();

                final ClientAnchor anchor = helper.createClientAnchor();
                anchor.setAnchorType(anchor.getAnchorType());


                final int pictureIndex =
                        wb.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_PNG);


                anchor.setCol1(0);
                anchor.setRow1(0);

                Picture pict = drawing.createPicture(anchor, pictureIndex);
                pict.resize();
            }



            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Teacher> readFromExcelFileTeacher() {
        List<Teacher> teacherList = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(fileChooser.showOpenDialog(stage));


            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();


                Cell[] cells = new Cell[4];
                int index = 0;
                while (cellIterator.hasNext()) {
                    cells[index++] = cellIterator.next();
                    //Check the cell type and format accordingly
                }
                Teacher teacher =
                        new Teacher(cells[0].getStringCellValue(),
                                cells[1].getStringCellValue(),
                                cells[2].getStringCellValue().toUpperCase(),
                                Arrays.stream(cells[3]
                                                .getStringCellValue()
                                                .split(";"))
                                        .map(Subject::valueOf).collect(Collectors.toList()));
                teacherList.add(teacher);
                System.out.println(teacher);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    public static List<SchoolClass> readFromExcelFileClass() {
        List<SchoolClass> classList = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(fileChooser.showOpenDialog(stage));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(1);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();


                Cell[] cells = new Cell[4];
                int index = 0;
                while (cellIterator.hasNext()) {
                    cells[index++] = cellIterator.next();
                    //Check the cell type and format accordingly
                }
                SchoolClass schoolClass =
                        new SchoolClass();

                classList.add(schoolClass);
                System.out.println(schoolClass);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classList;
    }
}
