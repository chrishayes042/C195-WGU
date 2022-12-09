package model;

import java.time.LocalDateTime;

public class Appointments {

    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpDt;
    private String lastUpdtUser;

    public int getAppointmentID(){
        return appointmentID;

    }
    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }

    public String getAppointmentTitle(){
        return appointmentTitle;
    }
    public void setAppointmentTitle(String appointmentTitle){
        this.appointmentTitle = appointmentTitle;
    }
    public String getAppointmentDescription(){
        return appointmentDescription;
    }
    public void setAppointmentDescription(String appointmentDescription){
        this.appointmentDescription = appointmentDescription;
    }
    public String getAppointmentLocation(){
        return appointmentLocation;
    }
    public void setAppointmentLocation(String appointmentLocation){
        this.appointmentLocation = appointmentLocation;
    }
    public String getAppointmentType(){
        return appointmentType;
    }
    public void setAppointmentType(String appointmentType){
        this.appointmentType = appointmentType;
    }
    public LocalDateTime getStart(){
        return start;
    }
    public void setStart(LocalDateTime start){
        this.start = start;
    }
    public LocalDateTime getEnd(){
        return end;
    }
    public void setEnd(LocalDateTime end){
        this.end = end;
    }
    public int getCustomerID(){
        return customerID;
    }
    public void setCustomerID(int customerID){
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getContactID(){
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
    public LocalDateTime getCreatedDate(){
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    public String getLastUpdtUser(){
        return lastUpdtUser;
    }
    public void setLastUpdtUser(String lastUpdtUser){
        this.lastUpdtUser = lastUpdtUser;
    }
    public LocalDateTime getLastUpDt(){
        return lastUpDt;
    }
    public void setLastUpDt(LocalDateTime ts){
        this.lastUpDt = ts;
    }
}
