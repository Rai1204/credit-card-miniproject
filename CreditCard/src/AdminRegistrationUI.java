import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminRegistrationUI extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JTextField securityKeyField;
    private final JButton registerButton;

    private static final String ADMIN_SECURITY_KEY = "admin12security"; // Default security key

    public AdminRegistrationUI() {
        setTitle("Admin Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Form Fields
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        add(new JLabel("Security Key:"));
        securityKeyField = new JTextField();
        add(securityKeyField);

        registerButton = new JButton("Register");
        add(new JLabel()); // Empty space
        add(registerButton);

        // Register Button Action
        registerButton.addActionListener(e -> registerAdmin());

        setVisible(true);
    }

    private void registerAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String securityKey = securityKeyField.getText().trim();

        // Validations
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || securityKey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!securityKey.equals(ADMIN_SECURITY_KEY)) {
            JOptionPane.showMessageDialog(this, "Invalid security key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Store in Database
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO admins (username, password) VALUES (?, ?)")) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Admin registered successfully!");
            dispose(); // Close registration window
            new OpeningPageUI(); // Navigate to empty opening page

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminRegistrationUI::new);
    }
}
