import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CardUI extends JFrame {
    private JTextField customerIdField, cardHolderField, cardNumberField, expiryDateField, cvvField, balanceField;
    private JButton addCardButton, viewCardsButton, blockCardButton;
    private JTable cardTable;
    private DefaultTableModel tableModel;

    public CardUI() {
        setTitle("Card Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Card"));

        formPanel.add(new JLabel("Customer ID:"));
        customerIdField = new JTextField();
        formPanel.add(customerIdField);

        formPanel.add(new JLabel("Card Holder:"));
        cardHolderField = new JTextField();
        formPanel.add(cardHolderField);

        formPanel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField();
        formPanel.add(cardNumberField);

        formPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        expiryDateField = new JTextField();
        formPanel.add(expiryDateField);

        formPanel.add(new JLabel("CVV:"));
        cvvField = new JTextField();
        formPanel.add(cvvField);

        formPanel.add(new JLabel("Initial Balance:"));
        balanceField = new JTextField();
        formPanel.add(balanceField);

        addCardButton = new JButton("Add Card");
        viewCardsButton = new JButton("View Cards");
        blockCardButton = new JButton("Block Selected Card");

        formPanel.add(addCardButton);
        formPanel.add(viewCardsButton);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Card ID", "Customer ID", "Card Holder", "Card Number", "Expiry Date", "Balance", "Status"}, 0);
        cardTable = new JTable(tableModel);
        add(new JScrollPane(cardTable), BorderLayout.CENTER);

        add(blockCardButton, BorderLayout.SOUTH);

        // Button Actions
        addCardButton.addActionListener(e -> addCard());
        viewCardsButton.addActionListener(e -> loadCards());
        blockCardButton.addActionListener(e -> blockCard());

        setVisible(true);
    }

    private void addCard() {
        try {
            int customerId = Integer.parseInt(customerIdField.getText());
            String cardHolder = cardHolderField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            int cvv = Integer.parseInt(cvvField.getText());
            double balance = Double.parseDouble(balanceField.getText());

            Card newCard = new Card(0, cardHolder, cardNumber, expiryDate, cvv, customerId, balance, "active");
            new CardDAO().addCard(newCard);

            JOptionPane.showMessageDialog(this, "Card added successfully!");
            customerIdField.setText("");
            cardHolderField.setText("");
            cardNumberField.setText("");
            expiryDateField.setText("");
            cvvField.setText("");
            balanceField.setText("");
            loadCards();    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Customer ID " );
        }
    }

    private void loadCards() {
        try {
            List<Card> cards = new CardDAO().getAllCards();
            tableModel.setRowCount(0); // Clear previous data

            for (Card c : cards) {
                tableModel.addRow(new Object[]{c.getCardId(), c.getCustomerId(), c.getCardHolder(), c.getCardNumber(), c.getExpiryDate(), c.getBalance(), c.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading cards: " + e.getMessage());
        }
    }

    private void blockCard() {
        int selectedRow = cardTable.getSelectedRow();
        if (selectedRow != -1) {
            int cardId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                new CardDAO().blockCard(cardId);
                JOptionPane.showMessageDialog(this, "Card blocked successfully!");
                loadCards(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a card to block!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CardUI::new);
    }
}
