import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Validate card details
    public boolean validateCard(int cardId, int cvv, String expiryDate) throws SQLException {
        String sql = "SELECT 1 FROM cards WHERE card_id = ? AND cvv = ? AND expiry_date = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setInt(2, cvv);
            ps.setString(3, expiryDate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if card details are valid
            }
        }
    }

    // Ensure the card belongs to the customer
    public boolean isCardOwnedByUser(int cardId, int customerId) throws SQLException {
        String sql = "SELECT 1 FROM cards WHERE card_id = ? AND customer_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setInt(2, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if the card belongs to the user
            }
        }
    }

    // Process transaction with Card Validation and Receiver Amount Update
    public boolean processTransaction(int cardId, int receiverId, double amount) throws SQLException {
        String insertTransactionSQL = "INSERT INTO transactions (card_id, receiver_id, amount, transaction_date, status) VALUES (?, ?, ?, NOW(), 'success')";
        String checkReceiverSQL = "SELECT amount FROM receivers WHERE receiver_id = ?";
        String updateReceiverSQL = "UPDATE receivers SET amount = amount + ? WHERE receiver_id = ?";
        String insertReceiverSQL = "INSERT INTO receivers (receiver_id, amount) VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false); // Start transaction
            
            try (PreparedStatement insertTransactionPS = con.prepareStatement(insertTransactionSQL)) {
                insertTransactionPS.setInt(1, cardId);
                insertTransactionPS.setInt(2, receiverId);
                insertTransactionPS.setDouble(3, amount);
                insertTransactionPS.executeUpdate();
            }

            boolean receiverExists;
            try (PreparedStatement checkReceiverPS = con.prepareStatement(checkReceiverSQL)) {
                checkReceiverPS.setInt(1, receiverId);
                try (ResultSet receiverRS = checkReceiverPS.executeQuery()) {
                    receiverExists = receiverRS.next();
                }
            }

            if (receiverExists) {
                try (PreparedStatement updateReceiverPS = con.prepareStatement(updateReceiverSQL)) {
                    updateReceiverPS.setDouble(1, amount);
                    updateReceiverPS.setInt(2, receiverId);
                    updateReceiverPS.executeUpdate();
                }
            } else {
                try (PreparedStatement insertReceiverPS = con.prepareStatement(insertReceiverSQL)) {
                    insertReceiverPS.setInt(1, receiverId);
                    insertReceiverPS.setDouble(2, amount);
                    insertReceiverPS.executeUpdate();
                }
            }

            con.commit(); // Commit transaction
            return true;
        } 
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactionList.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getInt("card_id"),
                    rs.getInt("receiver_id"),
                    rs.getDouble("amount"),
                    rs.getString("transaction_date"),
                    rs.getString("status")
                ));
            }
        }
        return transactionList;
    }

    // Get transactions by customer
    public List<Transaction> getTransactionsByCustomer(int customerId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
                     SELECT t.transaction_id, t.card_id, t.receiver_id, t.amount, t.transaction_date, t.status
                     FROM transactions t
                     JOIN cards c ON t.card_id = c.card_id
                     WHERE c.customer_id = ?""";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("card_id"),
                        rs.getInt("receiver_id"),
                        rs.getDouble("amount"),
                        rs.getString("transaction_date"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return transactions;
    }

    // Get transactions by card
    public List<Transaction> getTransactionsByCard(int cardId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE card_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("card_id"),
                        rs.getInt("receiver_id"),
                        rs.getDouble("amount"),
                        rs.getString("transaction_date"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return transactions;
    }
}
