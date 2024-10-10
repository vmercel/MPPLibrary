package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;
import business.CheckoutRecordEntry;

public class SystemController implements ControllerInterface {
    public static Auth currentAuth = null;

    // Method to log in a user
    public void login(String id, String password) throws LoginException {
        DataAccessFacade da = new DataAccessFacade();
        HashMap<String, User> map = da.readUserMap();
        if (!map.containsKey(id)) {
            throw new LoginException("ID " + id + " not found");
        }
        String passwordFound = map.get(id).getPassword();
        if (!passwordFound.equals(password)) {
            throw new LoginException("Password incorrect");
        }
        currentAuth = map.get(id).getAuthorization();
    }

    // Method to get all member IDs
    @Override
    public List<String> allMemberIds() {
        DataAccess da = new DataAccessFacade();
        List<String> retval = new ArrayList<>();
        retval.addAll(da.readMemberMap().keySet());
        return retval;
    }

    // Method to get all book IDs
    @Override
    public List<String> allBookIds() {
        DataAccessFacade da = new DataAccessFacade();
        List<String> retval = new ArrayList<>();
        retval.addAll(da.readBooksMap().keySet());
        return retval;
    }

    // New Method: Checkout a book for a member
 // Method: Checkout a book for a member
    public void checkoutBook(String memberId, String isbn) throws Exception {
        DataAccessFacade da = new DataAccessFacade();
        
        // Retrieve the member information
        HashMap<String, LibraryMember> memberMap = da.readMemberMap();
        LibraryMember member = memberMap.get(memberId);
        
        if (member == null) {
            throw new Exception("Library member with ID " + memberId + " does not exist.");
        }

        // Retrieve the book information
        HashMap<String, Book> bookMap = da.readBooksMap();
        Book book = bookMap.get(isbn);
        
        if (book == null) {
            throw new Exception("Book with ISBN " + isbn + " does not exist.");
        }

        // Get the next available copy of the book
        BookCopy availableCopy = book.getNextAvailableCopy();
        if (availableCopy == null) {
            throw new Exception("No available copies for the book with ISBN " + isbn);
        }

        // Proceed with the checkout
        LocalDate checkoutDate = LocalDate.now();
        LocalDate dueDate = checkoutDate.plusWeeks(2); // Default checkout period of 2 weeks
        
        // Create a new checkout record entry
        CheckoutRecordEntry entry = new CheckoutRecordEntry(availableCopy, member, checkoutDate, dueDate);

        // Ensure the member has a checkout record, if not, create one
        if (member.getCheckoutRecord() == null) {
            member.addCheckoutRecord(new CheckoutRecord(member)); // Initialize a new checkout record for the member
        }

        // Add the new entry to the member's checkout record
        member.getCheckoutRecord().addEntry(entry);
        
        // Mark the book copy as checked out (make it unavailable)
        availableCopy.changeAvailability();
        
        // Update data in persistent storage
        da.saveNewMember(member);  // Save updated member data with the checkout record
        da.saveNewBook(book);      // Save updated book with the updated copy information
    }

    // New Method: Retrieve the checkout record for a member
    public CheckoutRecord getCheckoutRecord(String memberId) throws Exception {
        DataAccess da = new DataAccessFacade();
        LibraryMember member = da.readMemberMap().get(memberId);

        if (member == null) {
            throw new Exception("Member with ID " + memberId + " not found.");
        }

        CheckoutRecord record = member.getCheckoutRecord();
        if (record == null || record.getEntries().isEmpty()) {
            throw new Exception("No checkout record found for member " + memberId);
        }

        return record;
    }
    
    
    public boolean isBookOverdueByIsbn(String isbn) {
        DataAccessFacade da = new DataAccessFacade();

        // Fetch all checkout records
        HashMap<String, CheckoutRecord> checkouts = da.readCheckoutMap();

        // Iterate through all checkout records to find if any copies of the book with the given ISBN are overdue
        for (CheckoutRecord record : checkouts.values()) {
            for (CheckoutRecordEntry entry : record.getEntries()) {
                BookCopy bookCopy = entry.getBookCopy();
                // Check if the book copy's book matches the given ISBN and if it is overdue
                if (bookCopy.getBook().getIsbn().equals(isbn) && entry.isOverdue()) {
                    return true;  // At least one copy of the book is overdue
                }
            }
        }

        // No overdue copies found for the given ISBN
        return false;
    }
    
    // Method to add a new book to the library
    public void addNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors) throws Exception {
        DataAccessFacade da = new DataAccessFacade();
        
        // Check if a book with the same ISBN already exists
        HashMap<String, Book> bookMap = da.readBooksMap();
        if (bookMap.containsKey(isbn)) {
            throw new Exception("Book with ISBN " + isbn + " already exists.");
        }

        // Create a new Book object
        Book newBook = new Book(isbn, title, maxCheckoutLength, authors);

        // Save the new book using DataAccessFacade
        da.saveNewBook(newBook);
        
        // Refresh the bookMap in memory by reloading it from storage (optional, depending on how you handle data)
        bookMap = da.readBooksMap();
    }
 
    
    // New Method: Get list of overdue books
    public HashMap<Book, LibraryMember> getOverdueBooks() {
        DataAccessFacade da = new DataAccessFacade();
        HashMap<String, CheckoutRecord> checkouts = da.readCheckoutMap();
        HashMap<Book, LibraryMember> overdueBooks = new HashMap<>();

        // Loop through all checkout records and find overdue entries
        for (CheckoutRecord record : checkouts.values()) {
            for (CheckoutRecordEntry entry : record.getEntries()) {
                if (entry.isOverdue()) {
                    overdueBooks.put(entry.getBookCopy().getBook(), entry.getLibraryMember());
                }
            }
        }
        System.out.println("now: "+LocalDate.now());
        return overdueBooks;
    }

    public void checkInBook(String memberId, String isbn, int copyNum) throws Exception {
        DataAccessFacade da = new DataAccessFacade();

        // Retrieve the member and book
        HashMap<String, LibraryMember> memberMap = da.readMemberMap();
        LibraryMember member = memberMap.get(memberId);

        HashMap<String, Book> bookMap = da.readBooksMap();
        Book book = bookMap.get(isbn);

        if (member == null) {
            throw new Exception("Library member with ID " + memberId + " does not exist.");
        }

        if (book == null) {
            throw new Exception("Book with ISBN " + isbn + " does not exist.");
        }

        // Get the book copy that is being returned
        BookCopy returningCopy = null;
        for (BookCopy copy : book.getCopies()) {
            if (copy.getCopyNum() == copyNum) {
                returningCopy = copy;
                break;
            }
        }

        if (returningCopy == null) {
            throw new Exception("No book copy found with number " + copyNum);
        }

        // Remove the corresponding checkout record entry from the member's checkout record
        CheckoutRecord record = member.getCheckoutRecord();
        if (record == null || record.getEntries().isEmpty()) {
            throw new Exception("No checkout records found for this member.");
        }

        // Remove the checkout record entry related to this book copy
        boolean entryRemoved = record.removeEntryForCopy(returningCopy);
        if (!entryRemoved) {
            throw new Exception("Checkout record for this book copy not found.");
        }

        // Mark the book copy as available
        returningCopy.changeAvailability(); // Now the book copy is available

        // Save the updated member and book information
        da.saveNewMember(member);  // Save updated member data without the checkout entry
        da.saveNewBook(book);      // Save updated book with the updated availability
    }
    
    @Override
    public void logout() {
        currentAuth = null;  // Reset the logged-in state
    }
    
    
    @Override
    public LibraryMember getLibraryMemberById(String memberId) {
        DataAccess da = new DataAccessFacade();
        return da.readMemberMap().get(memberId);
    }
    
    
    // New method to fetch a book by ISBN
    @Override
    public Book getBookByIsbn(String isbn) {
        DataAccess da = new DataAccessFacade();
        HashMap<String, Book> books = da.readBooksMap();  // Fetch the book map
        return books.get(isbn);  // Return the book if found, otherwise return null
    }
}
