package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointments;
import model.Customers;
import model.Reports;
import service.AppointmentService;
import service.CustomerService;
import service.ReportService;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

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



	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
				setCustAppTable();
				setAppSchTable();
				setComboBox();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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
	void setCustAppTable() throws SQLException {
		ObservableList<Reports> reportsList = ReportService.getReportCustAppByMonth();

		custTableAppTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportAppType"));
		custTableMonthCol.setCellValueFactory(new PropertyValueFactory<>("reportAppMonth"));
		custTableTotalAppCol.setCellValueFactory(new PropertyValueFactory<>("reportTotal"));

		custAppTableView.setItems(reportsList);
	}
	@FXML
	private void filterBy(javafx.event.ActionEvent actionEvent) throws SQLException{
		if(!filterCombo.getSelectionModel().isSelected(0)) {
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

	private void setComboBox() throws SQLException {
			ObservableList<Customers> custList = CustomerService.getAllCusts();
			ObservableList<String> custNameList = FXCollections.observableArrayList();

		reportTabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				if(reportTabPane.getSelectionModel().isSelected(1)){
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


}
