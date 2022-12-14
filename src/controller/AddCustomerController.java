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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Countries;
import model.Customers;
import model.FirstLevelDivision;
import service.CountryService;
import service.CustomerService;
import service.FirstLevelDivisionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Add Customer Controller Class
 */
public class AddCustomerController implements Initializable {

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
	@FXML
	private ComboBox<String> countryListCombo;

	/**
	 * Initialize method is called when the fxml is opened.
	 * The method sets the combo boxes and sets the cust id text field
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			setComboBoxes();
			ObservableList<Customers> custList = CustomerService.getAllCusts();
			ObservableList<Integer> idList = FXCollections.observableArrayList();
			custList.forEach(customers -> {
				idList.add(customers.getCustId());
			});
			AtomicInteger i = new AtomicInteger(1);
			AtomicInteger custId = new AtomicInteger(custList.size() + 1);
			custList.forEach(customers -> {
				if(customers.getCustId() != i.get()){
					custId.set(i.get());
				} else {
					i.getAndIncrement();
				}
			});


			custIdText.setText(String.valueOf(custId.get()));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add new customer method.
	 * Uses the text fields to set the attributes for a new Customers object.
	 * The object is then sent to the DAO via the addNewCustomer method.
	 * Alerts the user of creation of new table row.
	 * @param event
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	private void addNewCust(javafx.event.ActionEvent event) throws SQLException, IOException{

		Customers newCust = new Customers();
		if(textCheck()){
			newCust.setCustId(Integer.parseInt(custIdText.getText().trim()));
			newCust.setCustName(custNameText.getText().trim());
			newCust.setPostalCode(custZipText.getText().trim());
			newCust.setPhone(custPhoneText.getText().trim());
			FirstLevelDivision fld = FirstLevelDivisionService.getSingleDivision(0, custDivisionIdCombo.getSelectionModel().getSelectedItem(), true);
			newCust.setDivisionId(fld.getDivisionId());

			newCust.setCreatedBy("User");
			newCust.setLastUpdtUser(newCust.getCreatedBy());

			int countryId = fld.getCountryId();
			String country = CountryService.getCountryByID(countryId);
			StringBuilder sb = new StringBuilder();
			sb.append(country);
			sb.append(" address: ");
			String address = custAddrText.getText().trim();
			sb.append(address);
			sb.append(", ").append(fld.getDivision());
			newCust.setAddress(sb.toString());


			int added = CustomerService.addNewCustomer(newCust);
			if(added > 0){
				Alert addedAlert = new Alert(Alert.AlertType.CONFIRMATION);
				addedAlert.setTitle("New Customer Added");
				addedAlert.setContentText("A new customer has been added");
				addedAlert.showAndWait();
				exitPage(event, "/view/customer-view.fxml");
			}
		}
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
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	/**
	 * Set combo box method. Sets the combo box items
	 * Gets a list of all the countries from the getAllCountries method
	 * Loops through that list and sets a new list and fills with the getCountry attribute string
	 * Sets the string list to the combobox
	 * @throws SQLException
	 */
	void setComboBoxes() throws SQLException{

		ObservableList<Countries> cList = CountryService.getAllCountries();
		ObservableList<String> cStringList = FXCollections.observableArrayList();
		cStringList.add("Select Country...");
		cList.forEach(c ->{
			cStringList.add(c.getCountry());
		});
		countryListCombo.setItems(cStringList);
		countryListCombo.getSelectionModel().selectFirst();


	}

	/**
	 * Clears the textboxs and resets the comboboxes
	 */
	@FXML
	private void clearText(){
		custPhoneText.setText(null);
		custNameText.setText(null);
		custZipText.setText(null);
		custAddrText.setText(null);
		custDivisionIdCombo.getSelectionModel().selectFirst();
		countryListCombo.getSelectionModel().selectFirst();
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
			alert.setTitle("Error on Application Page");
			alert.setContentText(error);
			alert.showAndWait();
		}
		return textCheck;
	}

	/**
	 * Filter method that filters the combo boxes for the division id.
	 * Gets all the first level division objects and loops through them to get a new list of the division strings if the
	 * Country combo is set to 0 (Select...).
	 * Once the user changes the country combo if filters to the other divisions in that country id.
	 * @throws SQLException
	 */
	@FXML
	private void filterComboBox() throws SQLException {
		if(countryListCombo.getSelectionModel().isSelected(0)){
			ObservableList<FirstLevelDivision> fldList = FirstLevelDivisionService.getAllDivisions();
			ObservableList<String> divList = FXCollections.observableArrayList();
			divList.add("Select...");
			fldList.forEach(fld->{
				divList.add(fld.getDivision());
			});

			custDivisionIdCombo.setItems(divList);
			custDivisionIdCombo.getSelectionModel().selectFirst();
		} else {
			ObservableList<Countries> cList = CountryService.getAllCountries();
			AtomicInteger cId = new AtomicInteger();
			cList.forEach(c -> {
				if(c.getCountry().equals(countryListCombo.getSelectionModel().getSelectedItem())){
					cId.set(c.getCountryId());
				}
			});
			ObservableList<FirstLevelDivision> fldList = FirstLevelDivisionService.getFldDivisionById(cId.get());
			ObservableList<String> divList = FXCollections.observableArrayList();
			divList.add("Select...");
			fldList.forEach(fld -> {
				divList.add(fld.getDivision());
			});
			custDivisionIdCombo.setItems(divList);
			custDivisionIdCombo.getSelectionModel().selectFirst();
		}
	}

	/**
	 * Exits the screen on the exit button
	 * @param actionEvent
	 * @throws IOException
	 */
	public void exitScreen(ActionEvent actionEvent) throws IOException {
		exitPage(actionEvent, "/view/customer-view.fxml");
	}
}
