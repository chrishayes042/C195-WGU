package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Main controller class for the main-view.fxml
 */
public class MainViewController implements Initializable {
	@FXML
	private Button appointmentButton;


	/**
	 * initialize method
	 *
	 * @throws SQLException
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	/**
	 * method to go to the appointment-view.fxml
	 *
	 * @param actionEvent
	 * @throws IOException
	 */
	public void goToAppointmentsPage(javafx.event.ActionEvent actionEvent) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/appointment-view.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setTitle("Appointments");
		stage.setScene(new Scene(root));
		stage.show();

	}

	/**
	 * Method to open the customer view
	 *
	 * @param actionEvent
	 * @throws IOException
	 */
	public void goToCustPage(javafx.event.ActionEvent actionEvent) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/customer-view.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setTitle("Customers");
		stage.setScene(new Scene(root));
		stage.show();
	}

	/**
	 * Method to open the reports view
	 * @param actionEvent
	 * @throws IOException
	 */
	public void goToReportView(javafx.event.ActionEvent actionEvent) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/reports-view.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setTitle("Reports");
		stage.setScene(new Scene(root));
		stage.show();
	}

	/** Method to exit the program
	 *
	 * @param actionEvent
	 * @throws IOException
	 */
	public void exitWindow(ActionEvent actionEvent) throws IOException {
		((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
	}
}
