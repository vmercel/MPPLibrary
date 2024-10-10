package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import business.LibraryMember;
import business.Book;
import business.BookCopy;
import dataaccess.DataAccessFacade;
import dataaccess.DataAccess;
import business.SystemController;

public class CheckoutBookPanel extends JPanel {

    private JTextField memberIdField, isbnField, checkoutDateField, dueDateField, copyNumberField;
    private JButton checkoutButton;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public CheckoutBookPanel() {
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between elements
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Member ID:"), gbc);

        memberIdField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(memberIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("ISBN:"), gbc);

        isbnField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Copy Number (optional):"), gbc);

        copyNumberField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(copyNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Checkout Date (MM/dd/yyyy):"), gbc);

        checkoutDateField = new JTextField(15);
        checkoutDateField.setText(LocalDate.now().format(dateFormatter)); // Default to today’s date
        gbc.gridx = 1;
        inputPanel.add(checkoutDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Due Date (MM/dd/yyyy):"), gbc);

        dueDateField = new JTextField(15);
        dueDateField.setText(LocalDate.now().plusWeeks(2).format(dateFormatter)); // Default to 2 weeks from today
        gbc.gridx = 1;
        inputPanel.add(dueDateField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkoutButton = new JButton("Checkout Book");
        checkoutButton.addActionListener(this::checkoutBook);
        buttonPanel.add(checkoutButton);

        // Add panels to the main panel
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to handle the book checkout
    private void checkoutBook(ActionEvent e) {
        String memberId = memberIdField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (memberId.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both Member ID and ISBN must be provided!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Call SystemController to checkout the book
            SystemController controller = new SystemController();
            controller.checkoutBook(memberId, isbn);

            JOptionPane.showMessageDialog(this, "Book checked out successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void clearFields() {
        memberIdField.setText("");
        isbnField.setText("");
        copyNumberField.setText("");
        checkoutDateField.setText(LocalDate.now().format(dateFormatter));
        dueDateField.setText(LocalDate.now().plusWeeks(2).format(dateFormatter));
    }
}
