public class Card {
    private int cardId;
    private int customerId;
    private String cardHolder;
    private String cardNumber;
    private String expiryDate;
    private int cvv;
    private double balance;
    private String status;

    // Constructor
    public Card(int cardId, String cardHolder, String cardNumber, String expiryDate, int cvv, int customerId, double balance, String status) {
        this.cardId = cardId;
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.customerId = customerId;
        this.balance = balance;
        this.status = status;
    }

    // ✅ Corrected Getters
    public int getCardId() { return cardId; }
    public int getCustomerId() { return customerId; }
    public String getCardHolder() { return cardHolder; }
    public String getCardNumber() { return cardNumber; }
    public String getExpiryDate() { return expiryDate; }
    public int getCvv() { return cvv; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
}
