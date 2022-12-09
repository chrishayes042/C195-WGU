package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Users {

    private int userID;
    private String userName;
    private String passWord;
    private LocalDateTime createdDate;
    private String createdBy;
    private Timestamp lastUpDtTs;
    private String lastUpDtUser;


    public int getUserID(){
        return userID;
    }
    public void setUserID(int userId){
        this.userID = userId;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getPassWord(){
        return passWord;
    }
    public void setPassWord(String pass){
        this.passWord = pass;
    }
    public LocalDateTime getCreatedDate(){
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime cd){
        this.createdDate = cd;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    public Timestamp getLastUpDtTs(){
        return lastUpDtTs;
    }
    public void setLastUpDtTs(Timestamp ts){
        this.lastUpDtTs = ts;
    }
    public String getLastUpDtUser(){
        return lastUpDtUser;
    }
    public void setLastUpDtUser(String lu){
        this.lastUpDtUser = lu;
    }

}
