package rfidhandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rfidhandler.entity.animal.Animal;
import rfidhandler.entity.animal.AnimalHandler;
import rfidhandler.entity.animal.ui.AnimalCreateDialog;
import rfidhandler.entity.animal.ui.AnimalNameEditDialog;
import rfidhandler.entity.animal.ui.AnimalNewRecordDialog;
import rfidhandler.entity.animal.ui.AnimalTypeEditDialog;
import rfidhandler.entity.rfid.RfidUid;
import rfidhandler.entity.vaccine.VaccineHandler;
import rfidhandler.entity.vaccine.ui.VaccineAddDialog;
import rfidhandler.entity.vaccine.ui.VaccineTable;
import rfidhandler.thread.SerialAutoconnectThread;
import rfidhandler.thread.SerialReaderThread;

public class App {
	
	public static final String APP_NAME = "RfidHandler";
	public static final String FORMATTER_CONNECTION_STATUS = "Arduino is %s";
	public static final Image NO_IMAGE = new Image(App.class.getResourceAsStream("/noimage.png"));
	
	private static App instance;
	
	public static App get() {
		return instance;
	}	
	
	private Stage stage;
	private Scene scene;
	private Group root;
	private SerialReaderThread readerThread = null;
	private SerialAutoconnectThread autoconnectThread = null;
	
	private Label labelConnection;
	private Label labelId;
	private Label labelRfid;
	private Label labelName;
	private Label labelType;
	private ImageView blob;
	private Button btnAddVaccine;
	private VaccineTable vaccineTable;
	
	private Animal animal;
	
	public App(Stage stage) {
		if(instance != null) return;
		instance = this;
		
		this.stage = stage;
		this.root = new Group();
		this.scene = new Scene(this.root);
		this.stage.setScene(scene);
		
		labelConnection = new Label(String.format(FORMATTER_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED.name().toLowerCase()));
		labelRfid = new Label("?");
		labelId = new Label("?");
		labelName = new Label("?");
		labelType = new Label("?");
		blob = new ImageView(NO_IMAGE);
		btnAddVaccine = new Button("Add vaccine");
		vaccineTable = new VaccineTable();
		
		ContextMenu contextMenuName = new ContextMenu();
		MenuItem menuItemNameChange = new MenuItem("Edit");
		menuItemNameChange.setOnAction(e -> {
			if(animal == null) {
				contextMenuName.hide();
				return;
			}
			AnimalNameEditDialog dialog = new AnimalNameEditDialog(animal.getName());
			dialog.showAndWait();
			if(dialog.isCancelled()) return;
			if(AnimalHandler.changeName(animal, dialog.getNewName())) {
				loadAnimal(animal.getRfid().getUidString());
			}
		});
		contextMenuName.getItems().add(menuItemNameChange);
		labelName.setOnContextMenuRequested(e -> {
			if(animal != null) contextMenuName.show(labelName, e.getScreenX(), e.getScreenY());
		});
		
		ContextMenu contextMenuType = new ContextMenu();
		MenuItem menuItemTypeChange = new MenuItem("Edit");
		menuItemTypeChange.setOnAction(e -> {
			if(animal == null) {
				contextMenuType.hide();
				return;
			}
			AnimalTypeEditDialog dialog = new AnimalTypeEditDialog();
			dialog.showAndWait();
			if(dialog.isCancelled()) return;
			if(AnimalHandler.changeType(animal, dialog.getNewTypeId())) {
				loadAnimal(animal.getRfid().getUidString());
			}
		});
		contextMenuType.getItems().add(menuItemTypeChange);
		labelType.setOnContextMenuRequested(e -> {
			if(animal != null) contextMenuType.show(labelType, e.getScreenX(), e.getScreenY());
		});
		
		ContextMenu contextMenuBlob = new ContextMenu();
		MenuItem menuItemBlobChange = new MenuItem("Change");
		menuItemBlobChange.setOnAction(e -> {
			if(animal == null) {
				contextMenuBlob.hide();
				return;
			}
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(null);
			if(file == null) return;
			try {
				byte[] imageData = Files.readAllBytes(file.toPath());
				if(AnimalHandler.changeImage(animal, new SerialBlob(imageData))) {
					loadAnimal(animal.getRfid().getUidString());
				}
			} catch (IOException | SQLException ex) {}
		});
		contextMenuBlob.getItems().add(menuItemBlobChange);
		blob.setOnContextMenuRequested(e -> {
			if(animal != null) contextMenuBlob.show(blob, e.getScreenX(), e.getScreenY());
		});
		
		btnAddVaccine.setOnAction(e -> {
			VaccineAddDialog dialog = new VaccineAddDialog();
			dialog.showAndWait();
			if(dialog.isCancelled()) return;
			if(VaccineHandler.addVaccine(animal.getId(), dialog.getTimestamp(), dialog.getDescription())) {
				loadAnimal(animal.getRfid().getUidString());
			}
		});
		btnAddVaccine.setDisable(true);
		
		VBox vBox = new VBox();
		vBox.setSpacing(10);

		HBox connectionBox = new HBox(labelConnection);
		connectionBox.setAlignment(Pos.CENTER_RIGHT);
		connectionBox.setPrefHeight(50);

		HBox idBox = new HBox(new Label("ID: "), labelId);
		idBox.setSpacing(10);
		idBox.setAlignment(Pos.CENTER_LEFT);

		HBox rfidBox = new HBox(new Label("RFID: "), labelRfid);
		rfidBox.setSpacing(10);
		rfidBox.setAlignment(Pos.CENTER_LEFT);

		HBox nameBox = new HBox(new Label("Name: "), labelName);
		nameBox.setSpacing(10);
		nameBox.setAlignment(Pos.CENTER_LEFT);

		HBox typeBox = new HBox(new Label("Type: "), labelType);
		typeBox.setSpacing(10);
		typeBox.setAlignment(Pos.CENTER_LEFT);

		vBox.getChildren().addAll(connectionBox, idBox, rfidBox, nameBox, typeBox, blob, btnAddVaccine, vaccineTable);
		root.getChildren().add(vBox);

		labelConnection.getStyleClass().add("connection-label");
		labelId.getStyleClass().add("info-label");
		labelRfid.getStyleClass().add("info-label");
		labelName.getStyleClass().add("info-label");
		labelType.getStyleClass().add("info-label");
		blob.getStyleClass().add("imageView");
		scene.getStylesheets().add("main.css");
		btnAddVaccine.getStyleClass().add("button-add-vaccine");
		
		blob.setFitWidth(150);
		blob.setFitHeight(150);
		
		serialAutoconnect();
		
		stage.setOnCloseRequest(e -> {
        	destroy();
        });
		stage.setScene(scene);
		stage.setWidth(400);
		stage.setHeight(600);
		stage.setResizable(false);
		stage.setTitle(APP_NAME);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        stage.show();
	}
	
	public void loadAnimal(String uid) {
		animal = AnimalHandler.loadAnimal(new RfidUid(uid));
		btnAddVaccine.setDisable(animal == null);
		labelRfid.setText(uid);
		vaccineTable.getItems().clear();
		if(animal != null) {
			labelId.setText(Integer.toString(animal.getId()));
			labelName.setText(animal.getName());
			labelType.setText(animal.getType());
			blob.setImage(animal.getImage());
			vaccineTable.applyItems(animal.getVaccines());
		} else {
			labelId.setText("?");
			labelName.setText("?");
			labelType.setText("?");
			blob.setImage(NO_IMAGE);
			if(AnimalNewRecordDialog.show()) {
				AnimalCreateDialog dialog = new AnimalCreateDialog();
				dialog.showAndWait();
				if(dialog.isCancelled()) return;
				if(AnimalHandler.createAnimal(uid, dialog.getNewName(), dialog.getNewTypeId())) {
					loadAnimal(uid);
				}
			}
		}
	}
	
	public void connect(String port) {
		if(readerThread != null) {
			readerThread.interrupt();
			try {
				readerThread.join();
			} catch (InterruptedException e) {}
		}
		readerThread = new SerialReaderThread(port);
		readerThread.start();
	}
	
	private void serialAutoconnect() {
		autoconnectThread = new SerialAutoconnectThread();
		autoconnectThread.start();
	}
	
	public boolean isSerialConnected() {
		boolean connected = readerThread != null && readerThread.isPortConnected();
		if(!connected && readerThread != null) readerThread.kill();
		return connected;
	}
	
	public void setConnectionStatus(ConnectionStatus status) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				labelConnection.setText(String.format(FORMATTER_CONNECTION_STATUS, status.name().toLowerCase()));
				if(status.equals(ConnectionStatus.CONNECTED)) {
					labelConnection.getStyleClass().add("connected");
					labelConnection.getStyleClass().remove("disconnected");
				}
				else {
					labelConnection.getStyleClass().add("disconnected");
					labelConnection.getStyleClass().remove("connected");
				}
			}
		});
	}
	
	public void destroy() {
		if(autoconnectThread != null) autoconnectThread.kill();
		if(readerThread != null) readerThread.kill();
	}
	
	public Stage getStage() {
		return stage;
	}
	
}