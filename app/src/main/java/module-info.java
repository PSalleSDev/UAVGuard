module com.uavguard.app {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.uavguard.utilities;
    requires com.uavguard.plugin;
    requires com.google.gson;

    opens com.uavguard.app to javafx.fxml;
    exports com.uavguard.app;
}
