import java.io.*;
import java.util.*;

/**
 * Client class
 * Represents a single client in the system
 */
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    private String phone;
    private String id;
    private List<Wishlist> wishlists;
    private double balance; // Positive = credit, Negative = debit

    // When we make a new client, we auto-generate an ID like C1, C2, etc.
    public Client(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.id = "C" + ClientIdServer.instance().getId(); // unique ID
        this.wishlists = new ArrayList<>();
        this.balance = 0.0;
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getId() { return id; }
    public double getBalance() { return balance; }

    // Setters
    public void setBalance(double balance) { this.balance = balance; }
    public void addBalance(double amount) { this.balance += amount; }

    // Nicely formatted string so printing a Client shows details
    // Wishlist management methods
    public void addWishlist(Wishlist wishlist) {
        if (wishlist != null) {
            wishlists.add(wishlist);
        }
    }
    
    public boolean removeWishlist(int wishlistID) {
        return wishlists.removeIf(w -> w.getWishlistID() == wishlistID);
    }
    
    public List<Wishlist> getWishlists() {
        return new ArrayList<>(wishlists); // Return a copy to maintain encapsulation
    }
    
    public Wishlist getWishlist(int wishlistID) {
        return wishlists.stream()
                       .filter(w -> w.getWishlistID() == wishlistID)
                       .findFirst()
                       .orElse(null);
    }
    
    @Override
    public String toString() {
        return "Client: " + name + " (ID: " + id + "), " + address + ", " + phone + 
               "\nBalance: $" + String.format("%.2f", balance) +
               "\nWishlists: " + wishlists.size();
    }
}
