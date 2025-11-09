import java.util.ArrayList;
import java.util.List;

public class Waitlist {
    private List<WishlistItem> waitlistedItems = new ArrayList<>();

    public void addToWaitlist(WishlistItem item) {
        waitlistedItems.add(item);
    }

    public void displayWaitlist() {
        System.out.println("---- Waitlist ----");
        for (WishlistItem item : waitlistedItems) {
            System.out.println("ProductID: " + item.getProductID() +
                               " | Quantity: " + item.getQuantity());
        }
    }

    public List<WishlistItem> getWaitlistedItems() {
        return waitlistedItems;
    }
}
