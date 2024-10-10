package dataaccess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import business.Book;
import business.BookCopy;
import business.CheckoutRecord;
import business.CheckoutRecordEntry;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public class DataAccessFacade implements DataAccess {

    enum StorageType {
        BOOKS, MEMBERS, USERS, CHECKOUTS;  // Added CHECKOUTS to track checkouts
    }

    public static final String OUTPUT_DIR = System.getProperty("user.dir") 
            + "\\src\\dataaccess\\storage";
    public static final String DATE_PATTERN = "MM/dd/yyyy";

    // Save a new library member to the system
    public void saveNewMember(LibraryMember member) {
        HashMap<String, LibraryMember> mems = readMemberMap();
        String memberId = member.getMemberId();
        mems.put(memberId, member);
        saveToStorage(StorageType.MEMBERS, mems);    
    }
    
    // Save a new book to the system
    public void saveNewBook(Book book) {
        HashMap<String, Book> books = readBooksMap();
        String isbn = book.getIsbn();
        books.put(isbn, book);
        saveToStorage(StorageType.BOOKS, books);
    }

    // Add a new copy of an existing book
    public void addBookCopy(String isbn) {
        HashMap<String, Book> books = readBooksMap();
        Book book = books.get(isbn);
        if (book != null) {
            book.addCopy();
            saveToStorage(StorageType.BOOKS, books);
        }
    }
    
    // Checkout a book for a member
    public void checkoutBook(String memberId, String isbn, LocalDate checkoutDate, LocalDate dueDate) {
        HashMap<String, LibraryMember> members = readMemberMap();
        LibraryMember member = members.get(memberId);

        HashMap<String, Book> books = readBooksMap();
        Book book = books.get(isbn);

        if (member != null && book != null) {
            BookCopy availableCopy = book.getNextAvailableCopy();
            if (availableCopy != null) {
                // Create a new CheckoutRecordEntry
                CheckoutRecordEntry entry = new CheckoutRecordEntry(availableCopy, member, checkoutDate, dueDate);
                
                // Retrieve or create the CheckoutRecord for this member
                CheckoutRecord record = member.getCheckoutRecord();
                if (record == null) {
                    record = new CheckoutRecord(member);  // If no checkout record exists, create one
                }
                
                record.addEntry(entry);  // Add the new entry to the member's checkout record

                // Save the checkout record to storage
                HashMap<String, CheckoutRecord> checkouts = readCheckoutMap();
                checkouts.put(String.valueOf(availableCopy.getCopyNum()), record);  // Fix: Convert copy number to String
                saveToStorage(StorageType.CHECKOUTS, checkouts);

                // Update book and member data
                availableCopy.changeAvailability();  // Mark the copy as unavailable
                member.addCheckoutRecord(record);  // Add the checkout record to the member
                saveToStorage(StorageType.BOOKS, books);  // Save updated books
                saveToStorage(StorageType.MEMBERS, members);  // Save updated members
            }
        }
    }

    // Get a member's checkout record
    public CheckoutRecord getCheckoutRecord(String memberId) {
        HashMap<String, LibraryMember> members = readMemberMap();
        LibraryMember member = members.get(memberId);
        if (member != null) {
            return member.getCheckoutRecord();
        }
        return null;
    }

    // Determine if a book copy is overdue and which member has it
    public boolean isBookOverdue(String copyNumber) {
        HashMap<String, CheckoutRecord> checkouts = readCheckoutMap();
        CheckoutRecord record = checkouts.get(copyNumber);
        if (record != null) {
            for (CheckoutRecordEntry entry : record.getEntries()) {
                if (entry.getBookCopy().getCopyNum() == Integer.parseInt(copyNumber) && entry.isOverdue()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Read checkout map
    @SuppressWarnings("unchecked")
    public HashMap<String, CheckoutRecord> readCheckoutMap() {
        return (HashMap<String, CheckoutRecord>) readFromStorage(StorageType.CHECKOUTS);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Book> readBooksMap() {
        return (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, LibraryMember> readMemberMap() {
        return (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, User> readUserMap() {
        return (HashMap<String, User>) readFromStorage(StorageType.USERS);
    }

    // Methods to load maps
    static void loadBookMap(List<Book> bookList) {
        HashMap<String, Book> books = new HashMap<>();
        bookList.forEach(book -> books.put(book.getIsbn(), book));
        saveToStorage(StorageType.BOOKS, books);
    }

    static void loadUserMap(List<User> userList) {
        HashMap<String, User> users = new HashMap<>();
        userList.forEach(user -> users.put(user.getId(), user));
        saveToStorage(StorageType.USERS, users);
    }

    static void loadMemberMap(List<LibraryMember> memberList) {
        HashMap<String, LibraryMember> members = new HashMap<>();
        memberList.forEach(member -> members.put(member.getMemberId(), member));
        saveToStorage(StorageType.MEMBERS, members);
    }

    // Save to storage utility method
 // Save to storage utility method
    static void saveToStorage(StorageType type, Object ob) {
        ObjectOutputStream out = null;
        try {
            Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());

            // Ensure the directory exists before saving the file
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            out = new ObjectOutputStream(Files.newOutputStream(path));

            // Log the object being saved
            System.out.println("Saving to storage: " + type.toString());
            if (type == StorageType.CHECKOUTS) {
                HashMap<String, CheckoutRecord> checkoutMap = (HashMap<String, CheckoutRecord>) ob;
                checkoutMap.forEach((key, record) -> {
                    System.out.println("Checkout Record: Member ID: " + record.getMember().getMemberId());
                    for (CheckoutRecordEntry entry : record.getEntries()) {
                        System.out.println(" - Book: " + entry.getBookCopy().getBook().getTitle());
                        System.out.println(" - Due Date: " + entry.getDueDate());  // Log due date being saved
                    }
                });
            }

            out.writeObject(ob);  // Serialize the object
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }




    // Read from storage utility method
    static Object readFromStorage(StorageType type) {
        ObjectInputStream in = null;
        Object retVal = null;
        try {
            Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());

            if (!Files.exists(path)) {
                System.out.println("File not found: " + path + ". Creating a new one.");
                return new HashMap<>();
            }

            in = new ObjectInputStream(Files.newInputStream(path));
            retVal = in.readObject();

            // Log the object being read
            System.out.println("Reading from storage: " + type.toString());
            if (type == StorageType.CHECKOUTS) {
                HashMap<String, CheckoutRecord> checkoutMap = (HashMap<String, CheckoutRecord>) retVal;
                checkoutMap.forEach((key, record) -> {
                    System.out.println("Loaded Checkout Record: Member ID: " + record.getMember().getMemberId());
                    for (CheckoutRecordEntry entry : record.getEntries()) {
                        System.out.println(" - Book: " + entry.getBookCopy().getBook().getTitle());
                        System.out.println(" - Due Date: " + entry.getDueDate());
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }

    
    
    // New method to check if any copy of a book by ISBN is overdue
    public boolean isBookOverdueByIsbn(String isbn) {
        HashMap<String, Book> bookMap = readBooksMap();  // Fetch the book map from storage
        Book book = bookMap.get(isbn);  // Get the book by its ISBN

        if (book == null) {
            return false;  // If no book is found by ISBN, return false
        }

        // Check all copies of the book to see if any are overdue
        for (BookCopy copy : book.getCopies()) {
            if (!copy.isAvailable()) {  // Only check out copies that are not available (checked out)
                CheckoutRecordEntry entry = findCheckoutRecordEntryByCopy(copy);
                if (entry != null && entry.isOverdue()) {
                    return true;  // If any copy is overdue, return true
                }
            }
        }

        return false;  // If no overdue copies are found, return false
    }
    
    // Helper method to find the checkout record entry for a given book copy
    private CheckoutRecordEntry findCheckoutRecordEntryByCopy(BookCopy copy) {
        HashMap<String, CheckoutRecord> checkoutMap = readCheckoutMap();
        for (CheckoutRecord record : checkoutMap.values()) {
            for (CheckoutRecordEntry entry : record.getEntries()) {
                if (entry.getBookCopy().equals(copy)) {
                    return entry;
                }
            }
        }
        return null;
    }
    
    
    public HashMap<Book, LibraryMember> getOverdueBooks() {
        DataAccessFacade da = new DataAccessFacade();
        HashMap<String, CheckoutRecord> checkouts = da.readCheckoutMap(); // Ensure this map is not empty
        HashMap<Book, LibraryMember> overdueBooks = new HashMap<>();

        // Debugging: Print out all checkout records
        System.out.println("All checkout records: " + checkouts);

        // Loop through all checkout records and find overdue entries
        for (CheckoutRecord record : checkouts.values()) {
            for (CheckoutRecordEntry entry : record.getEntries()) {
                if (entry.isOverdue()) {
                    overdueBooks.put(entry.getBookCopy().getBook(), entry.getLibraryMember());
                }
            }
        }

        // Debugging: Print found overdue books
        System.out.println("Found overdue books: " + overdueBooks);

        return overdueBooks;
    }


}
