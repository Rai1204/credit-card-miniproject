public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;

    // ✅ Corrected Constructor
    public Customer(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // ✅ Corrected Getter Methods
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
