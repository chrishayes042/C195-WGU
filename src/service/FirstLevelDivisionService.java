package service;

import DAO.SQLFirstLevelDivisionDAO;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;

import java.sql.SQLException;

public interface FirstLevelDivisionService {
	/**
	 * Method used to get a list of all the rows from the sql table
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<FirstLevelDivision> getAllDivisions() throws SQLException{
		return SQLFirstLevelDivisionDAO.getAllDivisions();
	}

	/**
	 * Method used to get a list of divisions where the country ID = c
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<FirstLevelDivision> getFldDivisionById(int c) throws SQLException{
		return SQLFirstLevelDivisionDAO.getFldDivisionById(c);
	}

	/**
	 * Method used to create a division object from the sql table using the id or name
	 * @param id
	 * @param div
	 * @param isDivisionName
	 * @return
	 * @throws SQLException
	 */
	public static FirstLevelDivision getSingleDivision(int id, String div, boolean isDivisionName) throws SQLException{
		return SQLFirstLevelDivisionDAO.getSingleDivision(id, div, isDivisionName);
	}

}
