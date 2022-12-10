package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class appointmentDAO {

    public static ObservableList<Appointments> getAppointments() throws SQLException {
        FindAllAppointments apps = new FindAllAppointments();
        return apps.execute();
    }

    private static class FindAllAppointments{

        public ObservableList<Appointments> execute() throws SQLException{
            String sql = "SELECT * from Appointments";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Appointments app = new Appointments();
                app.setAppointmentID(resultSet.getInt(1));
                app.setAppointmentTitle(resultSet.getString(2));
                app.setAppointmentDescription(resultSet.getString(3));
                app.setAppointmentLocation(resultSet.getString(4));
                app.setAppointmentType(resultSet.getString(5));
                app.setStart(resultSet.getTimestamp(6).toLocalDateTime());
                app.setEnd(resultSet.getTimestamp(7).toLocalDateTime());
                app.setCreatedDate(resultSet.getTimestamp(8).toLocalDateTime());
                app.setCreatedBy(resultSet.getString(9));
                app.setLastUpDt(resultSet.getTimestamp(10).toLocalDateTime());
                app.setLastUpdtUser(resultSet.getString(11));
                app.setCustomerID(resultSet.getInt(12));
                app.setUserID(resultSet.getInt(13));
                app.setContactID(resultSet.getInt(14));
                appointmentsList.addAll(app);
            }
            return appointmentsList;
        }
    }

    public static int updateAppointment(Appointments app) throws SQLException{
        UpdateAppointment update = new UpdateAppointment();
        return update.execute(app);
    }
    private static class UpdateAppointment{

        public int execute(Appointments app) throws SQLException{
            String sql = "UPDATE appointments " +
                    "SET Title = ?, " +
                    "Description = ?, " +
                    "Location = ?," +
                    "Type = ?, " +
                    "Start = ?, " +
                    "End = ?, " +
                    "Create_Date = ?, " +
                    "Created_By = ?, " +
                    "Last_Update = ?," +
                    " Last_Updated_By = ?," +
                    "Customer_ID = ?," +
                    "User_ID = ?, " +
                    "Contact_ID = ? " +
                    "WHERE appointment_ID = " + app.getAppointmentID() ;

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, app.getAppointmentTitle());
            ps.setString(2, app.getAppointmentDescription());
            ps.setString(3, app.getAppointmentLocation());
            ps.setString(4, app.getAppointmentType());
            ps.setTimestamp(5, Timestamp.valueOf(app.getStart()));
            ps.setTimestamp(6, Timestamp.valueOf(app.getEnd()));
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, app.getCreatedBy());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, app.getLastUpdtUser());
            ps.setInt(11, app.getCustomerID());
            ps.setInt(12, app.getUserID());
            ps.setInt(13, app.getContactID());

            ps.executeUpdate();
            return app.getAppointmentID();
        }
    }

    public static int addNewAppointment(Appointments app) throws SQLException {
        AddAppointment add = new AddAppointment();
        return add.execute(app);
    }
    private static class AddAppointment{

        public int execute(Appointments app) throws SQLException{
            String sql = "INSERT INTO Appointments " +
                    "(Appointment_ID, " +
                    "Title, " +
                    "Description, " +
                    "Location, " +
                    "Type, " +
                    "Start, " +
                    "End, " +
                    "Create_Date, " +
                    "Created_By, " +
                    "Last_Update, " +
                    "Last_Updated_By, " +
                    "Customer_ID, " +
                    "User_ID, " +
                    "Contact_ID)" +

                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                ps.setInt(1, app.getAppointmentID());
                ps.setString(2, app.getAppointmentTitle());
                ps.setString(3, app.getAppointmentDescription());
                ps.setString(4, app.getAppointmentLocation());
                ps.setString(5, app.getAppointmentType());
                ps.setTimestamp(6, Timestamp.valueOf(app.getStart()));
                ps.setTimestamp(7, Timestamp.valueOf(app.getEnd()));
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, app.getCreatedBy());
                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(11, app.getLastUpdtUser());
                ps.setInt(12, app.getCustomerID());
                ps.setInt(13, app.getUserID());
                ps.setInt(14, app.getContactID());

               return ps.executeUpdate();
        }
    }

    public static void cancelAppointment(Appointments app) throws SQLException{
        CancelAppointment ca = new CancelAppointment();
        ca.execute(app);
    }
    private static class CancelAppointment{

        public void execute(Appointments app) throws SQLException{
            String sql = "DELETE from appointments where Appointment_ID = " + app.getAppointmentID();
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        }
    }
    public static int checkAppointmentOverLap(Appointments app, boolean isUpdate) throws SQLException {
        ObservableList<Appointments> list = getAppointments();
        AtomicInteger check = new AtomicInteger(-1);
        list.forEach(appointments -> {

            if(!isUpdate){
                if(app.getStart().equals(appointments.getStart())){
                    check.set(appointments.getAppointmentID());
                } else if(app.getAppointmentID() == appointments.getAppointmentID()){
                    check.set(appointments.getAppointmentID());
                }
            } else {
                if(app.getAppointmentID() != appointments.getAppointmentID() && app.getStart().equals(appointments.getStart())){
                    check.set(appointments.getAppointmentID());
                }
            }

        });
        return check.get();
    }

}




