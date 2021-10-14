module at.vs.vsmarkthartmannsdorf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens at.vs.vsmarkthartmannsdorf to javafx.fxml;
    exports at.vs.vsmarkthartmannsdorf;
}