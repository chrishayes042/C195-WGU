package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;
import service.FirstLevelDivisionService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLFirstLevelDivisionDAO implements FirstLevelDivisionService {

	/**
	 * Method that is called to call the execute method
	 * This method gets a list of FirstLevelDivision objects from the execute method
	 * @return ObeservableList
	 * @throws SQLException
	 */
	public static ObservableList<FirstLevelDivision> getAllDivisions() throws SQLException{
		FindAllDivisions fad = new FindAllDivisions();
		return fad.execute();
	}

	/**
	 * Class that holds the execute method
	 */
	private static class FindAllDivisions{

		/**
		 * Execute method that creates the sql query and executes it.
		 * Sets a new FirstLevelDivision object and puts it in a list to be returned
		 * @return ObeservableList<FirstLevelDivision>
		 * @throws SQLException
		 */
		public ObservableList<FirstLevelDivision> execute() throws SQLException{
			ObservableList<FirstLevelDivision> fldList = FXCollections.observableArrayList();
			String sql = "SELECT * from first_level_divisions";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				FirstLevelDivision fld = new FirstLevelDivision();
				fld.setDivisionId(rs.getInt(1));
				fld.setDivision(rs.getString(2));
				fld.setCreatedDate(rs.getTimestamp(3).toLocalDateTime());
				fld.setCreatedBy(rs.getString(4));
				fld.setLastUpDtTs(rs.getTimestamp(5).toLocalDateTime());
				fld.setLastUpdtUser(rs.getString(6));
				fld.setCountryId(rs.getInt(7));

				fldList.addAll(fld);
			}
			return fldList;
		}
	}

	/**
	 * Gets the FirstLevelDivision Division ID using a country_id that is passed in when called.
	 * Calls the execute method in the FindByName class.
	 *
	 * @param c
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<FirstLevelDivision> getFldDivisionById(int c) throws SQLException{
		FindIdByName fidbn = new FindIdByName();
		return fidbn.execute(c);
	}
	private static class FindIdByName{
		/**
		 * Execute method that creates the sql query and executes it.
		 * Sets a new FirstLevelDivision object and puts it in a list to be returned
		 * @param c
		 * @return
		 * @throws SQLException
		 */
		public ObservableList<FirstLevelDivision> execute(int c) throws SQLException {
			String sql = "SELECT * from first_level_divisions WHERE Country_ID = " + c;
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ObservableList<FirstLevelDivision> list = FXCollections.observableArrayList();
			while (rs.next()){
				FirstLevelDivision fld = new FirstLevelDivision();
				fld.setDivisionId(rs.getInt(1));
				fld.setDivision(rs.getString(2));
				list.addAll(fld);
			}
			return list;
		}
	}

	/**
	 * Another get division by id, but this one doesn't return a list just 1 FirstLevelDivision object.
	 * Instantiates the FindDivisionById class and calls the execute method that returns a new object.
	 * @param id
	 * @param div
	 * @param isDivisionName
	 * @return FirstLevelDivision object
	 * @throws SQLException
	 */
	public static FirstLevelDivision getSingleDivision(int id, String div, boolean isDivisionName) throws SQLException{
		FindDivisionById fd = new FindDivisionById();
		return fd.execute(id, div, isDivisionName);
	}
	private static class FindDivisionById{
		/**
		 * Execute method that creates the sql query and executes it.
		 * Sets a new FirstLevelDivision object and returns it
		 * @param id
		 * @param div
		 * @param isDivisionName
		 * @return
		 * @throws SQLException
		 */
		public FirstLevelDivision execute(int id, String div, boolean isDivisionName)throws SQLException{
			String sql = "";
			if(!isDivisionName){
				sql = "SELECT * from first_level_divisions WHERE Division_ID = " + id;
			} else {
				sql = "SELECT * from first_level_divisions WHERE Division = " + "'" + div + "'";
			}
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			FirstLevelDivision fld = new FirstLevelDivision();
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				fld.setDivisionId(rs.getInt(1));
				fld.setDivision(rs.getString(2));
				fld.setCountryId(rs.getInt(7));

			}
			return fld;
		}
	}

}
