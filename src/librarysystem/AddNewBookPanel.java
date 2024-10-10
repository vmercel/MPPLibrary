package librarysystem;

import business.Author;
import business.Address;
import business.SystemController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class AddNewBookPanel extends JPanel {
    private JTextField isbnField, titleField, maxCheckoutLengthField;
    private JTextField authorFirstNameField, authorLastNameField, authorTitleField, authorStreetField, authorCityField, authorStateField, authorZipField, authorBioField;
    private JButton addAuthorButton, addBookButton, removeAuthorButton;
    private DefaultListModel<Author> authorListModel;
    private JList<Author> authorList;

    private List<Author> authors = new ArrayList<>(); // List of authors
    private SystemController controller;  // Reference to SystemController

    public AddNewBookPanel() {
        setLayout(new BorderLayout());

        // Initialize the controller
        controller = new SystemController();

        // Create the input panel for book details
        JPanel bookInputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make fields stretch horizontally

        // ISBN
        gbc.gridx = 0;
        gbc.gridy = 0;
        bookInputPanel.add(new JLabel("ISBN:"), gbc);
        isbnField = new JTextField(15);
        gbc.gridx = 1;
        bookInputPanel.add(isbnField, gbc);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        bookInputPanel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(15);
        gbc.gridx = 1;
        bookInputPanel.add(titleField, gbc);

        // Max Checkout Length
        gbc.gridx = 0;
        gbc.gridy = 2;
        bookInputPanel.add(new JLabel("Max Checkout Length (days):"), gbc);
        maxCheckoutLengthField = new JTextField(15);
        gbc.gridx = 1;
        bookInputPanel.add(maxCheckoutLengthField, gbc);

        // Add bookInputPanel to the main layout
        add(bookInputPanel, BorderLayout.NORTH);

        // Create author input panel with two columns
        JPanel authorInputPanel = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5); // Adjust insets
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Column 1 (Author Details)
        gbc.gridx = 0;
        gbc.gridy = 0;
        authorInputPanel.add(new JLabel("Author First Name:"), gbc);
        authorFirstNameField = new JTextField(15);
        gbc.gridx = 1;
        authorInputPanel.add(authorFirstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        authorInputPanel.add(new JLabel("Author Last Name:"), gbc);
        authorLastNameField = new JTextField(15);
        gbc.gridx = 1;
        authorInputPanel.add(authorLastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        authorInputPanel.add(new JLabel("Author Title:"), gbc);
        authorTitleField = new JTextField(15);
        gbc.gridx = 1;
        authorInputPanel.add(authorTitleField, gbc);

        // Column 2 (Author Address)
        gbc.gridx = 2;
        gbc.gridy = 0;
        authorInputPanel.add(new JLabel("Author Street:"), gbc);
        authorStreetField = new JTextField(15);
        gbc.gridx = 3;
        authorInputPanel.add(authorStreetField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        authorInputPanel.add(new JLabel("Author City:"), gbc);
        authorCityField = new JTextField(15);
        gbc.gridx = 3;
        authorInputPanel.add(authorCityField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        authorInputPanel.add(new JLabel("Author State:"), gbc);
        authorStateField = new JTextField(15);
        gbc.gridx = 3;
        authorInputPanel.add(authorStateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        authorInputPanel.add(new JLabel("Author Zip:"), gbc);
        authorZipField = new JTextField(15);
        gbc.gridx = 3;
        authorInputPanel.add(authorZipField, gbc);

        // Author Bio
        gbc.gridx = 0;
        gbc.gridy = 4;
        authorInputPanel.add(new JLabel("Author Bio:"), gbc);
        authorBioField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridwidth = 3;  // Span across 3 columns
        authorInputPanel.add(authorBioField, gbc);

        // Button to add the author
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        addAuthorButton = new JButton("Add Author");
        addAuthorButton.addActionListener(this::addAuthor);
        authorInputPanel.add(addAuthorButton, gbc);

        // Add authorInputPanel to the main layout
        add(authorInputPanel, BorderLayout.CENTER);

        // Author list and button panel
        authorListModel = new DefaultListModel<>();
        authorList = new JList<>(authorListModel);

        JPanel listAndButtonPanel = new JPanel(new BorderLayout());
        listAndButtonPanel.add(new JLabel("Authors Added:"), BorderLayout.NORTH);
        listAndButtonPanel.add(new JScrollPane(authorList), BorderLayout.CENTER);

        // Remove Author Button
        removeAuthorButton = new JButton("Remove Author");
        removeAuthorButton.addActionListener(this::removeAuthor);
        removeAuthorButton.setPreferredSize(new Dimension(150, 25)); // Reduce size
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(removeAuthorButton);

        // Button to add the new book
        addBookButton = new JButton("Add New Book");
        addBookButton.setPreferredSize(new Dimension(150, 25)); // Consistent size
        buttonPanel.add(addBookButton);
        addBookButton.addActionListener(this::addNewBook);

        listAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add listAndButtonPanel to the main layout
        add(listAndButtonPanel, BorderLayout.SOUTH);
    }

    // Method to add an author to the list
    private void addAuthor(ActionEvent e) {
        try {
            String firstName = authorFirstNameField.getText().trim();
            String lastName = authorLastNameField.getText().trim();
            String title = authorTitleField.getText().trim();
            String street = authorStreetField.getText().trim();
            String city = authorCityField.getText().trim();
            String state = authorStateField.getText().trim();
            String zip = authorZipField.getText().trim();
            String bio = authorBioField.getText().trim();

            // Validate that all fields for author are filled
            if (firstName.isEmpty() || lastName.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All author fields must be filled!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Address address = new Address(street, city, state, zip);
            Author author = new Author(firstName, lastName, title, address, bio);

            // Add the author to the list
            authors.add(author);
            authorListModel.addElement(author);

            // Clear fields after adding
            clearAuthorFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding author: " + ex.getMessage());
        }
    }

    // Method to remove the selected author from the list
    private void removeAuthor(ActionEvent e) {
        int selectedIndex = authorList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Remove the selected author from both the list and the model
            authors.remove(selectedIndex);
            authorListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an author to remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new book to the library collection
    private void addNewBook(ActionEvent e) {
        try {
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            int maxCheckoutLength = Integer.parseInt(maxCheckoutLengthField.getText().trim());

            if (isbn.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN and Title are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authors.isEmpty()) {
                JOptionPane.showMessageDialog(this, "At least one author must be added!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use SystemController to add the new book
            controller.addNewBook(isbn, title, maxCheckoutLength, authors);

            JOptionPane.showMessageDialog(this, "New book added successfully!");
            clearBookFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number for max checkout length.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding new book: " + ex.getMessage());
        }
    }

    // Clear fields for the author section
    private void clearAuthorFields() {
        authorFirstNameField.setText("");
        authorLastNameField.setText("");
        authorTitleField.setText("");
        authorStreetField.setText("");
        authorCityField.setText("");
        authorStateField.setText("");
        authorZipField.setText("");
        authorBioField.setText("");
    }

    // Clear fields for the book section
    private void clearBookFields() {
        isbnField.setText("");
        titleField.setText("");
        maxCheckoutLengthField.setText("");
        authors.clear();
        authorListModel.clear();
    }
}
