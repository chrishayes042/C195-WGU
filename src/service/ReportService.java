package service;

import DAO.SQLAppointmentDAO;
import DAO.SQLReportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Reports;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service class to the SQLReportDAO class
 * I added a service layer to make the code more secure as it does not touch the database and easier to read and follow the methods.
 * If the application were more complex, one would add the business logic in the methods.
 * Each method in the DAO class would be added here to be called throughout the application
 */
public interface ReportService {


	// Report that gets the total number of customer appointments by type and month

	/**
	 * Method to get the report for the total numebr of customer appointments by type and month
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<Reports> getReportCustAppByMonth() throws SQLException {

		return SQLReportDAO.getCustReports();
	}

	// Report that gets the schedule for each contact in your org.
	// includes Appointment ID, Title, type, and description, start, end and custid
	public static ObservableList<Reports> getReportContactList() throws SQLException {
		ObservableList<Appointments> appList = SQLAppointmentDAO.getAppointments();
		ObservableList<Reports> repList = FXCollections.observableArrayList();

		appList.forEach(app ->{
		Reports reports = new Reports();
			reports.setReportContId(app.getContactID());
			reports.setReportAppId(app.getAppointmentID());
			reports.setReportAppType(app.getAppointmentType());
			reports.setReportAppDesc(app.getAppointmentDescription());
			reports.setReportAppStartDt(app.getStart());
			reports.setReportAppEndDt(app.getEnd());
			reports.setReportAppTitle(app.getAppointmentTitle());
			reports.setReportCustId(app.getCustomerID());
			repList.addAll(reports);
		});
		return repList;

	}
	// report that of my choice that differ from the other 2.


}
