package rfidhandler.entity.animal.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import rfidhandler.App;

import java.util.Optional;

public class AnimalNewRecordDialog {

    public static boolean show() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Record");
        alert.setHeaderText(null);
        alert.setContentText("Record not in database. Do you want to create a new record?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);
        
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(App.get().getClass().getResourceAsStream("/appicon.png")));

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }
}
