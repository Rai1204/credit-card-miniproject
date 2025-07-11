import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to validate user login and return the customer ID
    public Integer validateUser(String username, String password) throws SQLException {
    String sql = "SELECT id FROM customers WHERE name = ? AND phone = ?";
    
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, password);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id"); // Return valid customer ID
            }
        }
    }
    return -1; // Return -1 for invalid credentials instead of null
}


    // Method to check if a card belongs to a customer
    public boolean isCardOwnedByUser(int cardId, int customerId) throws SQLException {
        String sql = "SELECT * FROM cards WHERE card_id = ? AND customer_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cardId);
            ps.setInt(2, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Return true if the card belongs to the user
            }
        }
    }
}
