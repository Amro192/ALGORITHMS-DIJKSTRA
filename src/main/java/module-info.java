module com.othman.project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires atlantafx.base;
    requires jdk.compiler;


    opens com.othman.project3 to javafx.fxml;
    exports com.othman.project3;
    exports com.othman.project3.model;
    exports com.othman.project3.UI;
    opens com.othman.project3.UI to javafx.fxml;
}