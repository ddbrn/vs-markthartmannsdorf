package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.Main;
import at.vs.vsmarkthartmannsdorf.data.*;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IOAccess_Excel {
    private static Stage stage;

    public static void setStage(Stage stage) {
        IOAccess_Excel.stage = stage;
    }

    public static void createExcelFile(List<Teacher> teacherList, List<SchoolClass> schoolClassList) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(PropertiesLoader.getInstance().getProperties().get(PropertyName.export_folder.name()).toString()));
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

            //sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));


            String[] header = {
                    "Vorname", "Nachname", "Kürzel", "Fächer"
            };

            int rowCount = 7;
            final int[] columCount = {0};

            CellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);

            XSSFRow row = sheet.createRow(rowCount++);
            for (String head : header) {
                XSSFCell cell = row.createCell(columCount[0]++);
                cell.setCellValue(head);
                cell.setCellStyle(style);
            }

            for (Teacher teacher : teacherList) {
                row = sheet.createRow(rowCount++);
                columCount[0] = 0;
                XSSFCell cell = row.createCell(columCount[0]++);
                cell.setCellValue(teacher.getFirstname());

                cell = row.createCell(columCount[0]++);
                cell.setCellValue(teacher.getSurname());

                cell = row.createCell(columCount[0]++);
                cell.setCellValue(teacher.getAbbreviation());

                cell = row.createCell(columCount[0]);
                StringBuilder subjects = new StringBuilder();
                for (Subjectobject subject : teacher.getSubjects()) {
                    subjects.append(subject.getName()).append(";");
                }
                cell.setCellValue(subjects.substring(0, subjects.length() - 1));
            }


            sheet = wb.createSheet("Klassen");

            lastCol = 5;

            //sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));


            String[] headerClass = {
                    "Name", "Klassenvorstand"
            };

            rowCount = 7;
            columCount[0] = 0;

            row = sheet.createRow(rowCount++);
            for (String head : headerClass) {
                XSSFCell cell = row.createCell(columCount[0]++);
                cell.setCellValue(head);
                cell.setCellStyle(style);
            }

            for (SchoolClass schoolClass : schoolClassList) {
                row = sheet.createRow(rowCount++);
                columCount[0] = 0;
                XSSFCell cell = row.createCell(columCount[0]++);
                cell.setCellValue(schoolClass.getClassname());

                cell = row.createCell(columCount[0]);
                cell.setCellValue(schoolClass.getFormattedTeacher());

            }

            schoolClassList.forEach(schoolClass -> {
                XSSFSheet sheetClasses = wb.createSheet(schoolClass.getClassname());
                final int[] rowCountClasses = {7};
                XSSFRow rowClassesH = sheetClasses.createRow(rowCountClasses[0]++);
                XSSFCell cellH = rowClassesH.createCell(0);

                CellStyle styleH = wb.createCellStyle();
                Font fontH = wb.createFont();
                fontH.setBold(true);
                styleH.setFont(font);
                styleH.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                styleH.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);

                cellH.setCellValue("");

                cellH = rowClassesH.createCell(1);
                cellH.setCellValue(Day.Montag.name());
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(2);
                cellH.setCellValue(Day.Dienstag.name());
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(3);
                cellH.setCellValue(Day.Mittwoch.name());
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(4);
                cellH.setCellValue(Day.Donnerstag.name());
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(5);
                cellH.setCellValue(Day.Freitag.name());
                cellH.setCellStyle(styleH);

                Timetable timetable = SchoolDB.getInstance().getTimetablesFromClass(schoolClass).get(0); // Needs to be fixed sgr


                IntStream.range(1, 9).forEach(i -> {
                    XSSFRow rowClasses = sheetClasses.createRow(rowCountClasses[0]++);
                    AtomicReference<XSSFCell> cell = new AtomicReference<>(rowClasses.createCell(0));

                    cell.get().setCellValue(i);

                    AtomicReference<Color> color = new AtomicReference<>();

                    AtomicInteger index = new AtomicInteger(1);

                    Arrays.stream(Day.values()).forEach(day -> {
                        cell.set(rowClasses.createCell(index.getAndIncrement()));
                        Lesson lesson = timetable.getSubjects().get(day).get(i);
                        if (!lesson.isEmptyLesson()) {
                            cell.get().setCellValue(lesson.getSubject().getName());

                            color.set(Color.valueOf(
                                    PropertiesLoader
                                            .getInstance()
                                            .getProperties()
                                            .getProperty(lesson.getSubject().getName())));

                            byte[] rgb =
                                    new byte[]{(byte) (color.get().getRed() * 127), (byte) (color.get().getGreen() * 127), (byte) (color.get().getBlue() * 127)};

                            XSSFCellStyle styleC = wb.createCellStyle();

                            //create XSSFColor
                            XSSFColor xssfColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
                            //set fill color to cell style
                            XSSFFont fontC = wb.createFont();
                            fontC.setColor(xssfColor);
                            styleC.setFont(fontC);
                            //styleC.setFillForegroundColor(xssfColor);


                            cell.get().setCellStyle(styleC);
                        }
                    });

                });

                /*schoolClass.getTimetable().getTimetableDays().forEach(timetableDay -> {

                    XSSFRow rowClasses = sheetClasses.createRow(rowCountClasses[0]++);
                    XSSFCell cell = rowClasses.createCell(0);

                    cell.setCellValue(timetableDay.getId());

                    Color color;
                    byte[] rgb;
                    XSSFCellStyle styleC;
                    XSSFColor xssfColor;

                    cell = rowClasses.createCell(1);
                    if (!timetableDay.getMonday().isBlank()) {
                        cell.setCellValue(timetableDay.getMonday());

                        color = Color.valueOf(
                                PropertiesLoader
                                        .getInstance()
                                        .getProperties()
                                        .getProperty(Subject.valueOf(timetableDay.getMonday()).name()));

                        rgb =
                                new byte[]{(byte) (color.getRed() * 127), (byte) (color.getGreen() * 127), (byte) (color.getBlue() * 127)};

                        styleC = wb.createCellStyle();

                        //create XSSFColor
                        xssfColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
                        //set fill color to cell style
                        XSSFFont fontC = wb.createFont();
                        fontC.setColor(xssfColor);
                        styleC.setFont(fontC);
                        //styleC.setFillForegroundColor(xssfColor);


                        cell.setCellStyle(styleC);
                    }


                    cell = rowClasses.createCell(2);
                    if (!timetableDay.getTuesday().isBlank()) {
                        cell.setCellValue(timetableDay.getTuesday());

                        color = Color.valueOf(
                                PropertiesLoader
                                        .getInstance()
                                        .getProperties()
                                        .getProperty(Subject.valueOf(timetableDay.getTuesday()).name()));

                        rgb =
                                new byte[]{(byte) (color.getRed() * 127), (byte) (color.getGreen() * 127), (byte) (color.getBlue() * 127)};

                        styleC = wb.createCellStyle();


                        //create XSSFColor
                        xssfColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
                        //set fill color to cell style
                        XSSFFont fontC = wb.createFont();
                        fontC.setColor(xssfColor);
                        styleC.setFont(fontC);

                        cell.setCellStyle(styleC);
                    }


                    cell = rowClasses.createCell(3);
                    if (!timetableDay.getWednesday().isBlank()) {
                        cell.setCellValue(timetableDay.getWednesday());

                        color = Color.valueOf(
                                PropertiesLoader
                                        .getInstance()
                                        .getProperties()
                                        .getProperty(Subject.valueOf(timetableDay.getWednesday()).name()));

                        rgb =
                                new byte[]{(byte) (color.getRed() * 127), (byte) (color.getGreen() * 127), (byte) (color.getBlue() * 127)};

                        styleC = wb.createCellStyle();


                        //create XSSFColor
                        xssfColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
                        //set fill color to cell style
                        XSSFFont fontC = wb.createFont();
                        fontC.setColor(xssfColor);
                        styleC.setFont(fontC);

                        cell.setCellStyle(styleC);
                    }


                    cell = rowClasses.createCell(4);
                    if (!timetableDay.getThursday().isBlank()) {
                        cell.setCellValue(timetableDay.getThursday());

                        color = Color.valueOf(
                                PropertiesLoader
                                        .getInstance()
                                        .getProperties()
                                        .getProperty(Subject.valueOf(timetableDay.getThursday()).name()));

                        rgb =
                                new byte[]{(byte) (color.getRed() * 127), (byte) (color.getGreen() * 127), (byte) (color.getBlue() * 127)};

                        styleC = wb.createCellStyle();


                        //create XSSFColor
                        xssfColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
                        //set fill color to cell style
                        XSSFFont fontC = wb.createFont();
                        fontC.setColor(xssfColor);
                        styleC.setFont(fontC);

                        cell.setCellStyle(styleC);
                    }


                    cell = rowClasses.createCell(5);
                    if (!timetableDay.getFriday().isBlank()) {
                        cell.setCellValue(timetableDay.getFriday());

                        color = Color.valueOf(
                                PropertiesLoader
                                        .getInstance()
                                        .getProperties()
                                        .getProperty(Subject.valueOf(timetableDay.getFriday()).name()));

                        rgb =
                                new byte[]{(byte) (color.getRed() * 127), (byte) (color.getGreen() * 127), (byte) (color.getBlue() * 127)};

                        styleC = wb.createCellStyle();


                        //create XSSFColor
                        xssfColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
                        //set fill color to cell style
                        XSSFFont fontC = wb.createFont();
                        fontC.setColor(xssfColor);
                        styleC.setFont(fontC);

                        cell.setCellStyle(styleC);
                    }
                }); */
                int firstRowC = 0;
                int lastRowC = 5;
                int firstColC = 0;
                int lastColC = 4;

                //sheetClasses.addMergedRegion(new CellRangeAddress(firstRowC, lastRowC, firstColC, lastColC));
            });

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
                        new FileInputStream(Paths.get("", "data", "Logo_Volksschule_MH.jpg").toAbsolutePath().toString().replace("%20", " "));
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("vs-martkhartmannsdorf | EXPORTIERT");
            alert.setHeaderText("Es wurde erfolgreich unter \"" + selectedDirectory.getAbsolutePath() + "\\vs-markthartmannsdorf-data.xlsx" + "\" gespeichert!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static File file;

    public static boolean loadFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(PropertiesLoader.getInstance().getProperties().get(PropertyName.export_folder.name()).toString()));
            file = fileChooser.showOpenDialog(stage);
            return file != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static List<Teacher> readFromExcelFileTeacher() {

        List<Teacher> teacherList = new ArrayList<>();
        XSSFWorkbook workbook = null;
        try {
            //Create Workbook instance holding reference to .xlsx file
            workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each row's one by one
            Iterator<Row> rowIterator = sheet.iterator();
            skipImage(rowIterator);

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

                Arrays.stream(cells).forEach(System.out::println);


                Teacher teacher =
                        new Teacher(SchoolDB.getInstance().getLastTeacherID(), cells[0].getStringCellValue(),
                                cells[1].getStringCellValue(),
                                cells[2].getStringCellValue().toUpperCase(),
                                Arrays.stream(cells[3]
                                                .getStringCellValue()
                                                .split(";"))
                                        .map(s -> SchoolDB.getInstance().getSubjectobjectFromName(s)).collect(Collectors.toList()));
                teacherList.add(teacher);
                System.out.println(teacher);

            }
            workbook.close();

            return teacherList;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            e.printStackTrace();
            System.out.println("FEHLERHAFTE LEHRER EINGABE");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("vs-martkhartmannsdorf | FEHLER");
            alert.setHeaderText("Es ist ein Fehler beim Importieren passiert!");
            alert.setContentText("Es wurde eine fehlerhafte Eingabe im Excel Sheet getätigt, überprüfen Sie den Reiter \"Lehrer\".");

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();

            try {
                assert workbook != null;
                workbook.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static List<SchoolClass> readFromExcelFileClass(List<Teacher> teacherList, List<SchoolClass> schoolClassList) {

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
            //Create Workbook instance holding reference to .xlsx file

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(1);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            skipImage(rowIterator);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();


                Cell[] cells = new Cell[2];
                int index = 0;
                while (cellIterator.hasNext()) {
                    cells[index++] = cellIterator.next();
                    //Check the cell type and format accordingly
                }
                SchoolClass schoolClass =
                        new SchoolClass(SchoolDB.getInstance().getLastTeacherID(),
                                cells[0].getStringCellValue(),
                                teacherList
                                        .stream()
                                        .filter(teacher -> (teacher.getFirstname() + " " + teacher.getSurname() + " (" + teacher.getAbbreviation() + ")").equals(cells[1].getStringCellValue()))
                                        .findFirst().get().getId());


                if (schoolClassList.stream().noneMatch(schoolClass1 -> schoolClass1.getClassname().equals(schoolClass.getClassname()))) {
                    schoolClassList.add(schoolClass);
                }

                System.out.println(schoolClass);
            }

            workbook.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            e.printStackTrace();
            System.out.println("FEHLERHAFTE KLASSEN EINGABE");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("vs-martkhartmannsdorf | FEHLER");
            alert.setHeaderText("Es ist ein Fehler beim Importieren passiert!");
            alert.setContentText("Es wurde eine fehlerhafte Eingabe im Excel Sheet getätigt, überprüfen Sie den Reiter \"Klassen\".");

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();

            try {
                assert workbook != null;
                workbook.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return schoolClassList;
    }


    private static void skipImage(Iterator<Row> rowIterator) {
        rowIterator.next();
    }
}

