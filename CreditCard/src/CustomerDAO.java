import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void addCustomer(Customer customer) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.executeUpdate();
        con.close();
    }

    public List<Customer> getAllCustomers() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customers";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            customers.add(new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
            ));
        }
        con.close();
        return customers;
    }
}
