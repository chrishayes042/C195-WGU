package DAO;

import helper.Constants.Constants;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import service.AppointmentService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DAO class for the Appointments only class that will touch the DB
 */
public class SQLAppointmentDAO implements AppointmentService {
	/**
	 * Method that gets called to instantiate the FindAllAppointments class to call the execute method.
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Appointments> getAppointments() throws SQLException {
		FindAllAppointments apps = new FindAllAppointments();
		return apps.execute();
	}

	/**
	 * Class to find all appointments
	 */
	private static class FindAllAppointments {
		/**
		 * Execute method. Creates the SQL Query String and passes that to the PreparedStatement to pass into the ResultSet
		 * The result set goes through each row in the sql table and sets a new Appointment object to be passed into the List
		 * @return ObservableList
		 * @throws SQLException
		 */
		public ObservableList<Appointments> execute() throws SQLException {
			String sql = "SELECT * from Appointments";
			PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

			while (resultSet.next()) {
				Appointments app = new Appointments();
				app.setAppointmentID(resultSet.getInt(1));
				app.setAppointmentTitle(resultSet.getString(2));
				app.setAppointmentDescription(resultSet.getString(3));
				app.setAppointmentLocation(resultSet.getString(4));
				app.setAppointmentType(resultSet.getString(5));
				app.setStart(resultSet.getTimestamp(6).toLocalDateTime());
				app.setEnd(resultSet.getTimestamp(7).toLocalDateTime());
				app.setCreatedDate(resultSet.getTimestamp(8).toLocalDateTime());
				app.setCreatedBy(resultSet.getString(9));
				app.setLastUpDt(resultSet.getTimestamp(10).toLocalDateTime());
				app.setLastUpdtUser(resultSet.getString(11));
				app.setCustomerID(resultSet.getInt(12));
				app.setUserID(resultSet.getInt(13));
				app.setContactID(resultSet.getInt(14));
				appointmentsList.addAll(app);
			}
			return appointmentsList;
		}
	}

	/**
	 * Method used to instantiate the UpdateAppointment class to call the execute method.
	 *  Takes in an Appointment object
	 * @param app
	 * @return Integer
	 * @throws SQLException
	 */

	public static int updateAppointment(Appointments app) throws SQLException {
		UpdateAppointment update = new UpdateAppointment();
		return update.execute(app);
	}

	/**
	 * Class to Update an Appointment
	 */
	private static class UpdateAppointment {
		/**
		 * Execute method creates the update sql query. passes it into the PreparedStatement to be executed. Passes in the
		 * Appointment object attributes to be updated using the appointment id
		 * Returns the appointment id int.
		 * @param app
		 * @return Integer
		 * @throws SQLException
		 */
		public int execute(Appointments app) throws SQLException {
			String sql = "UPDATE appointments " +
					"SET Title = ?, " +
					"Description = ?, " +
					"Location = ?," +
					"Type = ?, " +
					"Start = ?, " +
					"End = ?, " +
					"Create_Date = ?, " +
					"Created_By = ?, " +
					"Last_Update = ?," +
					" Last_Updated_By = ?," +
					"Customer_ID = ?," +
					"User_ID = ?, " +
					"Contact_ID = ? " +
					"WHERE appointment_ID = " + app.getAppointmentID();

			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ps.setString(1, app.getAppointmentTitle());
			ps.setString(2, app.getAppointmentDescription());
			ps.setString(3, app.getAppointmentLocation());
			ps.setString(4, app.getAppointmentType());
			ps.setTimestamp(5, Timestamp.valueOf(app.getStart()));
			ps.setTimestamp(6, Timestamp.valueOf(app.getEnd()));
			ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(8, app.getCreatedBy());
			ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(10, app.getLastUpdtUser());
			ps.setInt(11, app.getCustomerID());
			ps.setInt(12, app.getUserID());
			ps.setInt(13, app.getContactID());

			ps.executeUpdate();
			return app.getAppointmentID();
		}
	}

	/**
	 * Method to instantiate the AddAppointment class to call the execute method.
	 * Returns an Appointment object
	 * Takes in an Appointments object.
	 * @param app
	 * @return
	 * @throws SQLException
	 */
	public static int addNewAppointment(Appointments app) throws SQLException {
		AddAppointment add = new AddAppointment();
		return add.execute(app);
	}

	/**
	 * Class to add an appointment
	 */
	private static class AddAppointment {
		/**
		 * Execute method creates the update sql query. passes it into the PreparedStatement to be executed. Passes in the
		 * Appointment object attributes to be added into the appointments table
		 * Returns the appointment id int.
		 * @param app
		 * @return Integer
		 * @throws SQLException
		 */
		public int execute(Appointments app) throws SQLException {
			String sql = "INSERT INTO Appointments " +
					"(Appointment_ID, " +
					"Title, " +
					"Description, " +
					"Location, " +
					"Type, " +
					"Start, " +
					"End, " +
					"Create_Date, " +
					"Created_By, " +
					"Last_Update, " +
					"Last_Updated_By, " +
					"Customer_ID, " +
					"User_ID, " +
					"Contact_ID)" +

					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

			ps.setInt(1, app.getAppointmentID());
			ps.setString(2, app.getAppointmentTitle());
			ps.setString(3, app.getAppointmentDescription());
			ps.setString(4, app.getAppointmentLocation());
			ps.setString(5, app.getAppointmentType());
			ps.setTimestamp(6, Timestamp.valueOf(app.getStart()));
			ps.setTimestamp(7, Timestamp.valueOf(app.getEnd()));
			ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(9, app.getCreatedBy());
			ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(11, app.getLastUpdtUser());
			ps.setInt(12, app.getCustomerID());
			ps.setInt(13, app.getUserID());
			ps.setInt(14, app.getContactID());

			return ps.executeUpdate();
		}
	}

	/**
	 * Method used to cancel an appointment. Instatiates the CancelAppointment class to call the execute method.
	 * Takes in an Appointments object
	 * Returns nothing
	 * @param app
	 * @throws SQLException
	 */
	public static void cancelAppointment(Appointments app) throws SQLException {
		CancelAppointment ca = new CancelAppointment();
		ca.execute(app);
	}

	/**
	 * Class to cancel an appointment
	 */
	private static class CancelAppointment {
		/**
		 * Execute method creates the update sql query. passes it into the PreparedStatement to be executed. Passes in the
		 * the appointment id and deletes that row from the table
		 * @param app
		 * @throws SQLException
		 */
		public void execute(Appointments app) throws SQLException {
			String sql = "DELETE from appointments where Appointment_ID = " + app.getAppointmentID();
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ps.executeUpdate();
		}
	}

	/**
	 * Method to call the class to call the execute method
	 * @return List
	 * @throws SQLException
	 */
	public static ObservableList<Appointments> getCustReports() throws SQLException{
		FindCustReport fcr = new FindCustReport();
		return fcr.execute();
	}

	/**
	 * Class to find the customer reports
	 */
	private static class FindCustReport {
		/**
		 * Execute method creates the sql string to pass into the sql database.
		 * The sql query gets the months as an integer.
		 * Used a switch statement for that int to match which Month it lines up with and set the String to that month
		 * @return
		 * @throws SQLException
		 */
		public  ObservableList<Appointments> execute()throws SQLException{
			String sql = "SELECT MONTH(Start) as Month, Type, COUNT(*) as Number from appointments GROUP BY Month, Type";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			String month = " ";
			ObservableList<Appointments> list = FXCollections.observableArrayList();
			while(rs.next()){
				Appointments app = new Appointments();
				// gets the month by an int from the sql table
				int monthInt = rs.getInt(1);

				app.setAppointmentType(rs.getString(2));
				app.setAppointmentID(rs.getInt(3));
				// uses a switch statement to get the correct string for the month
				switch(monthInt){
					case Constants.JAN_INT:
						month = Constants.JANUARY;
						break;
					case Constants.FEB_INT:
						month = Constants.FEBRUARY;
						break;
					case Constants.MAR_INT:
						month = Constants.MARCH;
						break;
					case Constants.APR_INT:
						month = Constants.APRIL;
						break;
					case Constants.MAY_INT:
						month = Constants.MAY;
						break;
					case Constants.JUNE_INT:
						month = Constants.JUNE;
						break;
					case Constants.JULY_INT:
						month = Constants.JULY;
						break;
					case Constants.AUG_INT:
						month = Constants.AUGUST;
						break;
					case Constants.SEP_INT:
						month = Constants.SEPTEMBER;
						break;
					case Constants.OCT_INT:
						month = Constants.OCTOBER;
						break;
					case Constants.NOV_INT:
						month = Constants.NOVEMBER;
						break;
					case Constants.DEC_INT:
						month = Constants.DECEMBER;
						break;

				}
				app.setAppointmentLocation(month);
				list.addAll(app);
			}
			return list;
		}
	}


//
//	public static ObservableList<Reports> getContactReportList()throws SQLException{
//		FindContactReportList fcpl = new FindContactReportList();
//		return fcpl.execte();
//	}
//
//	private static class FindContactReportList{
//
//		public ObservableList<Reports> execte() throws SQLException{
//			String sql = "SELECT * from appointments a LEFT JOIN contacts c ON a.Contact_ID = c.contact_id";
//			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
//			ObservableList<Reports> repList = FXCollections.observableArrayList();
//			while (rs.next()){
//				Reports rep = new Reports();
//				rep.setReportCustId(rs.getInt(1));
//				rep.setReportAppId(rs.getInt(2));
//
//
//				repList.addAll(rep);
//			}
//			return repList;
//		}
//	}
}




