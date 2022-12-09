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



	public static ObservableList<Contacts> getAllContacts() throws SQLException {
		FindAllContacts fac = new FindAllContacts();
		return fac.execute();
	}

	private static class FindAllContacts{

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
				contactsList.add(contact);
			}
			return contactsList;
		}
	}

	public static int findContactId(String name) throws SQLException {
		ObservableList<Contacts> list = getAllContacts();
		AtomicInteger cId = new AtomicInteger(-1);
		list.forEach(contacts -> {
			if (name.equals(contacts.getContactName())) {
				cId.set(contacts.getContactId());
			}});

		return cId.get();
	}
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
