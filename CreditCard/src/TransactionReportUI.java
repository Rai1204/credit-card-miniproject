import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TransactionReportUI extends JFrame {
    private final JTextField customerIdField;
    private final JButton viewReportButton;
    private final JTable reportTable;
    private final DefaultTableModel tableModel;

    public TransactionReportUI() {
        setTitle("Transaction Report");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Customer ID"));

        customerIdField = new JTextField();
        viewReportButton = new JButton("View Report");

        formPanel.add(customerIdField);
        formPanel.add(viewReportButton);

        add(formPanel, BorderLayout.NORTH);

        // Table Model
        tableModel = new DefaultTableModel(new String[]{"Transaction ID", "Card ID", "Receiver ID", "Amount", "Date", "Status"}, 0);
        reportTable = new JTable(tableModel);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // Button Action
        viewReportButton.addActionListener(e -> loadReport());

        setVisible(true);
    }

    private void loadReport() {
        String input = customerIdField.getText().trim();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Customer ID cannot be empty.");
            return;
        }

        try {
            int customerId = Integer.parseInt(input);

            new SwingWorker<Void, Transaction>() {
                @Override
                protected Void doInBackground() throws Exception {
                    TransactionDAO transactionDAO = new TransactionDAO();
                    List<Transaction> transactions = transactionDAO.getTransactionsByCustomer(customerId);
                    tableModel.setRowCount(0); // Clear previous data

                    if (transactions.isEmpty()) {
                        JOptionPane.showMessageDialog(TransactionReportUI.this, "No transactions found for this customer.");
                        return null;
                    }

                    for (Transaction t : transactions) {
                        tableModel.addRow(new Object[]{
                            t.getTransactionId(),
                            t.getCardId(),
                            t.getReceiverId(),  // Added Receiver ID
                            t.getAmount(),
                            t.getTransactionDate(),
                            t.getStatus()
                        });
                    }
                    return null;
                }

                @Override
                protected void done() {
                    // You can update UI components if needed after background task
                }
            }.execute();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Invalid customer ID. Please enter a valid number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching transactions: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TransactionReportUI::new);
    }
}
