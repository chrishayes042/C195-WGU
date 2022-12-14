package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Customers;
import service.CustomerService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO Class for the Customer. Only class that will touch the DB.
 */
public class SQLCustomerDAO implements CustomerService {

	public static ObservableList<Customers> getAllCusts() throws SQLException {
		FindAllCustomers fac = new FindAllCustomers();
		return fac.execute();
	}

	/**
	 * Class to find all the customers in the SQL Table
	 */
	private static class FindAllCustomers {

		public ObservableList<Customers> execute() throws SQLException {
			String sql = "SELECT * from customers";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ObservableList<Customers> custList = FXCollections.observableArrayList();

			while (rs.next()) {
				Customers cust = new Customers();

				cust.setCustId(rs.getInt(1));
				cust.setCustName(rs.getString(2));
				cust.setAddress(rs.getString(3));
				cust.setPostalCode(rs.getString(4));
				cust.setPhone(rs.getString(5));
				cust.setCreatedDate(rs.getTimestamp(6).toLocalDateTime());
				cust.setCreatedBy(rs.getString(7));
				cust.setLastUpdtTs(rs.getTimestamp(8).toLocalDateTime());
				cust.setLastUpdtUser(rs.getString(9));
				cust.setDivisionId(rs.getInt(10));
				custList.addAll(cust);
			}

			return custList;
		}
	}

	/**
	 * Delete customer method. Takes in a Customer Object.
	 * Gets the appointments to check if the customer has an appointment. If so, cancels the appointment first,
	 * then deletes the cust.
	 *
	 * @param cust
	 * @throws SQLException
	 */
	public static void deleteCustomer(Customers cust) throws SQLException {

		ObservableList<Appointments> appList = SQLAppointmentDAO.getAppointments();
		appList.forEach(app -> {
			if (cust.getCustId() == app.getCustomerID()) {
				try {
					SQLAppointmentDAO.cancelAppointment(app);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		DeleteCustomer dc = new DeleteCustomer();
		dc.execute(cust);
	}

	/**
	 * Class to delete a customer row from the sql table
	 */
	private static class DeleteCustomer {

		public void execute(Customers cust) throws SQLException {
			String sql = "DELETE from customers where Customer_ID = " + cust.getCustId();
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ps.executeUpdate();
		}
	}

	/**
	 * Method to add a new customer to the sql table
	 * @param cust
	 * @return
	 * @throws SQLException
	 */
	public static int addNewCustomer(Customers cust) throws SQLException {
		AddCustomer ac = new AddCustomer();
		return ac.execute(cust);
	}

	/**
	 * Class to add a customer row to the sql table
	 */
	private static class AddCustomer {
		/**
		 * Execute method that creates the sql query and executes it.
		 *
		 * @param cust
		 * @return
		 * @throws SQLException
		 */
		public int execute(Customers cust) throws SQLException {
			String sql = "INSERT into customers " +
					"(Customer_ID, " +
					"Customer_Name, " +
					"Address, " +
					"Postal_Code, " +
					"Phone, " +
					"Create_Date, " +
					"Created_By, " +
					"Last_Update, " +
					"Last_Updated_By, " +
					"Division_ID) " +
					"VALUES (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ps.setInt(1, cust.getCustId());
			ps.setString(2, cust.getCustName());
			ps.setString(3, cust.getAddress());
			ps.setString(4, cust.getPostalCode());
			ps.setString(5, cust.getPhone());
			ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(7, cust.getCreatedBy());
			ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(9, cust.getLastUpdtUser());
			ps.setInt(10, cust.getDivisionId());


			return ps.executeUpdate();
		}
	}

	/**
	 * Method used to update a customer row in the sql table.
	 * Instantiates the UpdateCustomer class to call the execute method.
	 * @param cust
	 * @return Integer
	 * @throws SQLException
	 */
	public static int updateCust(Customers cust) throws SQLException {
		UpdateCustomer uc = new UpdateCustomer();
		return uc.execute(cust);
	}

	/**
	 * Class to update a customer row in the sql table
	 */
	private static class UpdateCustomer {
		/**
		 * Execute method creates the sql query and executes it in the database
		 * @param cust
		 * @return Integer
		 * @throws SQLException
		 */
		public int execute(Customers cust) throws SQLException {
			String sql = "UPDATE customers " +
					"SET Customer_Name = ?, " +
					"Address = ?, " +
					"Postal_Code = ?," +
					"Phone = ?, " +
					"Last_Update = ?, " +
					"Last_Updated_By = ?, " +
					"Division_ID = ? " +

					"WHERE Customer_ID = " + cust.getCustId();

			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ps.setString(1, cust.getCustName());
			ps.setString(2, cust.getAddress());
			ps.setString(3, cust.getPostalCode());
			ps.setString(4, cust.getPhone());
			ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(6, cust.getLastUpdtUser());
			ps.setInt(7, cust.getDivisionId());

			ps.executeUpdate();
			return cust.getCustId();
		}
	}

	/**
	 * Method used to get the customer id from the customer name.
	 * Utilizes a lambda expression to loop through the List to match the customer name to the parameter string
	 * @param name
	 * @return Integer
	 * @throws SQLException
	 */
	public static int getCustIdFromName(String name) throws SQLException {
		ObservableList<Customers> custList = getAllCusts();
		AtomicInteger id = new AtomicInteger();
		if(name != null){
			custList.forEach(cust -> {
				if (name.equals(cust.getCustName())) {
					id.set(cust.getCustId());
				}
			});

		}
			return id.get();
	}





}
