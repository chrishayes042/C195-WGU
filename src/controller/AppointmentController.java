package controller;


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
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.Optional;
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
	private ComboBox<String> filterCombo;
	@FXML
	private ComboBox<String> contactNameCombo;
	@FXML
	private ComboBox<String> custIdCombo;
	@FXML
	private RadioButton allRadioButton;
	@FXML
	private RadioButton weeklyRadioButton;
	@FXML
	private RadioButton monthlyRadioButton;

	private AppointmentService as;


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
		appIdText.setEditable(false);
		allRadioButton.setSelected(true);


	}

	/**
	 * Changes the current screen to the add appointment view.
	 * Calls the switchToAddPage method
	 *
	 * @param actionEvent
	 * @throws IOException
	 */
	@FXML
	public void getAddAppointmentPage(javafx.event.ActionEvent actionEvent) throws IOException {
		switchToAddPage(actionEvent, "../view/addAppointment-view.fxml");

	}


	/**
	 * Method used to update appointments.
	 * Uses the textCheck method to check for blank text boxes.
	 * Uses all text/combo boxes on the current screen.
	 * Takes the Appointments object from the table to populate the text boxes. Only uses the createdBy attribute from the table object.
	 * Checks for overlap using the checkAppointmentOverLap method
	 * @throws SQLException
	 */
	@FXML
	private void updateAppointment() throws SQLException {
		if (textCheck()) {
			Appointments app = new Appointments();
			Appointments clickedApp = appTableView.getSelectionModel().getSelectedItem();
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
			app.setCreatedBy(clickedApp.getCreatedBy());
			int userId = UserService.findUserIdFromName(userIdCombo.getSelectionModel().getSelectedItem());
			app.setLastUpdtUser(UserService.findUserNameFromId(userId));
			app.setContactID(ContactService.findContactId(contactNameCombo.getSelectionModel().getSelectedItem()));

			// Check if appointment has same ID or conflicting start date/time
			Appointments overLap = AppointmentService.checkAppointmentOverLap(app, true);
			if (overLap != null) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("This appointment is not allowed");
				alert.setContentText("The appointment conflicts with appointment ID: " + overLap.getAppointmentID() + " " + overLap.getAppointmentType());
				alert.showAndWait();
			} else {
				int updated = AppointmentService.updateAppointment(app);
				if (updated > 0) {
					setTable();
					Alert addedAlert = new Alert(Alert.AlertType.CONFIRMATION);
					addedAlert.setTitle("Appointment Updated");
					String context = ("Appointment ID: " + app.getAppointmentID() + " has been updated.");
					addedAlert.setContentText(context);
					addedAlert.showAndWait();

				}
			}
		}

	}

	/**
	 * Method to cancel an appointment.
	 * Shows confirmation pop up
	 * Gets Appointment object from table click
	 * Sends the Appointment object to the appointmentDAO to insert into MSSQL table
	 *
	 * @throws SQLException
	 */
	@FXML
	private void cancelAppointment() throws SQLException {
		ButtonType buttonType = new ButtonType("Yes and Close", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("No and Close", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
		deleteAlert.getButtonTypes().setAll(buttonType, cancelButton);
		deleteAlert.setTitle("Cancel Appointment");
		deleteAlert.setContentText("Are you sure that you want to cancel this appointment?");
		Optional<ButtonType> result = deleteAlert.showAndWait();
		if (!result.isPresent()) {
			// do nothing...
		} else if (result.get() == buttonType) {
			Appointments app = appTableView.getSelectionModel().getSelectedItem();
			AppointmentService.cancelAppointment(app);
			Alert canceled = new Alert(Alert.AlertType.WARNING);
			canceled.setTitle("Canceled Appointment");
			String message = "Appointment ID: " + app.getAppointmentID() + "\n" + "Appointment Type: " + app.getAppointmentType() + " has been canceled.";
			canceled.setContentText(message);
			canceled.showAndWait();
			clearText();
		} else if (result.get() == cancelButton) {
			deleteAlert.close();
			clearText();
		}


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

	/**
	 * Method to populate the combo boxes.
	 * Get contacts from the contactDAO. Uses that to populate the contact names for the combo box.
	 * Used a lambda expression on line 299 to loop through the custList and add each cust name to a new list to populate a combobox
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
		// create new list with strings

	}

	/**
	 * setTable method.
	 * Gets the Observable list from the appointmentDAO and uses that to populate the table data
	 *
	 * @throws SQLException
	 */
	@FXML
	void setTable() throws SQLException {
		monthlyRadioButton.setSelected(false);
		weeklyRadioButton.setSelected(false);
		clearText();
		ObservableList<Appointments> appList = null;
		try {
			appList = AppointmentService.getAppointments();
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
	 * Filter the table method. Gets a list of all appointments via getAppointments Method
	 * Utilizes a lambda to loop through the list to check if the end date is after the start week and before the end week variables
	 * Adds those Appointments objects to a new list to set the table data
	 *
	 * @param e
	 * @throws SQLException
	 */
	@FXML
	private void filterTableWeekly(ActionEvent e) throws SQLException {
		allRadioButton.setSelected(false);
		monthlyRadioButton.setSelected(false);
		ObservableList<Appointments> allAppsList = AppointmentService.getAppointments();
		ObservableList<Appointments> weeklyList = FXCollections.observableArrayList();

		LocalDateTime startWeek = LocalDateTime.now().minusWeeks(1);
		LocalDateTime endWeek = LocalDateTime.now().plusWeeks(1);

		allAppsList.forEach(app -> {
			if (app.getEnd().isAfter(startWeek) && app.getEnd().isBefore(endWeek)) {
				weeklyList.add(app);
			}
			appTableView.setItems(weeklyList);
		});
		clearText();

	}

	/**
	 * Filter the table method. Gets a list of all appointments via getAppointments Method
	 * Utilizes a lambda to loop through the list to check if the end date is after the start month and before the end month variables
	 * Adds those Appointments objects to a new list to set the table data
	 *
	 * @param e
	 * @throws SQLException
	 */
	@FXML
	private void filterTableMonthly(ActionEvent e) throws SQLException {
		allRadioButton.setSelected(false);
		weeklyRadioButton.setSelected(false);
		ObservableList<Appointments> allAppsList = AppointmentService.getAppointments();
		ObservableList<Appointments> monthList = FXCollections.observableArrayList();

		LocalDateTime startMonth = LocalDateTime.now().minusMonths(1);
		LocalDateTime endMonth = LocalDateTime.now().plusMonths(1);

		allAppsList.forEach(app -> {
			if (app.getEnd().isAfter(startMonth) && app.getEnd().isBefore(endMonth)) {
				monthList.add(app);
			}
			appTableView.setItems(monthList);
		});

		clearText();
	}

	/**
	 * Method to populate the textfields/datepicker/combobox with the information from the table
	 *
	 * @param mouseEvent
	 */
	@FXML
	private void textPopulate(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
		Appointments app = appTableView.getSelectionModel().getSelectedItem();
		if (app != null) {
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
			custIdCombo.getSelectionModel().select(app.getCustomerID());
			userIdCombo.getSelectionModel().select(app.getUserID());
			String name = ContactService.findContactName(app.getContactID());
			contactNameCombo.getSelectionModel().select(name);
		}

	}

	/**
	 * Clears the text fields and sets the combo boxes back to first selection
	 */
	@FXML
	private void clearText() {

		appIdText.setText(null);
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
	 * Method to switch the current screen to any screen where called
	 *
	 * @param event
	 * @param switchScreen
	 * @throws IOException
	 */
	@FXML
	private void switchToAddPage(javafx.event.ActionEvent event, String switchScreen) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource(switchScreen));

		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	/**
	 * Exits the current screen when user hits the exit button
	 *
	 * @param actionEvent
	 * @throws IOException
	 */
	public void exitWindow(ActionEvent actionEvent) throws IOException {
		((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
	}


}
