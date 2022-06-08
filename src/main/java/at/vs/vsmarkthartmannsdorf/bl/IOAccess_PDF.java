package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.Main;
import at.vs.vsmarkthartmannsdorf.data.*;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.IntStream;
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
        try {
            System.out.println(Objects.requireNonNull
                    (Main.class.getClassLoader().getResource("Logo_Volksschule_MH.jpg")).getFile().replace("%20", " "));
            FileInputStream fis = new FileInputStream(Objects.requireNonNull
                    (Main.class.getClassLoader().getResource("Volksschule-Logo_Abstand.jpg")).getFile().replace("%20", " "));
            Image image = Image.getInstance(fis.readAllBytes());
            image.setSpacingAfter(200);
            document.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println(schoolClass.getTimetable().getTimeTableContent().size());
        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        addRows(table);

        document.add(table);
        document.close();
    }


    private static void addTableHeader(PdfPTable table) {
        Stream.of("Stunde", Day.Montag.name(), Day.Dienstag.name(), Day.Mittwoch.name(), Day.Donnerstag.name(), Day.Freitag.name())
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.WHITE);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table) {
        Timetable timetable = SchoolDB.getInstance().getTimetablesFromClass(schoolClass).get(0);
        System.out.println(timetable);
        HashMap<Day, HashMap<Integer, Lesson>> subjects = timetable.getSubjects();
        System.out.println(subjects.size());

        IntStream.range(1, 9).forEach(i -> {
            table.addCell(i + "");
            for (Day day : Day.values()) {
                Lesson lesson = subjects.get(day).get(i);
                if (lesson.isEmptyLesson()) {
                    table.addCell("");
                } else {
                    table.addCell(lesson.getSubject().getName());
                }
            }
            PdfPCell[] cells = table.getRow(i).getCells();

            IntStream.range(1, cells.length).forEach(j -> {
                setColorCell(cells[j]);
            });
        });
    }

    private static void setColorCell(PdfPCell cell) {
        System.out.println(cell.getPhrase().getContent());
        if (!cell.getPhrase().getContent().isEmpty()) {
            Color color = Color.valueOf(PropertiesLoader.getInstance().
                    getProperties().getProperty(cell.getPhrase().getContent()));
            String hex = color.toString().substring(1);
            int red = Integer.parseInt(hex.substring(1, 3), 16);
            int blue = Integer.parseInt(hex.substring(3, 5), 16);
            int green = Integer.parseInt(hex.substring(5, 7), 16);

            double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;

            if (luminance < 0.002) {
                Font font = new Font();
                font.setColor(new BaseColor(255, 255, 255));
                Phrase phrase = new Phrase(cell.getPhrase().getContent(), font);
                cell.setPhrase(phrase);
            }

            cell.setBackgroundColor(new BaseColor(red, blue, green));

        }
    }
}
