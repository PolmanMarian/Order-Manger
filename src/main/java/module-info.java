module com.example.assigment3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;

    opens com.example.assigment3 to javafx.fxml;
    exports com.example.assigment3;

    opens com.example.assigment3.Presentation to javafx.fxml;
    exports com.example.assigment3.Presentation;

    opens com.example.assigment3.Model to javafx.fxml;
    exports com.example.assigment3.Model;
}
