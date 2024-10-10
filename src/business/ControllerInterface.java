package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<String> allBookIds();
	
    // New method to fetch member details by ID
    LibraryMember getLibraryMemberById(String memberId);
    
    // Add this method to fetch book by ISBN
    Book getBookByIsbn(String isbn);  // Make sure this matches the implementation
	
    void checkInBook(String memberId, String isbn, int copyNum) throws Exception;
    
    void logout();
}
