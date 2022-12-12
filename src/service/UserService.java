package service;

import DAO.SQLUserDAO;
import javafx.collections.ObservableList;
import model.Users;

import java.sql.SQLException;

public interface UserService {
	/**
	 * Method used to validate the user credentials from the sql table
	 * @param userName
	 * @param pass
	 * @return
	 * @throws SQLException
	 */
	public static int validateUsers(String userName, String pass) throws SQLException {
		return SQLUserDAO.validateUsers(userName, pass);
	}

	/**
	 * Method used to get a list of all rows in SQL table
	 * @return
	 * @throws SQLException
	 */
	public static ObservableList<Users> getAllUsers() throws SQLException{
		return SQLUserDAO.getAllUsers();
	}

	/**
	 * Method used to return the Id from a row in the sql table where the UserName = name
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public static int findUserIdFromName(String name) throws SQLException{
		return SQLUserDAO.findUserIdFromName(name);
	}

	/**
	 * Method used to return the name from a row in the sql table where the User id = id
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static String findUserNameFromId(int id) throws SQLException{
		return SQLUserDAO.findUserNameFromId(id);
	}


}
