package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;
import service.UserService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SQLUserDAO implements UserService {
	/**
	 * Method to validate users credentials.
	 * Instantiates the ValidateUsers class to call the execute method.
	 * @param userName
	 * @param pass
	 * @return Integer
	 * @throws SQLException
	 */
	public static int validateUsers(String userName, String pass) throws SQLException{
		ValidateUsers vu = new ValidateUsers();
		return vu.execute(userName, pass);
	}

	private static class ValidateUsers {
		/**
		 * Method to create the sql query and pass it into the database.
		 * @param userName
		 * @param pass
		 * @return Integer
		 * @throws SQLException
		 */
		public int execute(String userName, String pass) throws SQLException{
			try {
				String sql = "SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + pass + "'";
				PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				rs.next();
				if (rs.getString(2).equals(userName) && rs.getString(3).equals(pass)) {
					// returns the userId
					return rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return -1;

		}
	}

	/**
	 * Method used to instatiate the FindAllUsers class to call the execute method.
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Users> getAllUsers() throws SQLException {
		FindAllUsers fau = new FindAllUsers();
		return fau.execute();
	}

	private static class FindAllUsers {
		/**
		 * Method to create and execute the sql query into the database.
		 * Creates a new Users object and passes it into a new List to return.
		 * @return Observable List
		 * @throws SQLException
		 */
		public ObservableList<Users> execute() throws SQLException {
			String sql = "SELECT * from users";
			PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			ObservableList<Users> userList = FXCollections.observableArrayList();
			while (rs.next()) {
				Users users = new Users();
				users.setUserID(rs.getInt(1));
				users.setUserName(rs.getString(2));
				users.setPassWord(rs.getString(3));
				users.setCreatedDate(rs.getTimestamp(4).toLocalDateTime());
				users.setCreatedBy(rs.getString(5));
				users.setLastUpDtTs(rs.getTimestamp(6).toLocalDateTime());
				users.setCreatedBy(rs.getString(7));
				userList.addAll(users);
			}
			return userList;
		}
	}

	/**
	 * Method used to get the user Id by looping through a list of all the users.
	 * Uses a lambda expression to loop through the list of users to match the Parameter String. Returns the UserID of the match.
	 * @param name
	 * @return Integer
	 * @throws SQLException
	 */
	public static int findUserIdFromName(String name) throws SQLException {
		ObservableList<Users> list = getAllUsers();
		AtomicInteger userId = new AtomicInteger(-1);
		list.forEach(user -> {
			if (name.equals(user.getUserName())) {
				userId.set(user.getUserID());
			}
		});
		return userId.get();
	}

	/**
	 * Method used to get the user's name by looping through a list of all the users.
	 * Uses a lambda expression to loop through the list of users to match the Parameter Integer. Returns the user's name of the match.
	 * @param id
	 * @return String
	 * @throws SQLException
	 */
	public static String findUserNameFromId(int id) throws SQLException {
		ObservableList<Users> list = getAllUsers();
		AtomicReference<String> name = new AtomicReference<>("");
		list.forEach(user -> {
			if (id == user.getUserID()) {
				name.set(user.getUserName());
			}
		});
		return name.get();
	}

}
