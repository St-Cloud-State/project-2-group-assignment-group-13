import java.util.ArrayList;
import java.util.List;

public class OrderRecord {
    private int orderID;
    private int clientID;
    private List<WishlistItem> orderedItems = new ArrayList<>();
    private double totalAmount;

    public OrderRecord(int orderID, int clientID) {
        this.orderID = orderID;
        this.clientID = clientID;
    }

    public void addOrderedItem(WishlistItem item, double price) {
        orderedItems.add(item);
        totalAmount += price * item.getQuantity();
    }

    public void displayOrder() {
        System.out.println("Order ID: " + orderID + " | Client ID: " + clientID);
        for (WishlistItem item : orderedItems) {
            System.out.println("  ProductID: " + item.getProductID() +
                               " | Qty: " + item.getQuantity());
        }
        System.out.println("Total Amount: $" + totalAmount);
    }
}
