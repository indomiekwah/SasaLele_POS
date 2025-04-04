module org.example.sasalele_pos {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sasalele_pos to javafx.fxml;
    exports org.example.sasalele_pos;
}