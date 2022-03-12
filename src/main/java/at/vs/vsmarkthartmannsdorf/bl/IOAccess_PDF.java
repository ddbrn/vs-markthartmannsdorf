package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.*;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class IOAccess_PDF {
    private static Stage stage;
    private static SchoolClass schoolClass;

    public static void setStage(Stage stage) {
        IOAccess_PDF.stage = stage;
    }

    public static void createPDF(SchoolClass klasse) throws FileNotFoundException, DocumentException {
        IOAccess_PDF.schoolClass = klasse;

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(PropertiesLoader.getInstance().getProperties().get(PropertyName.export_folder.name()).toString()));
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            return;
        }

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(selectedDirectory.getAbsolutePath() + "/Stundenplan-" + schoolClass.getClassname() + ".pdf"));

        document.open();

        // System.out.println(schoolClass.getTimetable().getTimeTableContent().size());
        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        // addRows(table);

        document.add(table);
        document.close();
    }


    private static void addTableHeader(PdfPTable table) {
        Stream.of("", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /*
    private static void addRows(PdfPTable table) {
        for (TimetableDay timetableDay : schoolClass.getTimetable().getTimeTableContent()) {
            table.addCell(timetableDay.getTime());
            table.addCell(timetableDay.getMonday());
            table.addCell(timetableDay.getTuesday());
            table.addCell(timetableDay.getWednesday());
            table.addCell(timetableDay.getThursday());
            table.addCell(timetableDay.getFriday());

            PdfPCell[] cells = table.getRow(timetableDay.getId()).getCells();

            for (PdfPCell cell : cells) {
                Color color;
                System.out.println(cell.getPhrase().getContent());

                String hex;
                int red, blue, green;
                switch (cell.getPhrase().getContent()){
                    case "Mathematik":
                         color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Mathematik.name()));
                         hex = color.toString().substring(2);
                         red = Integer.parseInt(hex.substring(1, 3), 16);
                         blue = Integer.parseInt(hex.substring(3, 5), 16);
                         green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    case "Deutsch":
                         color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Deutsch.name()));
                        hex = color.toString().substring(2);
                        red = Integer.parseInt(hex.substring(1, 3), 16);
                        blue = Integer.parseInt(hex.substring(3, 5), 16);
                        green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    case "Sport":
                         color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Sport.name()));
                        hex = color.toString().substring(2);
                        red = Integer.parseInt(hex.substring(1, 3), 16);
                        blue = Integer.parseInt(hex.substring(3, 5), 16);
                        green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    case "Musik":
                        color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Musik.name()));
                        hex = color.toString().substring(2);
                        red = Integer.parseInt(hex.substring(1, 3), 16);
                        blue = Integer.parseInt(hex.substring(3, 5), 16);
                        green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    case "Sachkunde":
                        color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Sachkunde.name()));
                        hex = color.toString().substring(2);
                        red = Integer.parseInt(hex.substring(1, 3), 16);
                        blue = Integer.parseInt(hex.substring(3, 5), 16);
                        green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    case "Englisch":
                        color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Englisch.name()));
                        hex = color.toString().substring(2);
                        red = Integer.parseInt(hex.substring(1, 3), 16);
                        blue = Integer.parseInt(hex.substring(3, 5), 16);
                        green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    case "Sozialkunde":
                        color = Color.valueOf(PropertiesLoader.getInstance().
                                getProperties().getProperty(Subject.Sozialkunde.name()));
                        hex = color.toString().substring(2);
                        red = Integer.parseInt(hex.substring(1, 3), 16);
                        blue = Integer.parseInt(hex.substring(3, 5), 16);
                        green = Integer.parseInt(hex.substring(5, 7), 16);

                        cell.setBackgroundColor(new BaseColor(red, blue, green));
                        break;
                    default:
                }
            }
        }
    } */
}
