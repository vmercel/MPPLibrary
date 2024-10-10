package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

public class DisplayAllMemberIDsPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea memberInfoArea;
    private JTextField searchField;  // Search bar to search for member IDs
    private JButton searchButton;
    private ControllerInterface ci;
    private JList<String> memberIdsList;
    private DefaultListModel<String> listModel;  // Model for the member IDs list

    public DisplayAllMemberIDsPanel() {
        // Set layout as CardLayout for switching between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize the controller interface to fetch data
        ci = new SystemController();

        // Panel to display list of member IDs with a search bar
        JPanel memberListPanel = new JPanel(new BorderLayout());

        // Search bar components
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Member ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add search panel to the top
        memberListPanel.add(searchPanel, BorderLayout.NORTH);

        // List to display all member IDs, and making it clickable
        listModel = new DefaultListModel<>();
        memberIdsList = new JList<>(listModel);
        memberIdsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberIdsList.setBackground(Color.WHITE);
        memberIdsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Detect double click on list item
                    String selectedMemberId = memberIdsList.getSelectedValue();
                    if (selectedMemberId != null) {
                        displayMemberInfo(selectedMemberId);  // Display member info on double click
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(memberIdsList);
        memberListPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button to return to the member list
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "memberList"));

        // Panel to display detailed member info
        JPanel memberInfoPanel = new JPanel(new BorderLayout());
        memberInfoArea = new JTextArea(15, 40);
        memberInfoArea.setEditable(false);
        memberInfoArea.setBackground(Color.WHITE); // Set background to white
        JScrollPane infoScrollPane = new JScrollPane(memberInfoArea);
        memberInfoPanel.add(infoScrollPane, BorderLayout.CENTER);
        memberInfoPanel.add(backButton, BorderLayout.SOUTH);

        // Add both panels to the card layout
        cardPanel.add(memberListPanel, "memberList");
        cardPanel.add(memberInfoPanel, "memberInfo");

        // Add cardPanel to the main layout
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        // Fetch and display all member IDs
        displayAllMemberIds();

        // Add action listener for search button
        searchButton.addActionListener(e -> searchMemberId());
    }

    // Method to fetch and display all member IDs
    private void displayAllMemberIds() {
        List<String> memberIds = ci.allMemberIds();  // Fetch member IDs from controller
        listModel.clear();  // Clear previous entries
        for (String id : memberIds) {
            listModel.addElement(id);  // Add each member ID to the list
        }
    }

    // Method to search for a specific member ID
    private void searchMemberId() {
        String searchId = searchField.getText().trim();

        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Member ID to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetch member info for the entered ID
        LibraryMember member = ci.getLibraryMemberById(searchId);

        if (member != null) {
            displayMemberInfo(searchId);  // Show member info if found
        } else {
            JOptionPane.showMessageDialog(this, "No member found with ID: " + searchId, "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Method to display member information when a member ID is clicked or found by search
    private void displayMemberInfo(String memberId) {
        LibraryMember member = ci.getLibraryMemberById(memberId);
        if (member != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Member ID: ").append(member.getMemberId()).append("\n");
            sb.append("First Name: ").append(member.getFirstName()).append("\n");
            sb.append("Last Name: ").append(member.getLastName()).append("\n");
            sb.append("Phone Number: ").append(member.getTelephone()).append("\n");
            sb.append("Address: ").append(member.getAddress()).append("\n");
            memberInfoArea.setText(sb.toString());

            // Switch to the member info panel
            cardLayout.show(cardPanel, "memberInfo");
        } else {
            JOptionPane.showMessageDialog(this, "Member information could not be retrieved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
