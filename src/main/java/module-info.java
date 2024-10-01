module com.example.progettochat {
    requires javafx.controls;
    requires javafx.fxml;


    opens progettochatsocket.client to javafx.fxml;
    exports progettochatsocket.client;
}