package business;

import java.io.Serializable;
import java.time.LocalDate;  // Import LocalDate
import java.util.ArrayList;
import java.util.List;

public class CheckoutRecord implements Serializable {
    private static final long serialVersionUID = -3117571660521993397L;

    private LibraryMember member;
    private List<CheckoutRecordEntry> entries;

    public CheckoutRecord(LibraryMember member) {
        this.member = member;
        this.entries = new ArrayList<>();
    }

    public void addEntry(CheckoutRecordEntry entry) {
        entries.add(entry);
    }

    public List<CheckoutRecordEntry> getEntries() {
        return entries;
    }

    public LibraryMember getMember() {
        return member;
    }
    
    // Method to remove a checkout record entry for a specific book copy
    public boolean removeEntryForCopy(BookCopy copy) {
        return entries.removeIf(entry -> entry.getBookCopy().equals(copy));  // Removes the entry if it matches the book copy
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Checkout record for member ID: ").append(member.getMemberId()).append("\n");
        for (CheckoutRecordEntry entry : entries) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
}
