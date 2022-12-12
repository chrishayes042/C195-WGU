package service;

import DAO.SQLAppointmentDAO;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.SQLException;

public interface AppointmentService {
	/**
	 * Method to return all appointments from the sql table
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<Appointments> getAppointments() throws SQLException{
		return SQLAppointmentDAO.getAppointments();
	}

	/**
	 * Method to update an appointment to the sql table
	 * @param app
	 * @return
	 * @throws SQLException
	 */
	public static int updateAppointment(Appointments app) throws SQLException{
		return SQLAppointmentDAO.updateAppointment(app);
	}

	/**
	 * Method to create a new appointment and add it to the sql table
	 * @param app
	 * @return
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
	 * Method to check for overlap in appointments
	 * @param app
	 * @param isUpdate
	 * @return
	 * @throws SQLException
	 */
	public static Appointments checkAppointmentOverLap(Appointments app, boolean isUpdate) throws SQLException{
		return SQLAppointmentDAO.checkAppointmentOverLap(app, isUpdate);
	}

}
