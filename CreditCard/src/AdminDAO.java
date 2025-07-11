import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    public boolean validateAdmin(String username, String password) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        boolean isValid = rs.next(); // If there's a match, return true
        con.close();
        return isValid;
    }
}
