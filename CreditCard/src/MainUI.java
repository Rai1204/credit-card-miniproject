import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainUI extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JButton addCustomerButton, viewCustomersButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public MainUI() {
        setTitle("Credit Card Processing System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Customer"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        addCustomerButton = new JButton("Add Customer");
        viewCustomersButton = new JButton("View Customers");

        formPanel.add(addCustomerButton);
        formPanel.add(viewCustomersButton);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone"}, 0);
        customerTable = new JTable(tableModel);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        addCustomerButton.addActionListener(e -> addCustomer());
        viewCustomersButton.addActionListener(e -> loadCustomers());

        setVisible(true);
    }

    private void addCustomer() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();

            Customer newCustomer = new Customer(0, name, email, phone);
            new CustomerDAO().addCustomer(newCustomer);

            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = new CustomerDAO().getAllCustomers();
            tableModel.setRowCount(0); // Clear previous data

            for (Customer c : customers) {
                tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getPhone()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}
