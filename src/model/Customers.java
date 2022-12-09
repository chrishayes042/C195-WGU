package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Customers {
    private int custId;
    private String custName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createdDate;
    private String createdBy;
    private Timestamp lastUpdtTs;
    private String lastUpdtUser;
    private int divisionId;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Timestamp getLastUpdtTs() {
        return lastUpdtTs;
    }

    public void setLastUpdtTs(Timestamp lastUpdtTs) {
        this.lastUpdtTs = lastUpdtTs;
    }

    public String getLastUpdtUser() {
        return lastUpdtUser;
    }

    public void setLastUpdtUser(String lastUpdtUser) {
        this.lastUpdtUser = lastUpdtUser;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
