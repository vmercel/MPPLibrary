package librarysystem;

import business.ControllerInterface;
import business.LoginException;
import business.SystemController;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public static final LoginPanel INSTANCE = new LoginPanel();

    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;

    private ControllerInterface ci; // Controller for authentication
    private Runnable loginSuccessCallback; // Callback for successful login

    private LoginPanel() {
        ci = new SystemController();  // Initialize the controller
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        // Create the form panel using GridBagLayout for better layout control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        username = new JTextField(15);  // Set preferred width
        gbc.gridx = 1;
        formPanel.add(username, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        password = new JPasswordField(15);  // Set preferred width
        gbc.gridx = 1;
        formPanel.add(password, gbc);

        // Login Button
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Add listener to login button
        addLoginButtonListener();
    }

    // Method to add the login button listener
    private void addLoginButtonListener() {
        loginButton.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(password.getPassword());  // Convert char array to string

            try {
                // Validate login against real data using the controller
                ci.login(user, pass);
                
                // If login is successful
                JOptionPane.showMessageDialog(this, "Login Successful");
                
                // Invoke callback to enable functionalities
                if (loginSuccessCallback != null) {
                    loginSuccessCallback.run();  // Trigger the callback
                }
            } catch (LoginException ex) {
                // Handle failed login attempts
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Method to set the login success callback
    public void setLoginSuccessCallback(Runnable callback) {
        this.loginSuccessCallback = callback;
    }

    public JPanel getMainPanel() {
        return this;
    }
}
