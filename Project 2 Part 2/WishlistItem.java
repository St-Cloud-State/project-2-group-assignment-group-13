public class WishlistItem {
    private int wishlistItemID;
    private int wishlistID;
    private String productID;  // Changed from int to String to match SKU format
    private int quantity;

    public WishlistItem(int wishlistItemID, int wishlistID, String productID, int quantity) {
        this.wishlistItemID = wishlistItemID;
        this.wishlistID = wishlistID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public int getWishlistItemID() {
        return wishlistItemID;
    }

    public int getWishlistID() {
        return wishlistID;
    }

    public String getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return "ProductID: " + productID + ", Quantity: " + quantity;
    }
}
