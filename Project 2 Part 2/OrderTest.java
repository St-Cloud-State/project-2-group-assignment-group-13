import java.util.*;

public class OrderTest {
    public static void main(String[] args) {
        // create wishlist
        Wishlist wishlist = new Wishlist(1, 290, "2025-10-19");
        List<WishlistItem> itemsToAdd = new ArrayList<>();
        itemsToAdd.add(new WishlistItem(1, 1, 100, 3)); // product 100 (stock 2)
        itemsToAdd.add(new WishlistItem(2, 1, 101, 1)); // product 101 (stock 5)
        wishlist.addProducts(itemsToAdd);

        // create fake product stocks
        Map<Integer, Integer> stockMap = new HashMap<>();
        stockMap.put(100, 2);  
        stockMap.put(101, 5); 

        // Create OrderRecord and Waitlist
        OrderRecord order = new OrderRecord(1, 290);
        Waitlist waitlist = new Waitlist();

        // processOrder logic
        for (WishlistItem item : wishlist.getItems()) {
            int productID = item.getProductID();
            int qtyRequested = item.getQuantity();
            int stock = stockMap.getOrDefault(productID, 0);

            if (stock >= qtyRequested) {
                order.addOrderedItem(item, 10.0); 
                stockMap.put(productID, stock - qtyRequested);
            } else if (stock > 0) {
                order.addOrderedItem(new WishlistItem(999, 1, productID, stock), 10.0);
                waitlist.addToWaitlist(new WishlistItem(1000, 1, productID, qtyRequested - stock));
                stockMap.put(productID, 0);
            } else {
                waitlist.addToWaitlist(item);
            }
        }

        // Display results
        order.displayOrder();
        waitlist.displayWaitlist();
    }
}
