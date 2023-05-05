package rfidhandler.entity.owner.ui;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rfidhandler.entity.owner.Owner;
import rfidhandler.entity.owner.OwnerHandler;

public class OwnerEditDialog extends Stage {
	
    private boolean isCancelled = false;
    private boolean isNewCreated = false;
    private int newId = 1;
    private ChoiceBox<Owner> choiceBox;

    public OwnerEditDialog() {
        getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));

        choiceBox = new ChoiceBox<>();
        ArrayList<Owner> list = OwnerHandler.getAll();
        choiceBox.getItems().addAll(list);
        choiceBox.setValue(list.get(0));

        Label ownerLabel = new Label("Owner:");
        Button addNewOwnerButton = new Button("New owner");
        addNewOwnerButton.getStyleClass().add("button-new");
        addNewOwnerButton.setOnAction(event -> {
        	OwnerCreateDialog dialog = new OwnerCreateDialog();
			dialog.showAndWait();
			if(dialog.isCancelled()) return;
			newId = OwnerHandler.createOwner(dialog.getName(), dialog.getAddress());
			isNewCreated = true;
			close();
        });
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

        HBox buttonBox = new HBox(confirmButton, addNewOwnerButton, cancelButton);
        buttonBox.setSpacing(10);

        VBox vBox = new VBox(ownerLabel, choiceBox, buttonBox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        vBox.setStyle("-fx-border-color: #C7C7C7; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("owner-edit-dialog.css");
        setScene(scene);
    }

    public int getNewOwnerId() {
    	if(isNewCreated) return newId;
        return choiceBox.getValue().getId();
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}