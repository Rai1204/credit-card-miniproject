import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionUI extends JFrame {
    private final JTextField cardIdField;
    private final JTextField cvvField;
    private final JTextField expiryField;
    private final JTextField receiverIdField;
    private final JTextField amountField;
    private final JButton processPaymentButton;
    private final JButton viewTransactionsButton;
    private final JTable transactionTable;
    private final DefaultTableModel tableModel;
    private final int loggedInCustomerId;

    public TransactionUI(int customerId) {
        this.loggedInCustomerId = customerId;
        setTitle("Transaction Management");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Process Payment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Card ID & CVV
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Card ID:"), gbc);
        gbc.gridx = 1;
        cardIdField = new JTextField(10);
        formPanel.add(cardIdField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 3;
        cvvField = new JTextField(5);
        formPanel.add(cvvField, gbc);

        // Row 2: Expiry Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        expiryField = new JTextField(10);
        gbc.gridwidth = 3;
        formPanel.add(expiryField, gbc);
        gbc.gridwidth = 1; // Reset grid width

        // Row 3: Receiver ID & Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Receiver ID:"), gbc);
        gbc.gridx = 1;
        receiverIdField = new JTextField(10);
        formPanel.add(receiverIdField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 3;
        amountField = new JTextField(10);
        formPanel.add(amountField, gbc);

        // Row 4: Buttons
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        processPaymentButton = new JButton("Process Payment");
        formPanel.add(processPaymentButton, gbc);

        gbc.gridx = 2;
        viewTransactionsButton = new JButton("View Transactions");
        formPanel.add(viewTransactionsButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Transaction ID", "Card ID", "Receiver ID", "Amount", "Date", "Status"}, 0);
        transactionTable = new JTable(tableModel);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        // Button Actions
        processPaymentButton.addActionListener(e -> processPayment());
        viewTransactionsButton.addActionListener(e -> loadTransactions());

        setVisible(true);
    }

    private void processPayment() {
        try {
            int cardId = Integer.parseInt(cardIdField.getText().trim());
            int cvv = Integer.parseInt(cvvField.getText().trim());
            String expiryDate = expiryField.getText().trim();
            int receiverId = Integer.parseInt(receiverIdField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());

            TransactionDAO transactionDAO = new TransactionDAO();
            if (!transactionDAO.isCardOwnedByUser(cardId, loggedInCustomerId)) {
                JOptionPane.showMessageDialog(this, "Error: You can only use your own cards.");
                return;
            }

            boolean isValid = transactionDAO.validateCard(cardId, cvv, expiryDate);
            if (isValid) {
                transactionDAO.processTransaction(cardId, receiverId, amount);
                JOptionPane.showMessageDialog(this, "Payment processed successfully!");
                loadTransactions();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card details. Transaction failed.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter valid numeric values.");
        } catch (SQLException ex) {
            Logger.getLogger(TransactionUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTransactions() {
        SwingWorker<Void, Transaction> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<Transaction> transactions = new TransactionDAO().getTransactionsByCustomer(loggedInCustomerId);
                    tableModel.setRowCount(0);
                    for (Transaction t : transactions) {
                        tableModel.addRow(new Object[]{
                            t.getTransactionId(),
                            t.getCardId(),
                            t.getReceiverId(),
                            t.getAmount(),
                            t.getTransactionDate(),
                            t.getStatus()
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error fetching transactions: " + ex.getMessage());
                }
                return null;
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransactionUI(1)); // Example ID
    }
}
