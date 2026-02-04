module com.example.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires javafx.graphics;


    opens com.example.ims001 to javafx.fxml;
    exports com.example.ims001;
}
