import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Wishlist {
    private int wishlistID;
    private int clientID;
    private String createdDate;
    private List<WishlistItem> items;

    public Wishlist(int wishlistID, int clientID, String createdDate) {
        this.wishlistID = wishlistID;
        this.clientID = clientID;
        this.createdDate = createdDate;
        this.items = new ArrayList<>();
    }

    public int getWishlistID() {
        return wishlistID;
    }

    public int getClientID() {
        return clientID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Get all items in the wishlist
     * @return A copy of the items list to maintain encapsulation
     */
    public List<WishlistItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Add a product to the wishlist
     * @param product The product to add
     * @param quantity The quantity to add
     * @return true if added successfully, false otherwise
     */
    public boolean addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }
        
        // Check if product already exists in wishlist
        for (WishlistItem item : items) {
            if (item.getProductID().equals(product.getSku())) {
                item.updateQuantity(item.getQuantity() + quantity);
                return true;
            }
        }
        
        // If not, add as new item
        int newItemId = items.isEmpty() ? 1 : items.get(items.size() - 1).getWishlistItemID() + 1;
        items.add(new WishlistItem(newItemId, wishlistID, product.getSku(), quantity));
        return true;
    }

    /**
     * Remove an item from the wishlist
     * @param wishlistItemID ID of the item to remove
     * @return true if removed, false if not found
     */
    public boolean removeItem(int wishlistItemID) {
        return items.removeIf(item -> item.getWishlistItemID() == wishlistItemID);
    }

    /**
     * Get the number of items in the wishlist
     * @return count of items
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Get the total quantity of all items in the wishlist
     * @return total quantity
     */
    public int getTotalQuantity() {
        return items.stream()
                   .mapToInt(WishlistItem::getQuantity)
                   .sum();
    }

    /**
     * Check if the wishlist is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Clear all items from the wishlist
     */
    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return String.format("Wishlist #%d (Created: %s, Items: %d)", 
                           wishlistID, createdDate, items.size());
    }
}
