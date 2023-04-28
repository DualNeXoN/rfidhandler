package rfidhandler.entity.vaccine.ui;

import java.sql.Timestamp;
import java.util.LinkedList;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rfidhandler.entity.vaccine.Vaccine;
import rfidhandler.utils.TimestampStringConverter;

public class VaccineTable extends TableView<Vaccine> {
	
	public VaccineTable() {
		
		TableColumn<Vaccine, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		TableColumn<Vaccine, Timestamp> timeColumn = new TableColumn<>("Time");
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		timeColumn.setCellFactory(col -> new TableCell<Vaccine, Timestamp>() {
		    @Override
		    protected void updateItem(Timestamp timestamp, boolean empty) {
		        super.updateItem(timestamp, empty);
		        if(empty || timestamp == null) {
		            setText("");
		        } else {
		            setText(new TimestampStringConverter().toString(timestamp));
		        }
		    }
		});
		
		getColumns().add(descriptionColumn);
		getColumns().add(timeColumn);
		
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setMaxHeight(200);
	}
	
	public void applyItems(LinkedList<Vaccine> list) {
		for(Vaccine vaccine : list) getItems().add(vaccine);
	}
	
}
