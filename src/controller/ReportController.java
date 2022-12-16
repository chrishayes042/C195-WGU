package controller;

import DAO.SQLAppointmentDAO;
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
import model.Countries;
import model.Customers;
import service.AppointmentService;
import service.CountryService;
import service.CustomerService;


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
	private TableView<Appointments> appSchTableView;
	@FXML
	private TableColumn<Appointments, Integer> contactIdCol;
	@FXML
	private TableColumn<Appointments, Integer> appIdCol;
	@FXML
	private TableColumn<Appointments, String> appTypeCol;
	@FXML
	private TableColumn<Appointments, String> appTitleCol;
	@FXML
	private TableColumn<Appointments, String> appDescCol;
	@FXML
	private TableColumn<Appointments, LocalDateTime> appStartCol;
	@FXML
	private TableColumn<Appointments, LocalDateTime> appEndCol;
	@FXML
	private TableColumn<Appointments, Integer> custIdCol;
	@FXML
	private ComboBox<String> filterCombo;
	@FXML
	private Tab appTab;
	@FXML
	private TabPane reportTabPane;

	@FXML
	private TableView<Appointments> custAppTableView;
	@FXML
	private TableColumn<Appointments, String> custTableAppTypeCol;
	@FXML
	private TableColumn<Appointments, String> custTableMonthCol;
	@FXML
	private TableColumn<Appointments, Integer> custTableTotalAppCol;

	@FXML
	private TableView<Countries> custCountryTableView;
	@FXML
	private TableColumn<Countries, String> countryTableNameCol;
	@FXML
	private TableColumn<Countries, String> countryTableCustLocationCol;
	@FXML
	private TableColumn<Countries, String> countryTableCustAddrCol;


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
		ObservableList<Appointments> appList = SQLAppointmentDAO.getAppointments();

		contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
		appIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
		appTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
		appTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
		appDescCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
		appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
		appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
		custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

		appSchTableView.setItems(appList);
	}

	/**
	 * This method sets a tableview with a list of Report objects
	 * @throws SQLException
	 */
	void setCustAppTable() throws SQLException {
		ObservableList<Appointments> appList = AppointmentService.getReportCustAppByMonth();

		custTableAppTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
		custTableMonthCol.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
		custTableTotalAppCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

		custAppTableView.setItems(appList);
	}

	void setCountryTable() throws SQLException{
		ObservableList<Countries> reportsList = CountryService.getCountryData();

		countryTableNameCol.setCellValueFactory(new PropertyValueFactory<>("country"));
		countryTableCustLocationCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
		countryTableCustAddrCol.setCellValueFactory(new PropertyValueFactory<>("lastUpDtUser"));

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
				ObservableList<Appointments> custNameList = FXCollections.observableArrayList();

				appList.forEach(app -> {
					int custId = 0;
					try {
						custId = CustomerService.getCustIdFromName(filterCombo.getSelectionModel().getSelectedItem());
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					if (custId == app.getCustomerID()) {
						Appointments rep = new Appointments();
						rep.setContactID(app.getContactID());
						rep.setAppointmentID(app.getAppointmentID());
						rep.setAppointmentType(app.getAppointmentType());
						rep.setAppointmentTitle(app.getAppointmentTitle());
						rep.setAppointmentDescription(app.getAppointmentDescription());
						rep.setStart(app.getStart());
						rep.setEnd(app.getEnd());
						rep.setCustomerID(app.getCustomerID());
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
