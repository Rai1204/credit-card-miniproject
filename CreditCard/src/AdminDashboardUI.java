import javax.swing.*;
import java.awt.*;

public class AdminDashboardUI extends JFrame {
    public AdminDashboardUI() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton mainUIButton = new JButton("Add Customer");
        JButton addCardButton = new JButton("Add Card");
        JButton transactionReportButton = new JButton("Transaction Reports");

        add(mainUIButton);
        add(addCardButton);
        add(transactionReportButton);

        // Button Actions
        mainUIButton.addActionListener(e -> new MainUI());
        addCardButton.addActionListener(e -> new CardUI());
        transactionReportButton.addActionListener(e -> new TransactionReportUI());

        setVisible(true);
    }
}
