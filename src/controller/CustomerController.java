package controller;

import DAO.SQLCountryDAO;
import DAO.SQLCustomerDAO;
import DAO.SQLFirstLevelDivisionDAO;
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
import model.Customers;
import model.FirstLevelDivision;
import service.CountryService;
import service.CustomerService;
import service.FirstLevelDivisionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Customer Controller Class
 */
public class CustomerController implements Initializable {
	@FXML
	private TableView<Customers> custTableView;
	@FXML
	private TableColumn<Customers, Integer> custId;
	@FXML
	private TableColumn<Customers, String> custName;
	@FXML
	private TableColumn<Customers, String> address;
	@FXML
	private TableColumn<Customers, String> postal;
	@FXML
	private TableColumn<Customers, String> phone;
	@FXML
	private TableColumn<Customers, Integer> divisionId;
	@FXML
	private TextField custIdText;
	@FXML
	private TextField custNameText;
	@FXML
	private TextField custAddrText;
	@FXML
	private TextField custZipText;
	@FXML
	private TextField custPhoneText;
	@FXML
	private ComboBox<String> custDivisionIdCombo;


	/**
	 * Initialize method is called when the fxml is opened.
	 * This calls the set table and set combobox methods
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			setTable();
			setComboBoxes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set table method calls the customerDAO to get a list of all the customers in the DB.
	 * Takes that list and populates the table view.
	 * @throws SQLException
	 */
	void setTable() throws SQLException{
		ObservableList<Customers> custList = CustomerService.getAllCusts();
		custId.setCellValueFactory(new PropertyValueFactory<>("custId"));
		custName.setCellValueFactory(new PropertyValueFactory<>("custName"));
		address.setCellValueFactory(new PropertyValueFactory<>("address"));
		postal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
		phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		divisionId.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

		custTableView.setItems(custList);
	}

	/**
	 * Delete the customer method.
	 * Takes the Customers object from where the user clicked on the table.
	 * Warns user first. And if they click OK and Close...
	 * Sends the object to the deleteCustomer Method in the customerDAO.
	 * updates the table
	 * @throws SQLException
	 */
	@FXML
	private void deleteCust() throws SQLException {
		ButtonType buttonType = new ButtonType("Yes and Close", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("No and Close", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
		deleteAlert.getButtonTypes().setAll(buttonType, cancelButton);
		deleteAlert.setTitle("Delete Customer");
		deleteAlert.setContentText("Are you sure that you want to delete this customer?");
		Optional<ButtonType> result = deleteAlert.showAndWait();
		if(!result.isPresent()){
			// do nothing...
		} else if (result.get() == buttonType){
			Customers cust = custTableView.getSelectionModel().getSelectedItem();
			CustomerService.deleteCustomer(cust);
			Alert deleted = new Alert(Alert.AlertType.INFORMATION);
			String message = "Customer ID: " + cust.getCustId() + "has been deleted";
			deleted.setContentText(message);
			deleted.showAndWait();

		}
		clearText();
		setTable();

	}

	/**
	 * Clear the text boxes method.
	 */
	@FXML
	private void clearText(){
		custIdText.setText(null);
		custPhoneText.setText(null);
		custNameText.setText(null);
		custZipText.setText(null);
		custAddrText.setText(null);
		custDivisionIdCombo.getSelectionModel().selectFirst();
	}

	void setComboBoxes() throws SQLException{
		ObservableList<FirstLevelDivision> fldList = FirstLevelDivisionService.getAllDivisions();
		ObservableList<String> idList = FXCollections.observableArrayList();
		idList.add("Select...");
		fldList.forEach(fld->{
			idList.add(fld.getDivision());
		});

		custDivisionIdCombo.setItems(idList);
		custDivisionIdCombo.getSelectionModel().selectFirst();
	}

	/**
	 * Populates the text boxes when an item is clicked on the table
	 * I have a split method in there to parse out the "country address and the state name.
	 *
	 * @param mouseEvent
	 * @throws SQLException
	 */
	@FXML
	private void textPopulate(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
		Customers cust = custTableView.getSelectionModel().getSelectedItem();
		if(cust != null) {
			custIdText.setText(String.valueOf(cust.getCustId()));
			custPhoneText.setText(cust.getPhone());
			custNameText.setText(cust.getCustName());
			custZipText.setText(cust.getPostalCode());

			String custAddr = cust.getAddress();
			String[] tokens = custAddr.split(":|\\,");
			if (tokens.length == 4) {
				custAddrText.setText(tokens[1].trim() + ", " + tokens[2].trim());
			} else {
				custAddrText.setText(tokens[1].trim());
			}

			int id = cust.getDivisionId();
			FirstLevelDivision fld = FirstLevelDivisionService.getSingleDivision(id, " ", false);
			custDivisionIdCombo.getSelectionModel().select(fld.getDivision());
		}
	}

	/**
	 * Method to get the add new customer page
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void getAddCustPage(javafx.event.ActionEvent event) throws IOException{
		switchToAddPage(event, "/view/add-customer-view.fxml");
	}

	/**
	 * Switch method to get any page
	 * @param event
	 * @param switchScreen
	 * @throws IOException
	 */
	@FXML
	private void switchToAddPage(javafx.event.ActionEvent event, String switchScreen) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource(switchScreen));

		Scene scene = new Scene(parent);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	/**
	 * Exit method when user clicks on the "Exit" button.
	 * @param actionEvent
	 * @throws IOException
	 */
	public void exitWindow(ActionEvent actionEvent) throws IOException {
		((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
	}

	/**
	 * Update Customer Method with the text box check for errors.
	 * Converts the division id from the combo box into the division_id attribute by calling the getDivisionById method.
	 * Added a string builder to build a string to set the address so it shows the full address in the table.
	 * Has an alert to show user that which user was updated
	 * @throws SQLException
	 */
	@FXML
	private void updateCustomer() throws SQLException {

		Customers updateCust = new Customers();
		if (textCheck()) {

			updateCust.setCustId(Integer.parseInt(custIdText.getText().trim()));
			updateCust.setPhone(custPhoneText.getText().trim());
			updateCust.setCustName(custNameText.getText().trim());
			updateCust.setPostalCode(custZipText.getText().trim());

			updateCust.setAddress(custAddrText.getText().trim());

			String comboDivision = custDivisionIdCombo.getSelectionModel().getSelectedItem();
			FirstLevelDivision division = FirstLevelDivisionService.getSingleDivision(0, comboDivision, true);

			updateCust.setDivisionId(division.getDivisionId());

			int countryId = division.getCountryId();
			String country = CountryService.getCountryByID(countryId);
			StringBuilder sb = new StringBuilder();
			sb.append(country);
			sb.append(" address: ");
			String address = custAddrText.getText().trim();
			sb.append(address);
			sb.append(", ").append(division.getDivision());
			updateCust.setAddress(sb.toString());

			updateCust.setCreatedBy("User");
			updateCust.setLastUpdtUser(updateCust.getCreatedBy());

			int updated = CustomerService.updateCust(updateCust);
			if (updated > 0) {
				setTable();
				Alert addedAlert = new Alert(Alert.AlertType.CONFIRMATION);
				addedAlert.setTitle("Customer Updated");
				String context = ("Customer ID: " + updateCust.getCustId() + " has been updated.");
				addedAlert.setContentText(context);
				addedAlert.showAndWait();
			}

			clearText();
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
		if (custIdText.getText().trim().isEmpty()) {
			error += "The Customer ID is blank";
			textCheck = false;
		} else if (custPhoneText.getText().trim().isEmpty()) {
			error += "The Customer Phone is blank";
			textCheck = false;
		} else if (custNameText.getText().trim().isEmpty()) {
			error += "The Customer Name is blank";
			textCheck = false;
		} else if (custZipText.getText().trim().isEmpty()) {
			error += "The Postal Code is blank";
			textCheck = false;
		} else if (custAddrText.getText().trim().isEmpty()) {
			error += "The Address is blank";
			textCheck = false;
		}  else if (custDivisionIdCombo.getSelectionModel().isSelected(0)) {
			error += "Must Select a Division ID";
			textCheck = false;
		}

		if (!textCheck) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error on Customer Panel");
			alert.setContentText(error);
			alert.showAndWait();
		}
		return textCheck;
	}
}
