package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.SchoolClass;
import at.vs.vsmarkthartmannsdorf.db.SchoolDB;
import javafx.print.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class IOAccess_Print {
    private static Stage stage;

    public static void printTest() {
        PrinterJob pj = PrinterJob.createPrinterJob();
        System.out.println(Printer.getAllPrinters());

        boolean success = pj.showPrintDialog(stage.getOwner());

        Node node = SchoolDB.getInstance().getPrintTimetables();

        System.out.println(pj.getJobSettings().getPageLayout());

        PageLayout layout = pj.getPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE,
                0,175,0,40);

        if(success){
            pj.printPage(layout, node);
            pj.endJob();
        }
    }

    public static void setStage(Stage stage) {
        System.out.println(stage);
        IOAccess_Print.stage = stage;
    }
}
