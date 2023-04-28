package rfidhandler;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rfidhandler.entity.animal.Animal;
import rfidhandler.entity.animal.AnimalHandler;
import rfidhandler.entity.rfid.RfidUid;
import rfidhandler.entity.vaccine.VaccineHandler;
import rfidhandler.entity.vaccine.ui.VaccineAddDialog;
import rfidhandler.entity.vaccine.ui.VaccineTable;
import rfidhandler.thread.SerialAutoconnectThread;
import rfidhandler.thread.SerialReaderThread;

public class App {
	
	public static final String APP_NAME = "RfidHandler";
	public static final String FORMATTER_CONNECTION_STATUS = "Arduino is %s";
	public static final String FORMATTER_LABEL_ID = "ID: %s";
	public static final String FORMATTER_LABEL_RFID = "RFID: %s";
	public static final String FORMATTER_LABEL_NAME = "Name: %s";
	public static final String FORMATTER_LABEL_TYPE = "Type: %s";
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
		
		VBox box = new VBox(5);
		root.getChildren().add(box);
		
		box.getChildren().add(labelConnection = new Label(String.format(FORMATTER_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED.name().toLowerCase())));
		box.getChildren().add(labelRfid = new Label(String.format(FORMATTER_LABEL_RFID, "?")));
		box.getChildren().add(labelId = new Label(String.format(FORMATTER_LABEL_ID, "?")));
		box.getChildren().add(labelName = new Label(String.format(FORMATTER_LABEL_NAME, "?")));
		box.getChildren().add(labelType = new Label(String.format(FORMATTER_LABEL_TYPE, "?")));
		box.getChildren().add(blob = new ImageView(NO_IMAGE));
		box.getChildren().add(btnAddVaccine = new Button("Add vaccine"));
		box.getChildren().add(vaccineTable = new VaccineTable());
		
		blob.setFitWidth(100);
		blob.setFitHeight(100);
		
		btnAddVaccine.setOnAction(e -> {
			VaccineAddDialog dialog = new VaccineAddDialog();
			dialog.showAndWait();
			if(dialog.isCancelled()) return;
			VaccineHandler.addVaccine(animal.getId(), dialog.getTimestamp(), dialog.getDescription());
			vaccineTable.getItems().clear();
			vaccineTable.applyItems(animal.getVaccines());
		});
		btnAddVaccine.setDisable(true);
		
		serialAutoconnect();
		
		stage.setOnCloseRequest(e -> {
        	destroy();
        });
		stage.setScene(scene);
		stage.setWidth(400);
		stage.setHeight(600);
		stage.setTitle(APP_NAME);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        stage.show();
	}
	
	public void loadAnimal(String uid) {
		animal = AnimalHandler.loadAnimal(new RfidUid(uid));
		btnAddVaccine.setDisable(animal == null);
		labelRfid.setText(String.format(FORMATTER_LABEL_RFID, uid));
		vaccineTable.getItems().clear();
		if(animal != null) {
			labelId.setText(String.format(FORMATTER_LABEL_ID, Integer.toString(animal.getId())));
			labelName.setText(String.format(FORMATTER_LABEL_NAME, animal.getName()));
			labelType.setText(String.format(FORMATTER_LABEL_TYPE, animal.getType()));
			blob.setImage(animal.getImage());
			vaccineTable.applyItems(animal.getVaccines());
		} else {
			labelId.setText(String.format(FORMATTER_LABEL_ID, "?"));
			labelName.setText(String.format(FORMATTER_LABEL_NAME, "?"));
			labelType.setText(String.format(FORMATTER_LABEL_TYPE, "?"));
			blob.setImage(NO_IMAGE);
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