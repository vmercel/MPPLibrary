package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import dataaccess.DataAccessFacade;
import business.LibraryMember;
import business.Address;

public class AddLibraryMemberPanel extends JPanel {

    private JTextField memberIdField, firstNameField, lastNameField, phoneField, addressfield;
    private JButton addButton;

    public AddLibraryMemberPanel() {
        setLayout(new BorderLayout());
        
        // Center Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding around components
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Member ID:"), gbc);
        
        memberIdField = new JTextField(15);  // Set preferred width
        gbc.gridx = 1;
        inputPanel.add(memberIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("First Name:"), gbc);

        firstNameField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Last Name:"), gbc);

        lastNameField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Phone Number:"), gbc);

        phoneField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(phoneField, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add Member");
        addButton.addActionListener(this::addLibraryMember);
        buttonPanel.add(addButton);

        // Add panels to the main panel
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Logic to handle adding a library member
    private void addLibraryMember(ActionEvent e) {
        String memberId = memberIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        Address address = new Address("1000 N 4TH STR", "Fairfield","IA","52557");

        // Implement logic to add the library member using DataAccessFacade
        DataAccessFacade dataAccess = new DataAccessFacade();
        LibraryMember newMember = new LibraryMember(memberId, firstName, lastName, phone, address);
        dataAccess.saveNewMember(newMember);

        JOptionPane.showMessageDialog(this, "Member added successfully!");
        clearFields();
    }

    // Method to clear input fields after adding a member
    private void clearFields() {
        memberIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
    }
}
