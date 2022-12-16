package service;

import DAO.SQLAppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.SQLException;

/**
 * Service class to the SQLAppointmentDAO class
 * I added a service layer to make the code more secure as it does not touch the database and easier to read and follow the methods.
 * If the application were more complex, one would add the business logic in the methods.
 * Each method in the DAO class would be added here to be called throughout the application
 */
public interface AppointmentService {
	/**
	 * Method to return all appointments from the sql table
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Appointments> getAppointments() throws SQLException{
		return SQLAppointmentDAO.getAppointments();
	}

	/**
	 * Method to update an appointment to the sql table
	 * @param app
	 * @return Integer
	 * @throws SQLException
	 */
	public static int updateAppointment(Appointments app) throws SQLException{
		return SQLAppointmentDAO.updateAppointment(app);
	}

	/**
	 * Method to create a new appointment and add it to the sql table
	 * @param app
	 * @return Integer
	 * @throws SQLException
	 */
	public static int addNewAppointment(Appointments app) throws SQLException{
		return SQLAppointmentDAO.addNewAppointment(app);
	}

	/**
	 * Method to delete the appointment from the sql table
	 * @param app
	 * @throws SQLException
	 */
	public static void cancelAppointment(Appointments app) throws SQLException{
		SQLAppointmentDAO.cancelAppointment(app);
	}

	/**
	 * Method to check for appointment overlaps.
	 * Parameters takes in the Appointments Object and a boolean to check if it's an update or not.
	 * Creates a list of all appointments in the DB via getAppointments method.
	 * Utilizes a lambda expression to loop through each appointment in the list to check the start time and the appointment ID for doubles
	 * or in between the start and end times.
	 * Will return the appointment id of a match so the user can see which appointment is overlapped.
	 * @param app
	 * @param isUpdate
	 * @return Integer
	 * @throws SQLException
	 */
	public static Appointments checkAppointmentOverLap(Appointments app, boolean isUpdate) throws SQLException{

		ObservableList<Appointments> list = getAppointments();
		Appointments appointment = null;

		for(Appointments appointments : list){
			if (!isUpdate) {
				if (app.getStart().equals(appointments.getStart()) ||
						(app.getStart().isAfter(appointments.getStart()) && app.getStart().isBefore(appointments.getEnd()))) {

					appointment = (appointments);
				}
			} else {
				if(app.getAppointmentID() != appointments.getAppointmentID()){
					if (app.getStart().equals(appointments.getStart()) ||
							(app.getStart().isAfter(appointments.getStart()) &&
									app.getStart().isBefore(appointments.getEnd()))) {

						appointment = (appointments);
					}
				}
			}
		}

		return appointment;
	}
	/**
	 * Method to get the report for the total numebr of customer appointments by type and month
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Appointments> getReportCustAppByMonth() throws SQLException {

		return SQLAppointmentDAO.getCustReports();
	}

	/**
	 * Method used to set the report data for the Contact Table
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Appointments> getReportContactList() throws SQLException {
		ObservableList<Appointments> appList = SQLAppointmentDAO.getAppointments();
		ObservableList<Appointments> repList = FXCollections.observableArrayList();

		appList.forEach(app ->{
			Appointments apps = new Appointments();
//			apps.setContactID(app.getContactID());
//			apps.setAppointmentID(app.getAppointmentID());
//			apps.setReportAppType(app.getAppointmentType());
//			apps.setReportAppDesc(app.getAppointmentDescription());
//			apps.setReportAppStartDt(app.getStart());
//			apps.setReportAppEndDt(app.getEnd());
//			apps.setReportAppTitle(app.getAppointmentTitle());
//			apps.setReportCustId(app.getCustomerID());
			repList.addAll(apps);
		});
		return repList;

	}

}
