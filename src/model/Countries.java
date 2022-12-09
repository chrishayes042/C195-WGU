package model;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class Countries {
    private int countryId;
    private String country;
    private LocalDateTime createdDate;
    private String createdBy;
    private Timestamp lastUpDtTs;
    private String lastUpDtUser;

    public int getCountryId(){
        return countryId;
    }
    public void setCountryId(int ci){
        this.countryId = ci;
    }
    public String getCountry(){
        return country;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public LocalDateTime getCreatedDate(){
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime cd){
        this.createdDate = cd;
    }
    public String getCreatedBy (){
        return createdBy;
    }
    public void setCreatedBy(String cb){
        this.createdBy = cb;
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
