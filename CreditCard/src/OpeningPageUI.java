import javax.swing.*;
import java.awt.*;

public class OpeningPageUI extends JFrame {
    private final JButton adminRegisterButton;
    private final JButton adminLoginButton;
    private final JButton userLoginButton;

    public OpeningPageUI() {
        setTitle("Opening Page");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        // Buttons
        adminRegisterButton = new JButton("Admin Registration");
        adminLoginButton = new JButton("Admin Login");
        userLoginButton = new JButton("User Login");

        // Add Buttons
        add(adminRegisterButton);
        add(adminLoginButton);
        add(userLoginButton);

        // Button Actions
        adminRegisterButton.addActionListener(e -> { // Close current window
            new AdminRegistrationUI();
        });

        adminLoginButton.addActionListener(e -> {
            new AdminLoginUI();
        });

        userLoginButton.addActionListener(e -> {
            new UserLoginUI();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OpeningPageUI::new);
    }
}
