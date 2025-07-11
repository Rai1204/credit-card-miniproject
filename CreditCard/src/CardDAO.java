import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    public boolean addCard(Card card) throws SQLException {
        String sql = "INSERT INTO cards (card_number, card_holder, expiry_date, cvv, balance, customer_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, card.getCardNumber());
            ps.setString(2, card.getCardHolder());
            ps.setString(3, card.getExpiryDate());
            ps.setInt(4, card.getCvv());
            ps.setDouble(5, card.getBalance());
            ps.setInt(6, card.getCustomerId());
            ps.setString(7, card.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Card> getAllCards() throws SQLException {
        List<Card> cardList = new ArrayList<>();
        String sql = "SELECT * FROM cards";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cardList.add(new Card(
                    rs.getInt("card_id"),
                    rs.getString("card_holder"),
                    rs.getString("card_number"),
                    rs.getString("expiry_date"),
                    rs.getInt("cvv"),
                    rs.getInt("customer_id"),
                    rs.getDouble("balance"),
                    rs.getString("status")
                ));
            }
        }
        return cardList;
    }

    public boolean updateCardBalance(String cardNumber, double newBalance) throws SQLException {
        String sql = "UPDATE cards SET balance = ? WHERE card_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, cardNumber);
            return ps.executeUpdate() > 0;
        }
    }

    public void blockCard(int cardId) throws SQLException {
        String sql = "UPDATE cards SET status = 'blocked' WHERE card_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();
        }
    }
}
