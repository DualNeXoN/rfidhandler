package rfidhandler.entity.owner.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OwnerCreateDialog extends Stage {
	
	private boolean isCancelled = false;
	private TextField nameField;
	private TextField addressField;

	public OwnerCreateDialog() {
	    getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));

	    nameField = new TextField();
	    nameField.setPromptText("Name");
	    
	    addressField = new TextField();
	    addressField.setPromptText("Address");

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

	    VBox vBox = new VBox(nameField, addressField, buttonBox);
	    vBox.setSpacing(10);
	    vBox.setPadding(new Insets(10));
	    vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
	    vBox.setStyle("-fx-border-color: #C7C7C7; -fx-border-width: 1px; -fx-border-radius: 5px;");

	    Scene scene = new Scene(vBox);
	    scene.getStylesheets().add("owner-create-dialog.css");
	    setScene(scene);
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public String getAddress() {
		return addressField.getText();
	}

	public boolean isCancelled() {
	    return isCancelled;
	}

}