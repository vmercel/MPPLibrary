package business;

import java.io.Serializable;

final public class Author extends Person implements Serializable {
    private static final long serialVersionUID = 7508481940058530471L;
    
    private String bio;

    public Author(String f, String l, String t, Address a, String bio) {
        super(f, l, t, a);
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    @Override
    public String toString() {
        // Assuming Person class has public getter methods for first name (getFirstName), last name (getLastName), and title (getTitle)
        return getFirstName() + " " + getLastName() + " (" + getAddress() + ")";
    }
}
