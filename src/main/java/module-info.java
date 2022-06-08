module vs.markthartmannsdorf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.poi;
    requires com.fasterxml.jackson.databind;
    requires lombok;
    requires org.apache.poi.ooxml;
    requires itextpdf;
    requires org.apache.commons.lang3;
    requires com.jfoenix;
    requires javafx.web;

    opens at.vs.vsmarkthartmannsdorf to javafx.fxml;
    exports at.vs.vsmarkthartmannsdorf;
    exports at.vs.vsmarkthartmannsdorf.data;
    exports at.vs.vsmarkthartmannsdorf.serializer;
}