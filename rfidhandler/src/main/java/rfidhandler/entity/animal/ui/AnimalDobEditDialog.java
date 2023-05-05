package rfidhandler.entity.animal.ui;

import java.sql.Date;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AnimalDobEditDialog extends Stage {
	
	private Date dob = null;
    private boolean isCancelled = false;

    public AnimalDobEditDialog() {
        getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        
        DatePicker datePicker = new DatePicker();
	    datePicker.setOnAction(e -> {
	        LocalDate localDate = datePicker.getValue();
	        dob = Date.valueOf(localDate);
	    });

        Label dobLabel = new Label("DoB:");
        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("button-add");
        confirmButton.setOnAction(event -> {
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

        VBox vBox = new VBox(dobLabel, datePicker, buttonBox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        vBox.setStyle("-fx-border-color: #C7C7C7; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("animal-dob-edit-dialog.css");
        setScene(scene);
    }

    public Date getDob() {
        return dob;
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}