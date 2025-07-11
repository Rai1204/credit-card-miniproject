import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UserLoginUI extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public UserLoginUI() {
        setTitle("User Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        // Labels and fields
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(new JLabel()); // Empty space
        add(loginButton);

        // Action Listener for login
        loginButton.addActionListener(e -> validateUser());

        setVisible(true);
    }

 private void validateUser() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Both fields are required!", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        UserDAO userDAO = new UserDAO(); 
        Integer customerId = userDAO.validateUser(username, password); // Ensure it returns an Integer
        
        if (customerId != null && customerId != -1) { // Valid user
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new TransactionUI(customerId);
            dispose();
        } else { // Handle incorrect credentials
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserLoginUI::new);
    }
}
