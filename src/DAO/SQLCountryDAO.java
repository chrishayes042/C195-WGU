package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import service.CountryService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO Class for the Country Only class that will touch the DB.
 */
public class SQLCountryDAO implements CountryService {
	/**
	 * Method that is called to get all of the countries from the sql table
	 * Instantiates the FindAllCountries class and calls the execute method which returns a list
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Countries> getAllCountries() throws SQLException{
		FindAllCountries fac = new FindAllCountries();
		return fac.execute();
	}

	/**
	 * Class to find all countries in the sql table
	 */
	private static class FindAllCountries{
		/**
		 * The execute method creates the sql string and passes that to the preparedstatement to execute the query.
		 * Creates a new List of countries
		 * Creates a new Countries object to be passed into the list
		 * @return ObservableList
		 * @throws SQLException
		 */
		public ObservableList<Countries> execute()throws SQLException {
			ObservableList<Countries> newList = FXCollections.observableArrayList();
			String sql = "SELECT * from countries";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()){
				Countries countries = new Countries();
				countries.setCountryId(rs.getInt(1));
				countries.setCountry(rs.getString(2));
				countries.setCreatedDate(rs.getTimestamp(3).toLocalDateTime());
				countries.setCreatedBy(rs.getString(4));
				countries.setLastUpDtTs(rs.getTimestamp(5).toLocalDateTime());
				countries.setLastUpDtUser(rs.getString(6));
				newList.addAll(countries);
			}
			return newList;
		}
	}
	/**
	 * Method that is called to get the country name from the sql table
	 * Instantiates the FindCountryById class and calls the execute method which returns a string
	 * @return string
	 * @throws SQLException
	 */
	public static String getCountryByID(int id)throws SQLException{
		FindCountryById fc = new FindCountryById();
		return fc.execute(id);
	}

	/**
	 * Class to find the country by id
	 */
	private static class FindCountryById{
		/**
		 * The execute method creates the sql string and passes that to the preparedstatement to execute the query.
		 * Creates a new String
		 * Sets string to the column that the query called
		 * @return String
		 * @throws SQLException
		 */
		public String execute(int id) throws SQLException{
			String sql = "SELECT country from countries WHERE Country_ID = " + id;
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String country = " ";
			while (rs.next()){
				country = rs.getString(1);
			}
			return country;
		}
	}


}
