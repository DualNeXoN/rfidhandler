package rfidhandler.entity.vaccine.ui;

import java.sql.Timestamp;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VaccineAddDialog extends Stage {
    private final TextField inputField;
    private Timestamp timestamp;
    private boolean isCancelled = false;

    public VaccineAddDialog() {
        getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));

        inputField = new TextField();
        Label descriptionLabel = new Label("Description:");
        Button confirmButton = new Button("Add");
        confirmButton.getStyleClass().add("button-add");
        confirmButton.setOnAction(event -> {
            Date now = new Date();
            timestamp = new Timestamp(now.getTime());
            close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("button-cancel");
        cancelButton.setOnAction(event -> {
            isCancelled = true;
            close();
        });

        HBox buttonBox = new HBox(confirmButton, cancelButton);
        buttonBox.setSpacing(10);

        VBox vBox = new VBox(descriptionLabel, inputField, buttonBox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        vBox.setStyle("-fx-border-color: #C7C7C7; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("vaccine-add-dialog.css");
        setScene(scene);
    }

    public String getDescription() {
        return inputField.getText();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}
