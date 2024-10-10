package business;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckoutRecordEntry implements Serializable {
    private static final long serialVersionUID = 5147265048973262104L;
    
    private BookCopy bookCopy;  // The specific copy of the book that was checked out
    private LocalDate checkoutDate;  // Date when the book was checked out
    private LocalDate dueDate;  // Due date for the book
    private LibraryMember member;  // The member who checked out the book

    // Constructor to create a checkout entry
    public CheckoutRecordEntry(BookCopy bookCopy, LibraryMember member, LocalDate checkoutDate, LocalDate dueDate) {
        this.bookCopy = bookCopy;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.member = member;
    }

    // Get the book copy that was checked out
    public BookCopy getBookCopy() {
        return bookCopy;
    }

    // Get the checkout date
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    // Get the due date
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Check if the book copy is overdue
    public boolean isOverdue() {
    	System.out.println("Due: "+dueDate + "\n"+checkoutDate);
    	return LocalDate.now().isAfter(dueDate);
    }
    
    // Get the library member who checked out the book
    public LibraryMember getLibraryMember() {
        return member;
    }

    // String representation of the checkout entry
    @Override
    public String toString() {
        return "Book Copy: " + bookCopy.getCopyNum() + " (" + bookCopy.getBook().getTitle() + ")" +
               ", Checkout Date: " + checkoutDate + ", Due Date: " + dueDate;
    }
}
