package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class contactDAO {


	/**
	 * Method that is called to get all of the contacts from the sql table
	 * Instantiates the FindAllContacts class and calls the execute method which returns a list
	 * @return ObservableList
	 * @throws SQLException
	 */
	public static ObservableList<Contacts> getAllContacts() throws SQLException {
		FindAllContacts fac = new FindAllContacts();
		return fac.execute();
	}

	private static class FindAllContacts{
		/**
		 * The execute method creates the sql string and passes that to the preparedstatement to execute the query.
		 * Creates a new List of Contacts
		 * Creates a now Contacts object to be passed into the list
		 * @return ObservableList
		 * @throws SQLException
		 */
		public ObservableList<Contacts> execute() throws SQLException {
			String sql = "SELECT * from contacts";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ObservableList<Contacts> contactsList = FXCollections.observableArrayList();

			while (rs.next()){
				Contacts contact = new Contacts();
				contact.setContactId(rs.getInt(1));
				contact.setContactName(rs.getString(2));
				contact.setEmail(rs.getString(3));
				contactsList.addAll(contact);
			}
			return contactsList;
		}
	}

	/**
	 * This method gets the contact id by getting a list of all the contacts from the getAllContacts method.
	 * Uses a lambda expression to loop through that list and if the name of the param = the name on the list, it returns that id.
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public static int findContactId(String name) throws SQLException {
		ObservableList<Contacts> list = getAllContacts();
		AtomicInteger cId = new AtomicInteger(-1);
		list.forEach(contacts -> {
			if (name.equals(contacts.getContactName())) {
				cId.set(contacts.getContactId());
			}});

		return cId.get();
	}

	/**
	 * This method finds the contact name using the id. Gets a list of all the contacts from the getAllContacts method.
	 * Uses a lambda expression to loop through that list and if the param id = the id on the list, it returns that name.
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static String findContactName(int id) throws SQLException{
		ObservableList<Contacts> list = getAllContacts();
		AtomicReference<String> name = new AtomicReference<>(" ");
		list.forEach(contacts -> {
			if(id == contacts.getContactId()){
				name.set(contacts.getContactName());
			}
		});
		return name.get();
	}


}
