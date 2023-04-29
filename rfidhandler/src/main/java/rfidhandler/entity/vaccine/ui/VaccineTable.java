package rfidhandler.entity.vaccine.ui;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import rfidhandler.entity.vaccine.Vaccine;
import rfidhandler.entity.vaccine.VaccineHandler;

public class VaccineTable extends TableView<Vaccine> {
	
	public VaccineTable() {
		
		TableColumn<Vaccine, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		descriptionColumn.setOnEditCommit(e -> {
			Vaccine vaccine = e.getTableView().getItems().get(e.getTablePosition().getRow());
			vaccine.setDescription(e.getNewValue());
		});
		getColumns().add(descriptionColumn);
		
		TableColumn<Vaccine, Timestamp> timeColumn = new TableColumn<>("Time");
		timeColumn.setMinWidth(130);
		timeColumn.setMaxWidth(130);
		timeColumn.setResizable(false);
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		timeColumn.setCellFactory(new Callback<TableColumn<Vaccine,Timestamp>, TableCell<Vaccine,Timestamp>>() {
            @Override
            public TableCell<Vaccine,Timestamp> call(TableColumn<Vaccine,Timestamp> param) {
                return new TableCell<Vaccine,Timestamp>(){
                    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    @Override
                    protected void updateItem(Timestamp item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null){
                            setText("");
                        } else {
                            setText(format.format(item));
                        }
                    }
                };
            }
        });
		getColumns().add(timeColumn);

		TableColumn<Vaccine, Void> deleteColumn = new TableColumn<>("Action");
		deleteColumn.setMinWidth(50);
		deleteColumn.setMaxWidth(50);
		deleteColumn.setResizable(false);
		deleteColumn.setSortable(false);
		deleteColumn.setReorderable(false);
		deleteColumn.setCellFactory(new Callback<TableColumn<Vaccine, Void>, TableCell<Vaccine, Void>>() {
			@Override
			public TableCell<Vaccine, Void> call(TableColumn<Vaccine, Void> param) {
				final TableCell<Vaccine, Void> cell = new TableCell<Vaccine, Void>() {
					private final Button deleteButton = new Button("X");

					{
						deleteButton.setOnAction((event) -> {
							Vaccine vaccine = getTableView().getItems().get(getIndex());
							getTableView().getItems().remove(vaccine);
							VaccineHandler.deleteVaccine(vaccine);
						});
					}

					@Override
					protected void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(deleteButton);
						}
					}
				};
				return cell;
			}
		});
		getColumns().add(deleteColumn);

		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setMaxHeight(150);
		setMinWidth(380);
		setEditable(true);
		getStyleClass().add("vaccine-table");
		descriptionColumn.getStyleClass().add("table-column");
		timeColumn.getStyleClass().add("table-column");
		deleteColumn.getStyleClass().add("table-column");
		deleteColumn.getStyleClass().add("action");
	}
	
	public void applyItems(LinkedList<Vaccine> list) {
		for(Vaccine vaccine : list) getItems().add(vaccine);
	}
	
}
