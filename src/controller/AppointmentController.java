package controller;

import DAO.appointmentDAO;
import DAO.contactDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointments;
import model.Contacts;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Appointment Controller for the appointment-view.fxml
 */

public class AppointmentController implements Initializable {
	@FXML
	private TableView<Appointments> appTableView;
	@FXML
	private TableColumn<Appointments, Integer> appointmentId;
	@FXML
	private TableColumn<Appointments, String> title;
	@FXML
	private TableColumn<Appointments, String> desc;
	@FXML
	private TableColumn<Appointments, String> loc;
	@FXML
	private TableColumn<Appointments, String> type;
	@FXML
	private TableColumn<Appointments, LocalDateTime> start;
	@FXML
	private TableColumn<Appointments, LocalDateTime> end;
	@FXML
	private TableColumn<Appointments, String> custId;
	@FXML
	private TableColumn<Appointments, String> contactId;
	@FXML
	private TableColumn<Appointments, Integer> userId;
	@FXML
	private TextField appIdText;
	@FXML
	private TextField appTitleText;
	@FXML
	private TextField appDescText;
	@FXML
	private TextField appLocText;
	@FXML
	private TextField custIdText;
	@FXML
	private DatePicker startDatePicker;
	@FXML
	private DatePicker endDatePicker;
	@FXML
	private ComboBox<String> startCombo;
	@FXML
	private ComboBox<String> endCombo;
	@FXML
	private TextField appTypeText;
	@FXML
	private TextField userIdText;
	@FXML
	private ComboBox<String> filterCombo;
	@FXML
	private ComboBox<String> contactNameCombo;


	/**
	 * Initializes the controller.
	 * I have it setting the table data when the fxml is opened.
	 *
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		try {
			setTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to add a new appointment to the sql server. Sends an Appointment Object to the appointmentDAO.
	 * Has a text check for errors
	 *
	 * @throws SQLException
	 */
	@FXML
	private void addAppointment() throws SQLException {


		ObservableList<Contacts> contactsList = contactDAO.getAllContacts();
		Appointments app = new Appointments();

		if (textCheck()) {
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalDate ldStart = startDatePicker.getValue();
			LocalDate ldEnd = endDatePicker.getValue();
			LocalTime ltStart = LocalTime.parse(startCombo.getValue(), timeFormatter);
			LocalTime ltEnd = LocalTime.parse(endCombo.getValue(), timeFormatter);
			LocalDateTime ldtStart = LocalDateTime.of(ldStart, ltStart);
			LocalDateTime ldtEnd = LocalDateTime.of(ldEnd, ltEnd);

			app.setAppointmentID(Integer.parseInt(appIdText.getText().trim()));
			app.setAppointmentTitle(appTitleText.getText().trim());
			app.setAppointmentDescription(appDescText.getText().trim());
			app.setAppointmentLocation(appLocText.getText().trim());
			app.setAppointmentType(appTypeText.getText().trim());
			app.setStart(ldtStart);
			app.setEnd(ldtEnd);
//			app.setCustomerID(Integer.parseInt(custIdText.getText().trim()));
			app.setUserID(Integer.parseInt(userIdText.getText().trim()));

//			app.setCreatedDate(LocalDateTime.now());
			app.setCreatedBy("User");
//			app.setLastUpDt(LocalDateTime.now());
			app.setLastUpdtUser("User");
			String contactName = contactNameCombo.getSelectionModel().getSelectedItem();
			int cId = contactDAO.findContactId(contactName);
			app.setContactID(cId);

			appointmentDAO.addNewAppointment(app);
		}

	}

	/**
	 * Method to cancel an appointment.
	 * Gets Appointment object from table click
	 * Sends the Appointment object to the appointmentDAO
	 *
	 * @throws SQLException
	 */
	@FXML
	private void cancelAppointment() throws SQLException {
		Appointments app = appTableView.getSelectionModel().getSelectedItem();
		appointmentDAO.cancelAppointment(app);
		setTable();
	}

	/**
	 * This is the error checker that checks the textfield/datepicker/comboboxes for errors.
	 * Shows an alert if any errors
	 *
	 * @return boolean
	 */
	protected boolean textCheck() {
		boolean textCheck = true;
		String error = "";
		if (appIdText.getText().trim().isEmpty()) {
			error += "The Application ID is blank";
			textCheck = false;
		} else if (appTitleText.getText().trim().isEmpty()) {
			error += "The Application Title is blank";
			textCheck = false;
		} else if (appDescText.getText().trim().isEmpty()) {
			error += "The Application Description is blank";
			textCheck = false;
		} else if (appLocText.getText().trim().isEmpty()) {
			error += "The Application Location is blank";
			textCheck = false;
		} else if (appTypeText.getText().trim().isEmpty()) {
			error += "The Application Type is blank";
			textCheck = false;
		} else if (startDatePicker.getValue() == null) {
			error += "The Start Date cannot be null";
			textCheck = false;
		} else if (endDatePicker.getValue() == null) {
			error += "The End Date cannot be null";
			textCheck = false;
		} else if (custIdText.getText().trim().isEmpty()) {
			error += "The Customer ID is blank";
			textCheck = false;
		} else if (userIdText.getText().trim().isEmpty()) {
			error += "The User ID is blank";
			textCheck = false;
		} else if (contactNameCombo.getSelectionModel().isSelected(0)) {
			error += "Select a Contact Name";
			textCheck = false;
		} else if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
			error += "The start date cannot be after the end date";
			textCheck = false;
		} else if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
			error += "The End date cannot be before the start date";
			textCheck = false;
		}

		if (!textCheck) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error on Application Page");
			alert.setContentText(error);
			alert.showAndWait();
		}
		return textCheck;
	}

	/**
	 * Method to populate the combo boxes.
	 * Gets contacts from the contactDAO. Uses that to populate the contact names for the combo box.
	 *
	 * @throws SQLException
	 */
	private void setComboBoxes() throws SQLException {
		// Gets all contacts into list
		ObservableList<Contacts> contactsList = contactDAO.getAllContacts();
		// create new list of Strings to add to the combo box
		ObservableList<String> contactNameComboList = FXCollections.observableArrayList();
		// add first item
		contactNameComboList.add("Select...");
		// loop through each contact in the contacts list and add the contact name to the new string list
		contactsList.forEach(con -> contactNameComboList.add(con.getContactName()));
		// set the items in the combo box
		contactNameCombo.setItems(contactNameComboList);
		// Make first selection default
		contactNameCombo.getSelectionModel().selectFirst();

		// Create a new list of strings
		ObservableList<String> timesComboList = FXCollections.observableArrayList();
		// Create new local time object. Set it to 8am
		LocalTime firstAppTime = LocalTime.MIN.plusHours(8);
		// create new local time object. Set it
		LocalTime lastAppTime = LocalTime.MAX.minusHours(1).minusMinutes(45);
		// add first item to list
		timesComboList.add("Select...");
		// if statement
		if (!firstAppTime.equals(0) || !lastAppTime.equals(0)) {
			// while the first time is before the last time
			while (firstAppTime.isBefore(lastAppTime)) {
				// add the times to the list
				timesComboList.add(String.valueOf(firstAppTime));
				// make the first time added 15 minutes
				firstAppTime = firstAppTime.plusMinutes(15);
			}
		}
		startCombo.setItems(timesComboList);
		endCombo.setItems(timesComboList);
		startCombo.getSelectionModel().selectFirst();
		endCombo.getSelectionModel().selectFirst();
		// create new list with strings
		ObservableList<String> filterComboList = FXCollections.observableArrayList("All", "Weekly", "Monthly");
		filterCombo.setItems(filterComboList);
		filterCombo.getSelectionModel().selectFirst();
	}

	/**
	 * setTable method.
	 * Gets the Observable list from the appointmentDAO and uses that to populate the table data
	 *
	 * @throws SQLException
	 */
	private void setTable() throws SQLException {

		ObservableList<Appointments> appList = null;
		try {
			appList = appointmentDAO.getAppointments();
			setComboBoxes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		appointmentId.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("appointmentID"));
		title.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentTitle"));
		desc.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentDescription"));
		loc.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentLocation"));
		type.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentType"));
		start.setCellValueFactory(new PropertyValueFactory<Appointments, LocalDateTime>("start"));
		end.setCellValueFactory(new PropertyValueFactory<Appointments, LocalDateTime>("end"));
		custId.setCellValueFactory(new PropertyValueFactory<Appointments, String>("customerID"));
		contactId.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contactID"));
		userId.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("userID"));

		appTableView.setItems(appList);
	}

	/**
	 * Method to populate the textfields/datepicker/combobox with the information from the table
	 *
	 * @param mouseEvent
	 */
	@FXML
	private void textPopulate(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
		Appointments app = appTableView.getSelectionModel().getSelectedItem();
		appIdText.setText(String.valueOf(app.getAppointmentID()));
		appTitleText.setText(app.getAppointmentTitle());
		appDescText.setText(app.getAppointmentDescription());
		appLocText.setText(app.getAppointmentLocation());
		appTypeText.setText(app.getAppointmentType());
		LocalDate ldStart = app.getStart().toLocalDate();
		startDatePicker.setValue(ldStart);
		LocalDate ldEnd = app.getEnd().toLocalDate();
		endDatePicker.setValue(ldEnd);
		LocalTime ltStart = app.getStart().toLocalTime();
		String ltStartString = ltStart.toString();
		startCombo.getSelectionModel().select(ltStartString);
		LocalTime ltEnd = app.getEnd().toLocalTime();
		String ltEndString = ltEnd.toString();
		endCombo.getSelectionModel().select(ltEndString);
		custIdText.setText(String.valueOf(app.getCustomerID()));
		userIdText.setText(String.valueOf(app.getUserID()));
		String name = contactDAO.findContactName(app.getContactID());
		contactNameCombo.getSelectionModel().select(name);


	}


}
