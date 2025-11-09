public class WaitlistItem {
    private String clientId;
    private String productId;
    private int quantity;
    private double unitPrice;

    public WaitlistItem(String clientId, String productId, int quantity, double unitPrice) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getClientId() { return clientId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    @Override
    public String toString() {
        return "Client: " + clientId + ", Product: " + productId + 
               ", Quantity: " + quantity + ", Unit Price: $" + unitPrice;
    }
}
