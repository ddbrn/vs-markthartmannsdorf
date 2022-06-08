package at.vs.vsmarkthartmannsdorf;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelpController implements Initializable {
    public WebView wvHelp;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Test");
        WebEngine webEngine = wvHelp.getEngine();
        String content =
                "Hello World!";

        InputStream is = getClass().getClassLoader().getResourceAsStream("help.html");
        if (is != null) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            webEngine.loadContent(result);
        }

        //webEngine.load("https://stackoverflow.com/questions/47318218/javafx-fxml-hbox-align-left-and-right");
        //webEngine.loadContent(content, "text/html");
    }
}
