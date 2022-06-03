module demo.javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens demo.javafx to javafx.fxml;
    exports demo.javafx;
}