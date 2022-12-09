package model;

public class Contacts {
    private int contactId;
    private String contactName;
    private String email;

    public int getContactId(){
        return contactId;
    }
    public void setContactId(int ci){
        this.contactId = ci;
    }
    public String getContactName(){
        return contactName;
    }
    public void setContactName(String cn){
        this.contactName = cn;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
}
