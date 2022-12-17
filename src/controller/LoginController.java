package controller;

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
import helper.logger.Logger;
import service.AppointmentService;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Login Controller Class
 */
public class LoginController implements Initializable {


	@FXML
	private Label userLocationLabel1;
	@FXML
	private Label userNameLabel;
	@FXML
	private Label userPassLabel;
	@FXML
	private Button signInButton;
	@FXML
	private TextField userNameText;
	@FXML
	private TextField pass;
	ResourceBundle resourceBundle;
	private int userId;
	private final Locale locale = Locale.getDefault();

	private LocalDateTime currentPlus15 = LocalDateTime.now().plusMinutes(15);
	private LocalDateTime currentMin15 = LocalDateTime.now().minusMinutes(15);

	/**
	 * Initialize the controller.
	 * Sets the Locale of the system. Sets the labels to the language using the resource bundle
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		Locale.setDefault(locale);
		resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());

		userLocationLabel1.setText(resourceBundle.getString("country"));
		userNameLabel.setText(resourceBundle.getString("userNameLabel"));
		userPassLabel.setText(resourceBundle.getString("passwordLabel"));
		signInButton.setText(resourceBundle.getString("signInButton"));
	}

	/**
	 * Method to validate the user login.
	 * Takes the user ID and passes it tho the validate users method in the UserService.
	 * If user is not in the sql db, will show an error alert. Will use the system's language to set the alert message.
	 * Also checks for an appointment within 15 mins. Will let the user know if there's no appointment.
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void validateUsers(ActionEvent event) throws IOException, SQLException {
		String userName = userNameText.getText().trim();
		userId = UserService.validateUsers(userNameText.getText().trim(), pass.getText().trim());
		boolean userValidate = validateUser(userId);
		Logger.loginLog(userName, userValidate);
		Locale.setDefault(locale);
		resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());


		if (!userValidate) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(resourceBundle.getString("alert"));
			alert.setHeaderText(resourceBundle.getString("alertHeader"));
			alert.setContentText(resourceBundle.getString("alertContent"));
			alert.showAndWait().ifPresent((response -> {
				System.out.println(resourceBundle.getString("alert"));
				Parent main = null;
				if (response == ButtonType.OK) {

				}
			}));
		} else {
			try {
				Appointments app = userHasAppointment(userId, currentPlus15, currentMin15);
				if (app.getAppointmentID() != 0) {
					Alert alertAppoint = new Alert(Alert.AlertType.CONFIRMATION);
					Locale.setDefault(locale);
					resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());
					alertAppoint.setTitle(resourceBundle.getString("alertApp"));
					alertAppoint.setHeaderText(resourceBundle.getString("alertAppHeader"));
					alertAppoint.setContentText(resourceBundle.getString("alertAppContent"));
					alertAppoint.showAndWait();
				} else {
					Alert alertAppoint = new Alert(Alert.AlertType.CONFIRMATION);
					Locale.setDefault(locale);
					resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());
					alertAppoint.setTitle(resourceBundle.getString("noApp"));
					alertAppoint.setContentText(resourceBundle.getString("noAppContent"));
					alertAppoint.showAndWait();
				}
				switchPage(event, "../view/main-view.fxml");


			} catch (IOException e) {
				e.printStackTrace();
			}



		}

	}

	/**
	 * Method to validate the user
	 * @param userId
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean validateUser(int userId) throws SQLException{
		userId = UserService.validateUsers(userNameText.getText().trim(), pass.getText().trim());
		boolean user = false;
		user = userId > 0;
		return user;
	}

	/**
	 * Method to look at each appointment.
	 * Uses a lambda expression to loop through the list of appointments to match the userId and appointment times.
	 * Creates a new Appointments object to return.
	 * @param userId
	 * @param currentTimePlus15
	 * @param currentTimeMin15
	 * @return Appointment object
	 * @throws SQLException
	 * @throws IOException
	 */
	private Appointments userHasAppointment(int userId, LocalDateTime currentTimePlus15, LocalDateTime currentTimeMin15) throws SQLException, IOException {
		ObservableList<Appointments> appList = AppointmentService.getAppointments();
		Appointments appoint = new Appointments();
		AtomicBoolean hasApp = new AtomicBoolean(false);
		appList.forEach(app -> {
			hasApp.set(app.getStart().equals(currentTimePlus15) && app.getUserID() == userId);
			if(hasApp.get()) {
				appoint.setAppointmentID(app.getAppointmentID());
				appoint.setAppointmentType(app.getAppointmentType());
				appoint.setAppointmentTitle(app.getAppointmentTitle());
				appoint.setAppointmentDescription(app.getAppointmentDescription());
			}
		});
		return appoint;
	}
	/**
	 * Method to switch the current screen to any screen where called
	 *
	 * @param event
	 * @param switchScreen
	 * @throws IOException
	 */
	@FXML
	private void switchPage(javafx.event.ActionEvent event, String switchScreen) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource(switchScreen));
		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}


}