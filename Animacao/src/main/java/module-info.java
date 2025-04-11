module com.example.animacao {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.animacao to javafx.fxml;
    exports com.example.animacao;
}