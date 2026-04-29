module com.nelly.education_based {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.nelly.education_based to javafx.fxml;
    exports com.nelly.education_based;
}