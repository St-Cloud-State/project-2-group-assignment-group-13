import java.util.*;

public class ClientMenuState implements State {

    @Override
    public void handle(Context context) {
        Scanner sc = context.getScanner();
        int clientID = context.getClientID();
        Client client = context.getClientDatabase().search("C" + clientID);
        if (client == null) {
            System.out.println("Client not found.");
            context.setState(new OpeningState());
            return;
        }

        System.out.println("\n--- Client Menu (ClientID: C" + clientID + ") ---");
        System.out.println("1. Show client details");
        System.out.println("2. Show products");
        System.out.println("3. Show invoices");
        System.out.println("4. Add to wishlist");
        System.out.println("5. Display wishlist");
        System.out.println("6. Place an order");
        System.out.println("7. Logout");
        System.out.print("Select option: ");

        int choice;
        try { choice = Integer.parseInt(sc.nextLine()); } 
        catch (NumberFormatException e) { 
            System.out.println("Invalid input."); 
            return; 
        }

        switch (choice) {
            case 1:
                System.out.println(client);
                break;
            case 2:
                context.getProductCatalog().getAllProductInfo(System.out);
                break;
            case 3:
                List<Invoice> invoices = context.getClientInvoices().get("C" + clientID);
                if (invoices != null && !invoices.isEmpty()) {
                    invoices.forEach(System.out::println);
                } else System.out.println("No invoices found.");
                break;
            case 4:
                addToWishlist(context, client);
                break;
            case 5:
                displayWishlist(client);
                break;
            case 6:
                placeOrder(context, client);
                break;
            case 7:
                if (context.isStartedFromClerk()) context.setState(new ClerkMenuState());
                else context.setState(new OpeningState());
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void addToWishlist(Context context, Client client) {
        Scanner sc = context.getScanner();
        List<Wishlist> wishlists = client.getWishlists();
        Wishlist wishlist;
        if (wishlists.isEmpty()) {
            wishlist = new Wishlist(1, Integer.parseInt(client.getId().substring(1)), new Date().toString());
            client.addWishlist(wishlist);
        } else {
            wishlist = wishlists.get(0);
        }
        System.out.print("Enter product SKU: ");
        String sku = sc.nextLine();
        Product product = context.getProductCatalog().getProductBySku(sku);
        if (product == null) { System.out.println("Product not found."); return; }
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        wishlist.addItem(product, qty);
        System.out.println("Product added to wishlist.");
    }

    private void displayWishlist(Client client) {
        List<Wishlist> wishlists = client.getWishlists();
        if (wishlists.isEmpty()) { System.out.println("No wishlists."); return; }
        Wishlist wishlist = wishlists.get(0);
        System.out.println("Wishlist #" + wishlist.getWishlistID() + ":");
        for (WishlistItem item : wishlist.getItems()) System.out.println("  " + item);
    }

    private void placeOrder(Context context, Client client) {
        List<Wishlist> wishlists = client.getWishlists();
        if (wishlists.isEmpty()) { System.out.println("No wishlists."); return; }
        Wishlist wishlist = wishlists.get(0);
        List<WishlistItem> items = wishlist.getItems();
        List<WishlistItem> unfulfilled = new ArrayList<>();
        double totalCost = 0.0;

        for (WishlistItem item : items) {
            Product product = context.getProductCatalog().getProductBySku(item.getProductID());
            if (product == null) { unfulfilled.add(item); continue; }

            int availableQty = product.getQuantity();
            int requestedQty = item.getQuantity();

            if (availableQty >= requestedQty) {
                product.updateStock(availableQty - requestedQty);
                double cost = requestedQty * product.getDefaultPrice();
                totalCost += cost;
                client.addBalance(-cost);
                Invoice invoice = new Invoice(client.getId(), product.getSku(), requestedQty, product.getDefaultPrice());
                context.getClientInvoices().computeIfAbsent(client.getId(), k -> new ArrayList<>()).add(invoice);
                System.out.println("Order fulfilled: " + requestedQty + " of " + product.getSku());
            } else {
                System.out.println("Insufficient stock for " + product.getSku() + ". Added to waitlist.");
                context.getProductCatalog().addToWaitlist(product.getSku(), client.getId(), requestedQty, product.getDefaultPrice());
                unfulfilled.add(item);
            }
        }

        wishlist.clear();
        for (WishlistItem item : unfulfilled) {
            Product product = context.getProductCatalog().getProductBySku(item.getProductID());
            if (product != null) wishlist.addItem(product, item.getQuantity());
        }

        if (totalCost > 0) System.out.println("Total order cost: $" + String.format("%.2f", totalCost));
        System.out.println("Order processing complete.");
    }
}
