module sample.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    opens sample.gui to javafx.fxml;
    exports sample.gui;
}