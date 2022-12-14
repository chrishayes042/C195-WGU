package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import model.Reports;
import service.AppointmentService;
import service.CustomerService;
import service.ReportService;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Report Controller Class
 */
public class ReportController implements Initializable {

	@FXML
	private TableView<Reports> appSchTableView;
	@FXML
	private TableColumn<Reports, Integer> contactIdCol;
	@FXML
	private TableColumn<Reports, Integer> appIdCol;
	@FXML
	private TableColumn<Reports, String> appTypeCol;
	@FXML
	private TableColumn<Reports, String> appTitleCol;
	@FXML
	private TableColumn<Reports, String> appDescCol;
	@FXML
	private TableColumn<Reports, LocalDateTime> appStartCol;
	@FXML
	private TableColumn<Reports, LocalDateTime> appEndCol;
	@FXML
	private TableColumn<Reports, Integer> custIdCol;
	@FXML
	private ComboBox<String> filterCombo;
	@FXML
	private Tab appTab;
	@FXML
	private TabPane reportTabPane;

	@FXML
	private TableView<Reports> custAppTableView;
	@FXML
	private TableColumn<Reports, String> custTableAppTypeCol;
	@FXML
	private TableColumn<Reports, String> custTableMonthCol;
	@FXML
	private TableColumn<Reports, Integer> custTableTotalAppCol;

	@FXML
	private TableView<Reports> custCountryTableView;
	@FXML
	private TableColumn<Reports, String> countryTableNameCol;
	@FXML
	private TableColumn<Reports, String> countryTableCustLocationCol;
	@FXML
	private TableColumn<Reports, String> countryTableCustAddrCol;


	/**
	 * Initialize method is called when the controller is called
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
				setCustAppTable();
				setAppSchTable();
				setComboBox();
				setCountryTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sets the table with a list of Reports object
	 * @throws SQLException
	 */
	@FXML
	void setAppSchTable() throws SQLException{
		ObservableList<Reports> reportsList = ReportService.getReportContactList();

		contactIdCol.setCellValueFactory(new PropertyValueFactory<>("reportContId"));
		appIdCol.setCellValueFactory(new PropertyValueFactory<>("reportAppId"));
		appTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportAppType"));
		appTitleCol.setCellValueFactory(new PropertyValueFactory<>("reportAppTitle"));
		appDescCol.setCellValueFactory(new PropertyValueFactory<>("reportAppDesc"));
		appStartCol.setCellValueFactory(new PropertyValueFactory<>("reportAppStartDt"));
		appEndCol.setCellValueFactory(new PropertyValueFactory<>("reportAppEndDt"));
		custIdCol.setCellValueFactory(new PropertyValueFactory<>("reportCustId"));

		appSchTableView.setItems(reportsList);
	}

	/**
	 * This method sets a tableview with a list of Report objects
	 * @throws SQLException
	 */
	void setCustAppTable() throws SQLException {
		ObservableList<Reports> reportsList = ReportService.getReportCustAppByMonth();

		custTableAppTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportAppType"));
		custTableMonthCol.setCellValueFactory(new PropertyValueFactory<>("reportAppMonth"));
		custTableTotalAppCol.setCellValueFactory(new PropertyValueFactory<>("reportTotal"));

		custAppTableView.setItems(reportsList);
	}

	void setCountryTable() throws SQLException{
		ObservableList<Reports> reportsList = ReportService.getCountryData();

		countryTableNameCol.setCellValueFactory(new PropertyValueFactory<>("reportCustName"));
		countryTableCustLocationCol.setCellValueFactory(new PropertyValueFactory<>("reportAppMonth"));
		countryTableCustAddrCol.setCellValueFactory(new PropertyValueFactory<>("reportAppType"));

		custCountryTableView.setItems(reportsList);

	}


	/**
	 * This method filters the tableview list.
	 * All it does is create a new list of Reports by matching the cust id that it gets from the combobox
	 * @param actionEvent
	 * @throws SQLException
	 */
	@FXML
	private void filterBy(javafx.event.ActionEvent actionEvent) throws SQLException{

			if (!filterCombo.getSelectionModel().isSelected(0)) {
				ObservableList<Appointments> appList = AppointmentService.getAppointments();
				ObservableList<Reports> custNameList = FXCollections.observableArrayList();

				appList.forEach(app -> {
					int custId = 0;
					try {
						custId = CustomerService.getCustIdFromName(filterCombo.getSelectionModel().getSelectedItem());
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					if (custId == app.getCustomerID()) {
						Reports rep = new Reports();
						rep.setReportContId(app.getContactID());
						rep.setReportAppId(app.getAppointmentID());
						rep.setReportAppType(app.getAppointmentType());
						rep.setReportAppTitle(app.getAppointmentTitle());
						rep.setReportAppDesc(app.getAppointmentDescription());
						rep.setReportAppStartDt(app.getStart());
						rep.setReportAppEndDt(app.getEnd());
						rep.setReportCustId(app.getCustomerID());
						custNameList.addAll(rep);
					}
				});
				appSchTableView.setItems(custNameList);
			} else {
				setAppSchTable();
			}

	}

	/**
	 * Method that sets the combobox items.
	 * Uses a changelistener for when the tabs change in the gui window.
	 *
	 * @throws SQLException
	 */
	private void setComboBox() throws SQLException {

		reportTabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				ObservableList<Customers> custList = null;
				try {
					custList = CustomerService.getAllCusts();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ObservableList<String> custNameList = FXCollections.observableArrayList();
				if(reportTabPane.getSelectionModel().isSelected(1)){
					custNameList.clear();

					custNameList.add("Select...");
					custList.forEach(cust ->{
						custNameList.add(cust.getCustName());
					});

					filterCombo.setItems(custNameList);
					filterCombo.getSelectionModel().selectFirst();
				}
		}
		});

	}

	/** Method to exit the program
	 *
	 * @param actionEvent
	 * @throws IOException
	 */
	@FXML
	public void exitWindow(ActionEvent actionEvent) throws IOException {
		((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
	}


}
