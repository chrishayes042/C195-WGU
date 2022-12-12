package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FirstLevelDivision {
    private int divisionId;
    private String division;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpDtTs;
    private String lastUpdtUser;
    private int countryId;

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpDtTs() {
        return lastUpDtTs;
    }

    public void setLastUpDtTs(LocalDateTime lastUpDtTs) {
        this.lastUpDtTs = lastUpDtTs;
    }

    public String getLastUpdtUser() {
        return lastUpdtUser;
    }

    public void setLastUpdtUser(String lastUpdtUser) {
        this.lastUpdtUser = lastUpdtUser;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
