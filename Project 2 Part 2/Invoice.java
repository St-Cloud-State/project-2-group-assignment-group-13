import java.util.*;

/**
 * Represents a purchase or fulfilled waitlist transaction for a client.
 */
public class Invoice implements java.io.Serializable {
    private static int nextId = 1;
    private String invoiceId;
    private String clientId;
    private String productId;
    private int quantity;
    private double unitPrice;
    private double amount;
    private Date date;

    public Invoice(String clientId, String productId, int qty, double price) {
        this.invoiceId = "INV" + nextId++;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = qty;
        this.unitPrice = price;
        this.amount = qty * price;
        this.date = new Date();
    }

    public double getAmount() { return amount; }
    public String getClientId() { return clientId; }
    public String getProductId() { return productId; }
    public String getInvoiceId() { return invoiceId; }

    @Override
    public String toString() {
        return invoiceId + " | Client: " + clientId +
               " | Product: " + productId +
               " | Qty: " + quantity +
               " | Unit: $" + unitPrice +
               " | Total: $" + amount +
               " | " + date;
    }
}
