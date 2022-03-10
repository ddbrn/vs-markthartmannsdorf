package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.data.TimetableDay;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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

        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        addRows(table);
        addCustomRows(table);

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

    private static void addRows(PdfPTable table) {
        for (TimetableDay timetableDay : schoolClass.getTimetable().getTimeTableContent()) {
            table.addCell(timetableDay.getTime());
            table.addCell(timetableDay.getMonday());
            table.addCell(timetableDay.getTuesday());
            table.addCell(timetableDay.getWednesday());
            table.addCell(timetableDay.getThursday());
            table.addCell(timetableDay.getFriday());
        }
    }
    private static void addCustomRows(PdfPTable table) {

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
}
