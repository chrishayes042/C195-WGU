package controller;

import DAO.appointmentDAO;
import DAO.contactDAO;
import DAO.customerDAO;
import DAO.userDAO;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		try {
			setComboBoxes();
			ObservableList<Appointments> appsList = appointmentDAO.getAppointments();
			ObservableList<Integer> appIdList = FXCollections.observableArrayList();

			appsList.forEach(app -> {
				appIdList.add(app.getAppointmentID());
			});
			AtomicInteger i = new AtomicInteger(1);
			AtomicInteger appId = new AtomicInteger(appsList.size() + 1);
			appsList.forEach(app -> {
				if(app.getAppointmentID() != i.get()){
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
	 * Has a text check for errors
	 * Has check for conflicting appointments
	 * @throws SQLException
	 */
	@FXML
	private void addAppointment(javafx.event.ActionEvent event) throws SQLException, IOException {

		ObservableList<Appointments> appsList = appointmentDAO.getAppointments();
		ObservableList<Contacts> contactsList = contactDAO.getAllContacts();
		ObservableList<Users> userList = userDAO.getAllUsers();
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

			app.setCustomerID(Integer.parseInt(custIdCombo.getSelectionModel().getSelectedItem()));
			app.setUserID(Integer.parseInt(userIdCombo.getSelectionModel().getSelectedItem()));

			int userId = Integer.parseInt(userIdCombo.getSelectionModel().getSelectedItem());
			String userName = userDAO.findUserNameFromId(userId);
			app.setCreatedBy(userName);
			app.setLastUpdtUser(userName);

			String contactName = contactNameCombo.getSelectionModel().getSelectedItem();
			int cId = contactDAO.findContactId(contactName);
			app.setContactID(cId);
			// Check if appointment has same ID or conflicting start date/time
			int overLap = appointmentDAO.checkAppointmentOverLap(app, false);
			if(overLap > 0){
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("This appointment is not allowed");
				alert.setContentText("The appointment conflicts with appointment ID: " + overLap);
				alert.showAndWait();
			} else {
				int added = appointmentDAO.addNewAppointment(app);
				if(added > 0){
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
		} else if (custIdCombo.getSelectionModel().isSelected(0)) {
			error += "Must Select a Customer ID";
			textCheck = false;
		} else if (userIdCombo.getSelectionModel().isSelected(0)) {
			error += "Must Select a User ID";
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

	protected void setComboBoxes() throws SQLException {
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

		ObservableList<Customers> custList = customerDAO.getAllCusts();
		ObservableList<String> custIDList = FXCollections.observableArrayList();
		custIDList.add("Select...");
		custList.forEach(cust -> custIDList.add(String.valueOf(cust.getCustId())));
		custIdCombo.setItems(custIDList);
		custIdCombo.getSelectionModel().selectFirst();

		ObservableList<Users> userList = userDAO.getAllUsers();
		ObservableList<String> userIdList = FXCollections.observableArrayList();
		userIdList.add("Select...");
		userList.forEach(users -> userIdList.add(String.valueOf(users.getUserID())));
		userIdCombo.setItems(userIdList);
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
	@FXML
	private void exitPage(javafx.event.ActionEvent event, String switchScreen) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource(switchScreen));
		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	private void clearText(){
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


	public void exitWindow(ActionEvent actionEvent) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("/view/appointment-view.fxml"));
		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	public void exitApplication(ActionEvent event) {
		Platform.exit();
	}
}
