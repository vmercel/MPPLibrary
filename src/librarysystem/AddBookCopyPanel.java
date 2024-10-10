package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import dataaccess.DataAccessFacade;
import business.Book;

public class AddBookCopyPanel extends JPanel {
    private JTextField isbnField;
    private JButton addCopyButton;

    public AddBookCopyPanel() {
        // Use BorderLayout for proper arrangement of panels
        setLayout(new BorderLayout());
        
        // Create the input panel with GridBagLayout for flexible layout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around the components
        gbc.anchor = GridBagConstraints.WEST;    // Align components to the left
        
        // ISBN label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ISBN:"), gbc);
        
        isbnField = new JTextField(15);  // Set preferred width for the text field
        gbc.gridx = 1;
        inputPanel.add(isbnField, gbc);
        
        // Button panel for the "Add Book Copy" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center the button
        addCopyButton = new JButton("Add Book Copy");
        addCopyButton.addActionListener(this::addBookCopy);
        buttonPanel.add(addCopyButton);

        // Add the inputPanel to the center and buttonPanel to the bottom
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to add a copy of an existing book
    private void addBookCopy(ActionEvent e) {
        String isbn = isbnField.getText().trim();

        // Validate that the ISBN field is not empty
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ISBN.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Call DataAccessFacade to add a copy of the book
            DataAccessFacade dataAccess = new DataAccessFacade();
            dataAccess.addBookCopy(isbn);

            // Show confirmation dialog
            JOptionPane.showMessageDialog(this, "Book copy added successfully!");
        } catch (IllegalArgumentException ex) {
            // If the book does not exist, show an error message
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        clearFields();
    }

    // Clear the fields after submission
    private void clearFields() {
        isbnField.setText("");
    }
}
