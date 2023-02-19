module sample.gui {
    requires javafx.controls;
    requires javafx.fxml;

    opens sample.gui to javafx.fxml;
    exports sample.gui;
}