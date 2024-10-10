package librarysystem;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import business.ControllerInterface;
import business.SystemController;

public class CheckInPanel extends JPanel {
    private JTextField memberIdField;
    private JTextField isbnField;
    private JTextField copyNumField;
    private JTextArea messageArea;
    private ControllerInterface ci;

    public CheckInPanel() {
        ci = new SystemController();  // Initialize the controller

        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Member ID input
        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdField = new JTextField(15);
        inputPanel.add(memberIdLabel);
        inputPanel.add(memberIdField);

        // ISBN input
        JLabel isbnLabel = new JLabel("Book ISBN:");
        isbnField = new JTextField(15);
        inputPanel.add(isbnLabel);
        inputPanel.add(isbnField);

        // Copy Number input
        JLabel copyNumLabel = new JLabel("Book Copy Number:");
        copyNumField = new JTextField(15);
        inputPanel.add(copyNumLabel);
        inputPanel.add(copyNumField);

        // Button for check-in action
        JButton checkInButton = new JButton("Check In");
        checkInButton.addActionListener(new CheckInActionListener());
        inputPanel.add(new JLabel());  // Empty label for spacing
        inputPanel.add(checkInButton);

        // Message area to display status
        messageArea = new JTextArea(5, 20);
        messageArea.setEditable(false);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        // Add the panels to the main layout
        add(inputPanel, BorderLayout.NORTH);
        add(messageScroll, BorderLayout.CENTER);
    }

    // Action listener for check-in process
    private class CheckInActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String memberId = memberIdField.getText();
            String isbn = isbnField.getText();
            String copyNumStr = copyNumField.getText();

            if (memberId.isEmpty() || isbn.isEmpty() || copyNumStr.isEmpty()) {
                messageArea.setText("All fields must be filled.");
                return;
            }

            try {
                int copyNum = Integer.parseInt(copyNumStr);
                ci.checkInBook(memberId, isbn, copyNum);
                messageArea.setText("Book copy successfully checked in!");
            } catch (NumberFormatException ex) {
                messageArea.setText("Invalid input for copy number.");
            } catch (Exception ex) {
                messageArea.setText("Error during check-in: " + ex.getMessage());
            }
        }
    }
}
