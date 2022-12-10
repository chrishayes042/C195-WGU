package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class userDAO {

    public int validateUsers(String userName, String pass){
        ValidateUsers vu = new ValidateUsers();
        return vu.execute(userName, pass);
    }

    private class ValidateUsers{

        public int execute(String userName, String pass){
            try {
                String sql = "SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + pass + "'";
                PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                if (rs.getString(2).equals(userName) && rs.getString(3).equals(pass)) {
                    // returns the userId
                    return rs.getInt(1);
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return -1;

        }
    }


    public static ObservableList<Users> getAllUsers() throws SQLException{
        FindAllUsers fau = new FindAllUsers();
        return fau.execute();
    }

    private static class FindAllUsers{

        public ObservableList<Users> execute() throws SQLException{
            String sql = "SELECT * from users";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            ObservableList<Users> userList = FXCollections.observableArrayList();
            while (rs.next()){
                Users users = new Users();
                users.setUserID(rs.getInt(1));
                users.setUserName(rs.getString(2));
                users.setPassWord(rs.getString(3));
                users.setCreatedDate(rs.getTimestamp(4).toLocalDateTime());
                users.setCreatedBy(rs.getString(5));
                users.setLastUpDtTs(rs.getTimestamp(6));
                users.setCreatedBy(rs.getString(7));
                userList.addAll(users);
            }
            return userList;
        }
    }

    public static int findUserIdFromName(String name) throws SQLException {
        ObservableList<Users> list = getAllUsers();
        AtomicInteger userId = new AtomicInteger(-1);
        list.forEach(user -> {
            if(name.equals(user.getUserName())){
                userId.set(user.getUserID());
            }
        });
        return userId.get();
    }
    public static String findUserNameFromId(int id) throws SQLException{
        ObservableList<Users> list = getAllUsers();
        AtomicReference<String> name = new AtomicReference<>("");
        list.forEach(user -> {
            if(id == user.getUserID()){
                name.set(user.getUserName());
        }});
        return name.get();
    }

}
