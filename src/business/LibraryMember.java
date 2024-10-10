package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

final public class LibraryMember extends Person implements Serializable {
    private String memberId;
    private List<CheckoutRecord> checkoutRecords;  // New field to store checkout records
    
    public LibraryMember(String memberId, String fname, String lname, String tel, Address add) {
        super(fname, lname, tel, add);
        this.memberId = memberId;
        this.checkoutRecords = new ArrayList<>();  // Initialize the list of checkout records
    }

    public String getMemberId() {
        return memberId;
    }

    // Method to add a checkout record
    public void addCheckoutRecord(CheckoutRecord record) {
        checkoutRecords.add(record);
    }

    // Method to get the most recent checkout record
    public CheckoutRecord getCheckoutRecord() {
        if (!checkoutRecords.isEmpty()) {
            return checkoutRecords.get(checkoutRecords.size() - 1);  // Return the most recent checkout
        }
        return null;
    }

    // Method to get all checkout records
    public List<CheckoutRecord> getCheckoutRecords() {
        return checkoutRecords;  // Return the full list of checkout records
    }

    @Override
    public String toString() {
        return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
                ", " + getTelephone() + " " + getAddress();
    }

    private static final long serialVersionUID = -2226197306790714013L;
}
