package service;

import DAO.SQLCustomerDAO;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service class to the SQLCustomerDAO class
 * I added a service layer to make the code more secure as it does not touch the database and easier to read and follow the methods.
 * If the application were more complex, one would add the business logic in the methods.
 * Each method in the DAO class would be added here to be called throughout the application
 */
public interface CustomerService {
	/**
	 * Method used to get a list of all the customers from the sql table
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Customers> getAllCusts() throws SQLException{
		return SQLCustomerDAO.getAllCusts();
	}

	/**
	 * Method used to delete a customer row on the sql table
	 * @param cust
	 * @throws SQLException
	 */
	public static void deleteCustomer(Customers cust) throws SQLException{
		SQLCustomerDAO.deleteCustomer(cust);
	}

	/**
	 * Method used to create a new row on the sql table
	 * @param cust
	 * @return Integer
	 * @throws SQLException
	 */
	public static int addNewCustomer(Customers cust) throws SQLException{
		return SQLCustomerDAO.addNewCustomer(cust);
	}

	/**
	 * Method used to update a row in the sql table
	 * @param cust
	 * @return Integer
	 * @throws SQLException
	 */
	public static int updateCust(Customers cust) throws SQLException{
		return SQLCustomerDAO.updateCust(cust);
	}

	/**
	 * Method used to search for the customer name from the ID.
	 * Used a lambda expression to loop through the customer list to match the param id to the customer id to
	 * extract the name from that customer.
	 * @param name
	 * @return Integer
	 * @throws SQLException
	 */
	public static int getCustIdFromName(String name) throws SQLException{
		return SQLCustomerDAO.getCustIdFromName(name);
	}
	public static String getCustNameFromId(int id) throws SQLException{
		ObservableList<Customers> custList = SQLCustomerDAO.getAllCusts();
		AtomicReference<String> name = new AtomicReference<>("");
		custList.forEach(cust -> {
			if(id == cust.getCustId()){
				name.set(cust.getCustName());
			}
		});
		return name.get();
	}

}
