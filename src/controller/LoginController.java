package controller;

import DAO.appointmentDAO;
import DAO.userDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML private Label userLocationLabel1;
    @FXML private Label userNameLabel;
    @FXML private Label userPassLabel;
    @FXML private Button signInButton;
    @FXML private TextField userNameText;
    @FXML private TextField pass;
    ResourceBundle resourceBundle;
    userDAO ud = new userDAO();
    private int userId;
    private Locale locale = Locale.getDefault();
    private LocalDateTime currentPlus15 = LocalDateTime.now().plusMinutes(15);
    private LocalDateTime currentMin15 = LocalDateTime.now().minusMinutes(15);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale.setDefault(locale);
       resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());

       userLocationLabel1.setText(resourceBundle.getString("country"));
       userNameLabel.setText(resourceBundle.getString("userNameLabel"));
       userPassLabel.setText(resourceBundle.getString("passwordLabel"));
       signInButton.setText(resourceBundle.getString("signInButton"));
 }
   @FXML
   public void validateUsers(ActionEvent event) throws IOException, SQLException {

           userId = ud.validateUsers(userNameText.getText().trim(), pass.getText().trim());
           boolean userValidate = validateUser(userId);
           Locale.setDefault(locale);
           resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());


           if(!userValidate){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle(resourceBundle.getString("alert"));
               alert.setHeaderText(resourceBundle.getString("alertHeader"));
               alert.setContentText(resourceBundle.getString("alertContent"));
               alert.showAndWait().ifPresent((response -> {
                   System.out.println(resourceBundle.getString("alert"));
                   Parent main = null;
                   if(response == ButtonType.OK){

                   }
               }));
           } else {
               try {

                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/main-view.fxml"));
                   Parent root = (Parent) fxmlLoader.load();
                   Stage stage = new Stage();
                   stage.setTitle("Main");
                   stage.setScene(new Scene(root));
                   stage.show();

               } catch (IOException e){
                   e.printStackTrace();
               }
               boolean hasApp = userHasAppointment(userId, currentPlus15, currentMin15);
               if(hasApp){
                   Alert alertAppoint = new Alert(Alert.AlertType.CONFIRMATION);
                   Locale.setDefault(locale);
                   resourceBundle = ResourceBundle.getBundle("location", Locale.getDefault());
                   alertAppoint.setTitle(resourceBundle.getString("alertApp"));
                   alertAppoint.setHeaderText(resourceBundle.getString("alertAppHeader"));
                   alertAppoint.setContentText(resourceBundle.getString("alertAppContent"));
               }


           }

   }
   private boolean validateUser(int userId){
       userId = ud.validateUsers(userNameText.getText().trim(), pass.getText().trim());
       boolean user = false;
       user = userId > 0;
       return user;
   }

   private void nextPage() throws IOException{

       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("main-view.fxml"));
       Parent root = loader.load();
       Stage stage = (Stage) signInButton.getScene().getWindow();
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.show();

   }
   private boolean userHasAppointment(int userId, LocalDateTime start, LocalDateTime end) throws SQLException, IOException {
       ObservableList<Appointments> appList = appointmentDAO.getAppointments();

       boolean hasApp = false;
       for(Appointments app : appList){
           hasApp = (app.getStart() == start || app.getEnd() == end) && app.getUserID() == userId;

       }


       return hasApp;
   }





}