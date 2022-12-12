package service;

import DAO.SQLCountryDAO;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.SQLException;

public interface CountryService {

	/**
	 * Method used to get a list of all the sql table rows
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<Countries> getAllCountries() throws SQLException{
		return SQLCountryDAO.getAllCountries();
	}

	/**
	 * Method used to get the country name from the sql table where the country_id = id
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static String getCountryByID(int id) throws SQLException{
		return SQLCountryDAO.getCountryByID(id);
	}


}
