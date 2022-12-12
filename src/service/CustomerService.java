package service;

import DAO.SQLCustomerDAO;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.SQLException;

public interface CustomerService {
	/**
	 * Method used to get a list of all the customers from the sql table
	 * @return
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
	 * @return
	 * @throws SQLException
	 */
	public static int addNewCustomer(Customers cust) throws SQLException{
		return SQLCustomerDAO.addNewCustomer(cust);
	}

	/**
	 * Method used to update a row in the sql table
	 * @param cust
	 * @return
	 * @throws SQLException
	 */
	public static int updateCust(Customers cust) throws SQLException{
		return SQLCustomerDAO.updateCust(cust);
	}

	/**
	 * Method used to parse the Customer ID from the Customer Name
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public static int getCustIdFromName(String name) throws SQLException{
		return SQLCustomerDAO.getCustIdFromName(name);
	}


}
