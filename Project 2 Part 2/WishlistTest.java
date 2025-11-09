import java.util.ArrayList;
import java.util.List;

public class WishlistTest {
    public static void main(String[] args) {
        // Create a test client
        Client client = new Client("Test User", "123 Test St", "555-1234");
        
        // Create a wishlist for the client
        Wishlist wishlist = new Wishlist(1, Integer.parseInt(client.getId().substring(1)), "2025-09-27");
        
        // Create test products
        Product product1 = new Product("Test Product 1", "TP100", "Description 1", 10, 19.99);
        Product product2 = new Product("Test Product 2", "TP101", "Description 2", 5, 29.99);
        
        // Add items to wishlist
        System.out.println("Adding items to wishlist...");
        wishlist.addItem(product1, 2);
        wishlist.addItem(product2, 1);
        
        // Add the wishlist to the client
        client.addWishlist(wishlist);
        
        // Display wishlist contents
        System.out.println("\nWishlist Contents:");
        System.out.println("Wishlist ID: " + wishlist.getWishlistID());
        System.out.println("Client ID: " + wishlist.getClientID());
        System.out.println("Created: " + wishlist.getCreatedDate());
        
        System.out.println("\nItems in wishlist:");
        for (WishlistItem item : wishlist.getItems()) {
            System.out.println("- Product ID: " + item.getProductID() + 
                             ", Quantity: " + item.getQuantity());
        }
        
        System.out.println("\nTotal items in wishlist: " + wishlist.getItemCount());
        System.out.println("Total quantity of all items: " + wishlist.getTotalQuantity());
    }
}
