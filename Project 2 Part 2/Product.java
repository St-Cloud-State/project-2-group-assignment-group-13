import java.util.LinkedList;
import java.util.Queue;

public class Product {
    private String name;
    private String sku;
    private String description;
    private int quantity;
    private double defaultPrice;
    private Queue<WaitlistItem> waitlist;

    public Product(String name, String sku, String description, int quantity, double defaultPrice) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.quantity = quantity;
        this.defaultPrice = defaultPrice;
        this.waitlist = new LinkedList<>();
    }

    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public double getDefaultPrice() { return defaultPrice; }

    public String getDetails() {
        return "Product Name: " + name + ", SKU: " + sku + ", Description: " + description + ", Quantity: " + quantity + ", Price: $" + defaultPrice;
    }

    public void updateStock(int newQuantity) {
        this.quantity = newQuantity;
    }

    public void addToWaitlist(String clientId, int quantity, double price) {
        waitlist.add(new WaitlistItem(clientId, sku, quantity, price));
    }

    public Queue<WaitlistItem> getWaitlist() {
        return waitlist;
    }

    public boolean hasWaitlistItems() {
        return !waitlist.isEmpty();
    }

    public WaitlistItem getNextWaitlistItem() {
        return waitlist.peek();
    }

    public WaitlistItem removeNextWaitlistItem() {
        return waitlist.poll();
    }
}
