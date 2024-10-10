package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import business.ControllerInterface;
import business.Book;
import business.SystemController;

public class DisplayAllBookIDsPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea bookInfoArea;
    private JTextField searchField;
    private JButton searchButton;
    private ControllerInterface ci;
    private JList<String> bookIdsList;
    private DefaultListModel<String> listModel;

    public DisplayAllBookIDsPanel() {
        // Set layout as CardLayout for switching between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize the controller interface to fetch data
        ci = new SystemController();

        // Panel to display list of book IDs with a search bar
        JPanel bookListPanel = new JPanel(new BorderLayout());

        // Search bar components
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Book ISBN:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add search panel to the top
        bookListPanel.add(searchPanel, BorderLayout.NORTH);

        // List to display all book IDs, and making it clickable
        listModel = new DefaultListModel<>();
        bookIdsList = new JList<>(listModel);
        bookIdsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookIdsList.setBackground(Color.WHITE);
        bookIdsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Detect double click on list item
                    String selectedBookId = bookIdsList.getSelectedValue();
                    if (selectedBookId != null) {
                        displayBookInfo(selectedBookId);  // Display book info on double click
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(bookIdsList);
        bookListPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button to return to the book list
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "bookList"));

        // Panel to display detailed book info
        JPanel bookInfoPanel = new JPanel(new BorderLayout());
        bookInfoArea = new JTextArea(15, 40);
        bookInfoArea.setEditable(false);
        bookInfoArea.setBackground(Color.WHITE); // Set background to white
        JScrollPane infoScrollPane = new JScrollPane(bookInfoArea);
        bookInfoPanel.add(infoScrollPane, BorderLayout.CENTER);
        bookInfoPanel.add(backButton, BorderLayout.SOUTH);

        // Add both panels to the card layout
        cardPanel.add(bookListPanel, "bookList");
        cardPanel.add(bookInfoPanel, "bookInfo");

        // Add cardPanel to the main layout
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        // Fetch and display all book IDs
        displayAllBookIds();

        // Add action listener for search button
        searchButton.addActionListener(e -> searchBookId());
    }

    // Method to fetch and display all book IDs
    private void displayAllBookIds() {
        List<String> bookIds = ci.allBookIds();  // Fetch book IDs from controller
        listModel.clear();  // Clear previous entries
        for (String id : bookIds) {
            listModel.addElement(id);  // Add each book ID to the list
        }
    }

    // Method to search for a specific book ID (ISBN)
    private void searchBookId() {
        String searchId = searchField.getText().trim();

        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Book ISBN to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetch book info for the entered ISBN
        Book book = ci.getBookByIsbn(searchId);

        if (book != null) {
            displayBookInfo(searchId);  // Show book info if found
        } else {
            JOptionPane.showMessageDialog(this, "No book found with ISBN: " + searchId, "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Method to display book information when a book ID is clicked or found by search
    private void displayBookInfo(String bookId) {
        Book book = ci.getBookByIsbn(bookId);
        if (book != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ISBN: ").append(book.getIsbn()).append("\n");
            sb.append("Title: ").append(book.getTitle()).append("\n");
            sb.append("Authors: ").append(book.getAuthors()).append("\n");
            sb.append("Max Checkout Length: ").append(book.getMaxCheckoutLength()).append(" days\n");
            sb.append("Available Copies: ").append(book.getNumCopies()).append("\n");
            bookInfoArea.setText(sb.toString());

            // Switch to the book info panel
            cardLayout.show(cardPanel, "bookInfo");
        } else {
            JOptionPane.showMessageDialog(this, "Book information could not be retrieved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
