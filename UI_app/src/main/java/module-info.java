module com.example.ui_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ui_app to javafx.fxml;
    exports com.example.ui_app;
}