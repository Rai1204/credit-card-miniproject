public class Transaction {
    private int transactionId;
    private int cardId;
    private int receiverId;
    private double amount;
    private String transactionDate;
    private String status;

    public Transaction(int transactionId, int cardId, int receiverId, double amount, String transactionDate, String status) {
        this.transactionId = transactionId;
        this.cardId = cardId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    public int getTransactionId() { return transactionId; }
    public int getCardId() { return cardId; }
    public int getReceiverId() { return receiverId; }
    public double getAmount() { return amount; }
    public String getTransactionDate() { return transactionDate; }
    public String getStatus() { return status; }
}
