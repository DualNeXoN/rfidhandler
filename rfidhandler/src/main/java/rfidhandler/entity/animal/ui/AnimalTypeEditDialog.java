package rfidhandler.entity.animal.ui;

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
import javafx.util.StringConverter;
import rfidhandler.entity.animal.AnimalHandler;
import rfidhandler.entity.type.AnimalType;

public class AnimalTypeEditDialog extends Stage {
	
    private boolean isCancelled = false;
    private ChoiceBox<AnimalType> choiceBox;

    public AnimalTypeEditDialog() {
        getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));

        choiceBox = new ChoiceBox<>();
        ArrayList<AnimalType> list = AnimalHandler.getTypes();
        choiceBox.getItems().addAll(list);
        StringConverter<AnimalType> converter = new StringConverter<AnimalType>() {
            @Override
            public String toString(AnimalType animalType) {
                if (animalType == null) {
                    return null;
                } else {
                    return animalType.getName();
                }
            }

            @Override
            public AnimalType fromString(String name) {
                for (AnimalType animalType : list) {
                    if (animalType.getName().equals(name)) {
                        return animalType;
                    }
                }
                return new AnimalType(-1, name);
            }
        };
        choiceBox.setConverter(converter);
        choiceBox.setValue(list.get(0));

        Label typeLabel = new Label("Type:");
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

        VBox vBox = new VBox(typeLabel, choiceBox, buttonBox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        vBox.setStyle("-fx-border-color: #C7C7C7; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("animal-type-edit-dialog.css");
        setScene(scene);
    }

    public int getNewTypeId() {
        return choiceBox.getValue().getId();
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}