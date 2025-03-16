module com.app.foodguard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;

    opens com.app.foodguard to javafx.fxml;
    opens com.app.foodguard.controller to javafx.fxml;
    exports com.app.foodguard;
    exports com.app.foodguard.controller to javafx.fxml;
}