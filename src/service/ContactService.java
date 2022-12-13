package service;

import DAO.SQLContactDAO;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.SQLException;
/**
 * Service class to the SQLContactDAO class
 * I added a service layer to make the code more secure as it does not touch the database and easier to read and follow the methods.
 * If the application were more complex, one would add the business logic in the methods.
 * Each method in the DAO class would be added here to be called throughout the application
 */
public interface ContactService {
	/**
	 * Method used to get a list of all the contacts in the sql table
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<Contacts> getAllContacts() throws SQLException{
		return SQLContactDAO.getAllContacts();
	}

	/**
	 * Method used to find the ContactID from the sql table where the contact name = name
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public static int findContactId(String name) throws SQLException{
		return SQLContactDAO.findContactId(name);
	}

	/**
	 * Method used to find the contact name from the sql table where the contact id = id
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static String findContactName(int id) throws SQLException{
		return SQLContactDAO.findContactName(id);
	}

}
