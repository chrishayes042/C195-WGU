package controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;
import model.Contacts;
import model.Customers;
import model.Users;
import service.AppointmentService;
import service.ContactService;
import service.CustomerService;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AddAppointemntController class
 */
public class AddAppointmentController implements Initializable {
	@FXML
	private TextField appIdText;
	@FXML
	private TextField appTitleText;
	@FXML
	private TextField appDescText;
	@FXML
	private TextField appLocText;
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
	private ComboBox<String> userIdCombo;

	@FXML
	private ComboBox<String> contactNameCombo;
	@FXML
	private ComboBox<String> custIdCombo;
	@FXML
	private Button exitButton;
	AppointmentController appC;

	/**
	 * Initialize method
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		try {
			setComboBoxes();
			ObservableList<Appointments> appsList = AppointmentService.getAppointments();
			ObservableList<Integer> appIdList = FXCollections.observableArrayList();

			appsList.forEach(app -> {
				appIdList.add(app.getAppointmentID());
			});
			AtomicInteger i = new AtomicInteger(1);
			AtomicInteger appId = new AtomicInteger(appsList.size() + 1);
			appsList.forEach(app -> {
				if (app.getAppointmentID() != i.get()) {
					appId.set(i.get());
				} else {
					i.getAndIncrement();
				}
			});


			appIdText.setText(String.valueOf(appId.get()));
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Method to add a new appointment to the sql server. Sends an Appointment Object to the appointmentDAO.
	 * Has a text check for errors.
	 * Has check for conflicting appointments.
	 * Sets the object using the text/comboboxes.
	 * Alerts user of creation.
	 * @throws SQLException
	 */
	@FXML
	private void addAppointment(javafx.event.ActionEvent event) throws SQLException, IOException {

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
			app.setCustomerID(CustomerService.getCustIdFromName(custIdCombo.getSelectionModel().getSelectedItem()));
			app.setUserID(UserService.findUserIdFromName(userIdCombo.getSelectionModel().getSelectedItem()));
			app.setCreatedBy(userIdCombo.getSelectionModel().getSelectedItem());
			app.setLastUpdtUser(userIdCombo.getSelectionModel().getSelectedItem());

			app.setContactID(ContactService.findContactId(contactNameCombo.getSelectionModel().getSelectedItem()));
			// Check if appointment has same ID or conflicting start date/time
			Appointments overLap = AppointmentService.checkAppointmentOverLap(app, false);
			if (overLap != null) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("This appointment is not allowed");
				alert.setContentText("The appointment conflicts with appointment ID: " + overLap.getAppointmentID() + " " + overLap.getAppointmentType());
				alert.showAndWait();
			} else {
				int added = AppointmentService.addNewAppointment(app);
				if (added > 0) {
					Alert addedAlert = new Alert(Alert.AlertType.CONFIRMATION);
					addedAlert.setTitle("New Appointment Added");
					addedAlert.setContentText("A new appointment has been added");
					addedAlert.showAndWait();
					exitPage(event, "/view/appointment-view.fxml");


				}
			}

		}

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
			error += "The Application ID is blank\n";
			textCheck = false;
		}
		if (appTitleText.getText().trim().isEmpty()) {
			error += "The Application Title is blank\n";
			textCheck = false;
		}
		if (appDescText.getText().trim().isEmpty()) {
			error += "The Application Description is blank\n";
			textCheck = false;
		}
		if (appLocText.getText().trim().isEmpty()) {
			error += "The Application Location is blank\n";
			textCheck = false;
		}
		if (appTypeText.getText().trim().isEmpty()) {
			error += "The Application Type is blank\n";
			textCheck = false;
		}
		if (startDatePicker.getValue() == null) {
			error += "The Start Date cannot be null\n";
			textCheck = false;
		}
		if (endDatePicker.getValue() == null) {
			error += "The End Date cannot be null\n";
			textCheck = false;
		}
		if (custIdCombo.getSelectionModel().isSelected(0)) {
			error += "Must Select a Customer ID\n";
			textCheck = false;
		}
		if (userIdCombo.getSelectionModel().isSelected(0)) {
			error += "Must Select a User ID\n";
			textCheck = false;
		}
		if (contactNameCombo.getSelectionModel().isSelected(0)) {
			error += "Select a Contact Name\n";
			textCheck = false;
		}
		if(startCombo.getSelectionModel().getSelectedIndex() == 0){
			error += "You must select a start time\n";
			textCheck = false;
		}
		if(endCombo.getSelectionModel().getSelectedIndex() == 0){
			error += "You must select a end time\n";
			textCheck = false;
		}
		if(startDatePicker.getValue() != null || endDatePicker.getValue() != null){

			if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
				error += "The start date cannot be after the end date\n";
				textCheck = false;
			}
			if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
				error += "The End date cannot be before the start date\n";
				textCheck = false;
			}
		}
		if(startCombo.getSelectionModel().getSelectedIndex() != 0 || endCombo.getSelectionModel().getSelectedIndex() != 0){
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime ltStart = LocalTime.parse(startCombo.getValue(), timeFormatter);
			LocalTime ltEnd = LocalTime.parse(endCombo.getValue(), timeFormatter);
			if (ltStart.isAfter(ltEnd)) {
				error += "The start time cannot be after the end time\n";
				textCheck = false;
			}
			if (ltEnd.isBefore(ltStart)) {
				error += "The End time cannot be before the start time\n";
				textCheck = false;
			}
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
	 * Get contacts from the contactDAO. Uses that to populate the contact names for the combo box.
	 * Used a lambda expression to loop through the custList and add each cust name to a new list to populate a combobox
	 *
	 * @throws SQLException
	 */
	protected void setComboBoxes() throws SQLException {
		// Gets all contacts into list
		ObservableList<Contacts> contactsList = ContactService.getAllContacts();
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

		ObservableList<Customers> custList = CustomerService.getAllCusts();
		ObservableList<String> custNameList = FXCollections.observableArrayList();
		custNameList.add("Select...");
		custList.forEach(cust -> custNameList.add(String.valueOf(cust.getCustName())));
		custIdCombo.setItems(custNameList);
		custIdCombo.getSelectionModel().selectFirst();

		ObservableList<Users> userList = UserService.getAllUsers();
		ObservableList<String> userNameList = FXCollections.observableArrayList();
		userNameList.add("Select...");
		userList.forEach(users -> userNameList.add(String.valueOf(users.getUserName())));
		userIdCombo.setItems(userNameList);
		userIdCombo.getSelectionModel().selectFirst();

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
	}
	/**
	 * Exit page method is used to exit the current screen and go to any other fxml view that is set
	 * @param event
	 * @param switchScreen
	 * @throws IOException
	 */
	@FXML
	private void exitPage(javafx.event.ActionEvent event, String switchScreen) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource(switchScreen));
		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	/**
	 * Clears the textboxes and resets the comboboxes
	 */
	@FXML
	private void clearText() {
		appTitleText.setText(null);
		appDescText.setText(null);
		appLocText.setText(null);
		appTypeText.setText(null);
		startDatePicker.setValue(null);
		endDatePicker.setValue(null);
		startCombo.getSelectionModel().selectFirst();
		endCombo.getSelectionModel().selectFirst();
		custIdCombo.getSelectionModel().selectFirst();
		userIdCombo.getSelectionModel().selectFirst();
		contactNameCombo.getSelectionModel().selectFirst();
	}

	/**
	 * Exits the current window and goes to the appointment-view
	 * @param actionEvent
	 * @throws IOException
	 */
	public void exitWindow(ActionEvent actionEvent) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("/view/appointment-view.fxml"));
		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	public void exitApplication(ActionEvent event) {
		Platform.exit();
	}
}
