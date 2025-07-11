import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminLoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public AdminLoginUI() {
        setTitle("Admin Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(new JLabel()); // Empty space
        add(loginButton);

        loginButton.addActionListener(e -> login());

        setVisible(true);
    }

 private void login() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    try {
        AdminDAO adminDAO = new AdminDAO();
        if (adminDAO.validateAdmin(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new AdminDashboardUI(); // Navigate to Admin Dashboard instead of MainUI
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!");
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginUI::new);
    }
}
