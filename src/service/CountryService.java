package service;

import DAO.SQLCountryDAO;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.SQLException;
/**
 * Service class to the SQLCountryDAO class
 * I added a service layer to make the code more secure as it does not touch the database and easier to read and follow the methods.
 * If the application were more complex, one would add the business logic in the methods.
 * Each method in the DAO class would be added here to be called throughout the application
 */
public interface CountryService {

	/**
	 * Method used to get a list of all the sql table rows
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Countries> getAllCountries() throws SQLException{
		return SQLCountryDAO.getAllCountries();
	}

	/**
	 * Method used to get the country name from the sql table where the country_id = id
	 * @param id
	 * @return String
	 * @throws SQLException
	 */
	public static String getCountryByID(int id) throws SQLException{
		return SQLCountryDAO.getCountryByID(id);
	}


}
