module sample.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires com.google.gson;

    opens sample.gui to javafx.fxml;
    exports sample.gui;
}