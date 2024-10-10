package dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import business.Address;
import business.Author;
import business.Book;
import business.LibraryMember;
import business.CheckoutRecord;
import business.BookCopy;
import business.CheckoutRecordEntry;

/**
 * This class loads data into the data repository and also
 * sets up the storage units that are used in the application.
 * The main method in this class must be run once (and only
 * once) before the rest of the application can work properly.
 * It will create three serialized objects in the dataaccess.storage
 * folder.
 */
public class TestData {
    public static void main(String[] args) {
        TestData td = new TestData();
        td.bookData(); // Initialize books and their copies
        td.libraryMemberData(); // Initialize library members
        td.userData(); // Initialize users
        td.checkoutRecordData(); // Initialize checkout records with overdue and non-overdue cases
        DataAccess da = new DataAccessFacade();
        System.out.println(da.readBooksMap());
        System.out.println(da.readUserMap());
    }

    /// Create books with copies
    public void bookData() {
        allBooks.get(0).addCopy(); // Adding copies for each book
        allBooks.get(0).addCopy();
        allBooks.get(1).addCopy();
        allBooks.get(3).addCopy();
        allBooks.get(2).addCopy();
        allBooks.get(2).addCopy();
        DataAccessFacade.loadBookMap(allBooks); // Load books into storage
    }

    // Create users with different roles (LIBRARIAN, ADMIN, BOTH)
    public void userData() {
        DataAccessFacade.loadUserMap(allUsers);
    }

    // Create library members
    public void libraryMemberData() {
        LibraryMember libraryMember = new LibraryMember("1001", "Andy", "Rogers", "641-223-2211", addresses.get(4));
        members.add(libraryMember);
        libraryMember = new LibraryMember("1002", "Drew", "Stevens", "702-998-2414", addresses.get(5));
        members.add(libraryMember);

        libraryMember = new LibraryMember("1003", "Sarah", "Eagleton", "451-234-8811", addresses.get(6));
        members.add(libraryMember);

        libraryMember = new LibraryMember("1004", "Ricardo", "Montalbahn", "641-472-2871", addresses.get(7));
        members.add(libraryMember);

        // Adding more members with overdue books
        libraryMember = new LibraryMember("1005", "Laura", "Smith", "555-123-4567", addresses.get(0));
        members.add(libraryMember);

        libraryMember = new LibraryMember("1006", "John", "Doe", "555-987-6543", addresses.get(1));
        members.add(libraryMember);

        DataAccessFacade.loadMemberMap(members);
    }

    // Create checkout records for some members and books (including overdue cases)
    public void checkoutRecordData() {
        DataAccess da = new DataAccessFacade();

        // Set a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Fetching members and books
        LibraryMember member1 = da.readMemberMap().get("1001");
        LibraryMember member2 = da.readMemberMap().get("1002");
        Book book1 = da.readBooksMap().get("23-11451");
        Book book2 = da.readBooksMap().get("28-12331");

        if (member1 != null && book1 != null && book2 != null) {
            // Get the first available copy of each book
            BookCopy book1Copy = book1.getNextAvailableCopy();
            BookCopy book2Copy = book2.getNextAvailableCopy();

            // Create a checkout record for member1
            CheckoutRecord record1 = new CheckoutRecord(member1);

            // Hard code checkout and due dates using LocalDate.parse with a predefined date format
            LocalDate checkoutDate1 = LocalDate.parse("2024-07-15", formatter);
            LocalDate dueDate1 = LocalDate.parse("2024-08-15", formatter); // Overdue date

            // Create a CheckoutRecordEntry
            CheckoutRecordEntry entry1 = new CheckoutRecordEntry(book1Copy, member1, checkoutDate1, dueDate1);
            record1.addEntry(entry1);
            member1.addCheckoutRecord(record1);

            // Log the created record
            System.out.println("Checkout record created for member: " + member1.getMemberId());
            System.out.println("Book: " + book1.getTitle() + " | Due date: " + dueDate1);

            // Create another checkout record for member2
            CheckoutRecord record2 = new CheckoutRecord(member2);

            // Hard code the dates for member2
            LocalDate checkoutDate2 = LocalDate.parse("2024-06-01", formatter);
            LocalDate dueDate2 = LocalDate.parse("2024-07-01", formatter); // Overdue date

            // Create a CheckoutRecordEntry for member2
            CheckoutRecordEntry entry2 = new CheckoutRecordEntry(book2Copy, member2, checkoutDate2, dueDate2);
            record2.addEntry(entry2);
            member2.addCheckoutRecord(record2);

            // Log the created record
            System.out.println("Checkout record created for member: " + member2.getMemberId());
            System.out.println("Book: " + book2.getTitle() + " | Due date: " + dueDate2);

            // Save the updated member data and books
            DataAccessFacade.loadMemberMap(members);
            DataAccessFacade.loadBookMap(allBooks);
        }
    }


    ////////////// DATA //////////////
    List<LibraryMember> members = new ArrayList<>();
    @SuppressWarnings("serial")
    List<Address> addresses = new ArrayList<Address>() {
        {
            add(new Address("101 S. Main", "Fairfield", "IA", "52556"));
            add(new Address("51 S. George", "Georgetown", "MI", "65434"));
            add(new Address("23 Headley Ave", "Seville", "Georgia", "41234"));
            add(new Address("1 N. Baton", "Baton Rouge", "LA", "33556"));
            add(new Address("5001 Venice Dr.", "Los Angeles", "CA", "93736"));
            add(new Address("1435 Channing Ave", "Palo Alto", "CA", "94301"));
            add(new Address("42 Dogwood Dr.", "Fairfield", "IA", "52556"));
            add(new Address("501 Central", "Mountain View", "CA", "94707"));
        }
    };

    @SuppressWarnings("serial")
    public List<Author> allAuthors = new ArrayList<Author>() {
        {
            add(new Author("Joe", "Thomas", "641-445-2123", addresses.get(0), "A happy man is he."));
            add(new Author("Sandra", "Thomas", "641-445-2123", addresses.get(0), "A happy wife is she."));
            add(new Author("Nirmal", "Pugh", "641-919-3223", addresses.get(1), "Thinker of thoughts."));
            add(new Author("Andrew", "Cleveland", "976-445-2232", addresses.get(2), "Author of childrens' books."));
            add(new Author("Sarah", "Connor", "123-422-2663", addresses.get(3), "Known for her clever style."));
        }
    };

    @SuppressWarnings("serial")
    List<Book> allBooks = new ArrayList<Book>() {
        {
            add(new Book("23-11451", "The Big Fish", 21, Arrays.asList(allAuthors.get(0), allAuthors.get(1))));
            add(new Book("28-12331", "Antarctica", 7, Arrays.asList(allAuthors.get(2))));
            add(new Book("99-22223", "Thinking Java", 21, Arrays.asList(allAuthors.get(3))));
            add(new Book("48-56882", "Jimmy's First Day of School", 7, Arrays.asList(allAuthors.get(4))));
        }
    };

    @SuppressWarnings("serial")
    List<User> allUsers = new ArrayList<User>() {
        {
            add(new User("101", "xyz", Auth.LIBRARIAN));
            add(new User("102", "abc", Auth.ADMIN));
            add(new User("103", "111", Auth.BOTH));
        }
    };
}
