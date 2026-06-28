module tech.octopusdragon.checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens tech.octopusdragon.checkers to javafx.fxml;
    opens tech.octopusdragon.checkers.control to javafx.fxml;
    opens tech.octopusdragon.checkers.window to javafx.fxml;
    opens tech.octopusdragon.checkers.controller to javafx.fxml;
    opens tech.octopusdragon.checkers.model to com.google.gson;
    exports tech.octopusdragon.checkers;
}