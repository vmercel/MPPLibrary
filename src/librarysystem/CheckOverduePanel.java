package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import business.Book;
import business.LibraryMember;
import business.SystemController;

public class CheckOverduePanel extends JPanel {
    private JTextField isbnField;
    private JButton checkOverdueButton;
    private JTextArea bookInfoArea;
    private JList<String> overdueBookList;
    private DefaultListModel<String> listModel;
    private SystemController controller;

    public CheckOverduePanel() {
        controller = new SystemController(); // Initialize controller
        setLayout(new BorderLayout());

        // Create a panel for searching by ISBN
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.WEST;

        // Label and text field for ISBN
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("ISBN:"), gbc);

        isbnField = new JTextField(15);
        gbc.gridx = 1;
        searchPanel.add(isbnField, gbc);

        // Button for checking overdue by ISBN
        checkOverdueButton = new JButton("Check Overdue by ISBN");
        checkOverdueButton.addActionListener(e -> checkOverdueByIsbn());
        gbc.gridx = 2;
        searchPanel.add(checkOverdueButton, gbc);

        add(searchPanel, BorderLayout.NORTH);

        // List model for displaying overdue books
        listModel = new DefaultListModel<>();
        overdueBookList = new JList<>(listModel);
        overdueBookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a mouse listener for clicking on the list items
        overdueBookList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Single click to display details
                    String selectedBookId = overdueBookList.getSelectedValue();
                    if (selectedBookId != null) {
                        displayBookDetails(selectedBookId); // Display book details in the text area
                    }
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(overdueBookList);
        add(listScrollPane, BorderLayout.CENTER);

        // Text area for displaying book and member details or "No overdue books found"
        bookInfoArea = new JTextArea(10, 40);
        bookInfoArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(bookInfoArea);
        add(infoScrollPane, BorderLayout.SOUTH);

        // Automatically fetch and display the overdue books when the panel is shown
        fetchAndDisplayOverdueBooks();
    }

    // Method to check if any copy of the book by ISBN is overdue
    private void checkOverdueByIsbn() {
        String isbn = isbnField.getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the controller method to check for overdue books by ISBN
        boolean isOverdue = controller.isBookOverdueByIsbn(isbn);

        if (isOverdue) {
            JOptionPane.showMessageDialog(this, "At least one copy of the book is overdue.", "Overdue Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No copies of the book are overdue.", "Overdue Status", JOptionPane.INFORMATION_MESSAGE);
        }

        clearFields();
    }

    // Fetches and displays the list of overdue books
    private void fetchAndDisplayOverdueBooks() {
        listModel.clear(); // Clear the list before adding items
        Map<Book, LibraryMember> overdueBooks = controller.getOverdueBooks(); // Fetch overdue books using the controller

        // If there are overdue books, display them in the list
        if (!overdueBooks.isEmpty()) {
            for (Book book : overdueBooks.keySet()) {
                listModel.addElement(book.getIsbn()); // Add ISBN of the overdue books to the list
            }
        } else {
            // If no overdue books, display message in the text area
            bookInfoArea.setText("No overdue books found.");
        }
    }

    // Displays details of the selected book and the member who checked it out
    private void displayBookDetails(String isbn) {
        Map<Book, LibraryMember> overdueBooks = controller.getOverdueBooks();
        StringBuilder details = new StringBuilder();

        // Find the record for the given ISBN and show details
        for (Map.Entry<Book, LibraryMember> entry : overdueBooks.entrySet()) {
            Book book = entry.getKey();
            LibraryMember member = entry.getValue();
            if (book.getIsbn().equals(isbn)) {
                details.append("Book Title: ").append(book.getTitle()).append("\n");
                details.append("ISBN: ").append(book.getIsbn()).append("\n");
                details.append("Checked Out By: ").append(member.getFirstName()).append(" ").append(member.getLastName()).append("\n");
                break;
            }
        }

        bookInfoArea.setText(details.toString()); // Set the book details to the text area
    }

    // Clear the input field after checking
    private void clearFields() {
        isbnField.setText("");
    }
}
