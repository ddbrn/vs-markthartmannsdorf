package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.Main;
import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.Subject;
import at.vs.vsmarkthartmannsdorf.data.Teacher;
import javafx.scene.paint.Color;
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

            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));


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
                for (Subject subject : teacher.getSubjects()) {
                    subjects.append(subject).append(";");
                }
                cell.setCellValue(subjects.substring(0, subjects.length() - 1));
            }


            sheet = wb.createSheet("Klassen");

            lastCol = 5;

            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));


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
                cellH.setCellValue("Montag");
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(2);
                cellH.setCellValue("Dienstag");
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(3);
                cellH.setCellValue("Mittwoch");
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(4);
                cellH.setCellValue("Donnerstag");
                cellH.setCellStyle(styleH);

                cellH = rowClassesH.createCell(5);
                cellH.setCellValue("Freitag");
                cellH.setCellStyle(styleH);

                schoolClass.getTimetable().getTimeTable().forEach(timetableDay -> {

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
                });
                int firstRowC = 0;
                int lastRowC = 5;
                int firstColC = 0;
                int lastColC = 4;

                sheetClasses.addMergedRegion(new CellRangeAddress(firstRowC, lastRowC, firstColC, lastColC));
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
                        new FileInputStream(Objects.requireNonNull(Main.class.getClassLoader().getResource("Logo_Volksschule_MH.jpg")).getFile().replace("%20", " "));
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
        try {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
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
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    public static List<SchoolClass> readFromExcelFileClass(List<Teacher> teacherList, List<SchoolClass> schoolClassList) {
        try {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

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
                        new SchoolClass(
                                cells[0].getStringCellValue(),
                                teacherList
                                        .stream()
                                        .filter(teacher -> (teacher.getFirstname() + " " + teacher.getSurname() + " (" + teacher.getAbbreviation() + ")").equals(cells[1].getStringCellValue()))
                                        .findFirst().get());


                if (!schoolClassList.contains(schoolClass)) {
                    schoolClassList.add(schoolClass);
                }

                System.out.println(schoolClass);
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schoolClassList;
    }


    private static void skipImage(Iterator<Row> rowIterator) {
        rowIterator.next();
    }
}

