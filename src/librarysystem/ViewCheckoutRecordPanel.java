package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import dataaccess.DataAccessFacade;
import business.CheckoutRecord;
import business.SystemController;
public class ViewCheckoutRecordPanel extends JPanel {
    private JTextField memberIdField;
    private JTextArea checkoutRecordArea;
    private JButton viewRecordButton;

    public ViewCheckoutRecordPanel() {
        // Use BorderLayout for main layout
        setLayout(new BorderLayout(10, 10));  // Add spacing between components

        // Create the input panel with GridBagLayout for flexible alignment
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add padding around components
        gbc.anchor = GridBagConstraints.WEST;

        // Label for Member ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Member ID:"), gbc);

        // TextField for Member ID input
        memberIdField = new JTextField(15);  // Set preferred size
        gbc.gridx = 1;
        inputPanel.add(memberIdField, gbc);

        // Add inputPanel to the top of the layout
        add(inputPanel, BorderLayout.NORTH);

        // Button to view the checkout record
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        viewRecordButton = new JButton("View Checkout Record");
        viewRecordButton.addActionListener(this::viewCheckoutRecord);
        buttonPanel.add(viewRecordButton);

        // Add the button panel below the input field
        add(buttonPanel, BorderLayout.CENTER);

        // TextArea for displaying the checkout record
        checkoutRecordArea = new JTextArea(10, 40);  // Set preferred size
        checkoutRecordArea.setEditable(false);
        checkoutRecordArea.setLineWrap(true);
        checkoutRecordArea.setWrapStyleWord(true);

        // Place the JTextArea inside a JScrollPane for scrollable output
        JScrollPane scrollPane = new JScrollPane(checkoutRecordArea);
        add(scrollPane, BorderLayout.SOUTH);
    }

    // Method to view the checkout record of a member
    private void viewCheckoutRecord(ActionEvent e) {
        String memberId = memberIdField.getText().trim();

        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Member ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Call SystemController to retrieve the checkout record
            SystemController controller = new SystemController();
            CheckoutRecord record = controller.getCheckoutRecord(memberId);

            checkoutRecordArea.setText(formatCheckoutRecord(record));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            checkoutRecordArea.setText("");
        }
    }


    // Helper method to format the CheckoutRecord for display
    private String formatCheckoutRecord(CheckoutRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("Checkout Record for Member ID: ").append(record.getMember().getMemberId()).append("\n\n");

        record.getEntries().forEach(entry -> {
            sb.append("Book: ").append(entry.getBookCopy().getBook().getTitle()).append("\n")
              .append("ISBN: ").append(entry.getBookCopy().getBook().getIsbn()).append("\n")
              .append("Checkout Date: ").append(entry.getCheckoutDate()).append("\n")
              .append("Due Date: ").append(entry.getDueDate()).append("\n")
              .append("Copy Number: ").append(entry.getBookCopy().getCopyNum()).append("\n")
              .append("Overdue: ").append(entry.isOverdue() ? "Yes" : "No").append("\n")
              .append("--------------------------------------------\n");
        });

        return sb.toString();
    }
}
