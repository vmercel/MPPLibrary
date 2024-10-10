package librarysystem;

import java.awt.*;
import javax.swing.*;
import business.ControllerInterface;
import business.SystemController;
import java.util.*;

public class LibrarySystem extends JFrame implements LibWindow {
    private static final long serialVersionUID = 1L;

    ControllerInterface ci = new SystemController();
    public final static LibrarySystem INSTANCE = new LibrarySystem();

    // UI Components
    JPanel cards; // Card layout to switch between panels
    JList<String> navigationList; // Navigation list for different actions
    JTextArea statusBar = new JTextArea("Welcome to the Library System!");

    // Panels for different functionalities
    LoginPanel loginPanel;
    AddLibraryMemberPanel addLibraryMemberPanel;
    CheckoutBookPanel checkoutBookPanel;
    AddBookCopyPanel addBookCopyPanel;
    AddNewBookPanel addNewBookPanel;
    ViewCheckoutRecordPanel viewCheckoutRecordPanel;
    CheckOverduePanel checkOverduePanel;
    JPanel homePanel;

    // List items for navigation
    DefaultListModel<String> listModel;
    String[] menuItems = {
        "Home", 
        "Login", 
        "Logout", 
        "Add Library Member", 
        "Checkout Book", 
        "Check In Book",  // New option for Check In
        "Add Copy of Existing Book", 
        "Add New Book", 
        "View Checkout Record", 
        "Check Overdue Books",
        "Display All Book IDs",  // New
        "Display All Member IDs"  // New
    };

    // Store enabled status for each menu item
    Map<String, Boolean> itemEnabledStatus = new HashMap<>();

    private boolean isInitialized = false;

    private LibrarySystem() {}

    // Initialize the UI
    public void init() {
        setTitle("Library System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createPanels();
        createNavigationList();

        // Split pane to separate navigation and content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(navigationList), cards);
        splitPane.setDividerLocation(150); // Set size for navigation pane

        // Adding a status bar at the bottom
        JSplitPane outerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane, statusBar);
        outerSplitPane.setDividerLocation(500);

        add(outerSplitPane, BorderLayout.CENTER);

        isInitialized = true;
    }

    // Create the main panels for each functionality
    private void createPanels() {
        cards = new JPanel(new CardLayout());

        // Create a custom panel for the home screen with a background image
        homePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon(getClass().getResource("library.jpg"));
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        homePanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Library System!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        homePanel.add(welcomeLabel, BorderLayout.CENTER);
        cards.add(homePanel, "Home");

        loginPanel = LoginPanel.INSTANCE;
        cards.add(loginPanel.getMainPanel(), "Login");

        addLibraryMemberPanel = new AddLibraryMemberPanel();
        cards.add(addLibraryMemberPanel, "Add Library Member");

        checkoutBookPanel = new CheckoutBookPanel();
        cards.add(checkoutBookPanel, "Checkout Book");

        addBookCopyPanel = new AddBookCopyPanel();
        cards.add(addBookCopyPanel, "Add Copy of Existing Book");

        addNewBookPanel = new AddNewBookPanel();
        cards.add(addNewBookPanel, "Add New Book");

        viewCheckoutRecordPanel = new ViewCheckoutRecordPanel();
        cards.add(viewCheckoutRecordPanel, "View Checkout Record");

        checkOverduePanel = new CheckOverduePanel();
        cards.add(checkOverduePanel, "Check Overdue Books");

        // Add the new panels for displaying book and member IDs
        DisplayAllBookIDsPanel displayAllBookIDsPanel = new DisplayAllBookIDsPanel();
        cards.add(displayAllBookIDsPanel, "Display All Book IDs");

        DisplayAllMemberIDsPanel displayAllMemberIDsPanel = new DisplayAllMemberIDsPanel();
        cards.add(displayAllMemberIDsPanel, "Display All Member IDs");

        // Add the new Check In panel
        CheckInPanel checkInPanel = new CheckInPanel();
        cards.add(checkInPanel, "Check In Book");

        // Handle button visibility on login/logout
        loginPanel.setLoginSuccessCallback(() -> enableFunctionalities(true));
    }

    // Create navigation list with action listeners
    private void createNavigationList() {
        listModel = new DefaultListModel<>();
        for (String menuItem : menuItems) {
            listModel.addElement(menuItem);
            itemEnabledStatus.put(menuItem, menuItem.equals("Home") || menuItem.equals("Login")); // Initially, only "Home" and "Login" are enabled
        }

        navigationList = new JList<>(listModel);
        navigationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        navigationList.setCellRenderer(new MenuItemRenderer()); // Use custom renderer to show disabled state

        navigationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = navigationList.getSelectedValue();
                if (!itemEnabledStatus.get(selectedValue)) {
                    navigationList.clearSelection();
                    return;
                }

                if ("Logout".equals(selectedValue)) {
                    // Handle logout
                    ci.logout();
                    enableFunctionalities(false);  // Disable all functionalities except "Login"
                    CardLayout cl = (CardLayout) (cards.getLayout());
                    cl.show(cards, "Home");  // Show the home panel
                    statusBar.setText("Logged out. Welcome to the Library System.");
                } else {
                    CardLayout cl = (CardLayout) (cards.getLayout());
                    cl.show(cards, selectedValue);
                }
            }
        });
    }

    // Enable or disable menu functionalities based on login status
    public void enableFunctionalities(boolean enable) {
        String[] enabledItems = {
            "Add Library Member", 
            "Checkout Book", 
            "Add Copy of Existing Book", 
            "Add New Book", 
            "Check In Book",
            "View Checkout Record", 
            "Check Overdue Books", 
            "Logout",
            "Display All Book IDs",       // Enable "Display All Book IDs" after login
            "Display All Member IDs"      // Enable "Display All Member IDs" after login
        };

        String[] disabledItems = { "Login" };

        // Enable or disable all functionalities after login/logout
        for (String item : enabledItems) {
            itemEnabledStatus.put(item, enable);
        }

        // Disable items that should be shown only when logged out
        for (String item : disabledItems) {
            itemEnabledStatus.put(item, !enable);
        }

        navigationList.repaint();  // Repaint to reflect the changes in UI
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibrarySystem librarySystem = LibrarySystem.INSTANCE;
            librarySystem.init();
            librarySystem.setVisible(true);
        });
    }

    // Custom renderer to show disabled items as grayed out
    class MenuItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String menuItem = (String) value;

            // Set gray color for disabled items
            if (!itemEnabledStatus.get(menuItem)) {
                c.setForeground(Color.GRAY);
            } else if (isSelected) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.BLUE);
            }

            return c;
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }
}
