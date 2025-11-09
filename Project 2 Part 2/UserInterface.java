import java.util.*;
import java.io.*;

public class UserInterface {
    private static UserInterface instance;
    private Scanner scanner;
    private ProductCatalog productCatalog;
    private ClientDatabase clientDatabase;
    private Map<String, List<Invoice>> clientInvoices; // Track invoices by client ID

    private UserInterface() {
        scanner = new Scanner(System.in);
        productCatalog = new ProductCatalog();
        clientDatabase = ClientDatabase.instance();
        clientInvoices = new HashMap<>();
    }

    public static UserInterface instance() {
        if (instance == null) {
            instance = new UserInterface();
        }
        return instance;
    }

    public void process() {
        System.out.println("Warehouse Management System");
        System.out.println("==========================");
        
        // Load sample data for testing
        loadSampleData();
        
        int choice;
        do {
            displayMenu();
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createClient();
                    break;
                case 2:
                    displayAllClients();
                    break;
                case 3:
                    createProduct();
                    break;
                case 4:
                    displayAllProducts();
                    break;
                case 5:
                    addToWishlist();
                    break;
                case 6:
                    displayWishlist();
                    break;
                case 7:
                    placeOrder();
                    break;
                case 8:
                    recordPayment();
                    break;
                case 9:
                    receiveShipment();
                    break;
                case 10:
                    displayWaitlist();
                    break;
                case 11:
                    displayInvoices();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Create Client");
        System.out.println("2. Display All Clients");
        System.out.println("3. Create Product");
        System.out.println("4. Display All Products");
        System.out.println("5. Add to Wishlist");
        System.out.println("6. Display Wishlist");
        System.out.println("7. Place Order");
        System.out.println("8. Record Payment");
        System.out.println("9. Receive Shipment");
        System.out.println("10. Display Waitlist");
        System.out.println("11. Display Invoices");
        System.out.println("0. Exit");
    }

    private void loadSampleData() {
        // Create sample products file if it doesn't exist
        File productFile = new File("productcatalog.txt");
        if (!productFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(productFile))) {
                // Will be populated when user creates products
            } catch (IOException e) {
                System.out.println("Error creating product catalog file.");
            }
        }
        
        // Create sample waitlist file if it doesn't exist
        File waitlistFile = new File("waitlist.txt");
        if (!waitlistFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(waitlistFile))) {
                // Will be populated when waitlists are created
            } catch (IOException e) {
                System.out.println("Error creating waitlist file.");
            }
        }
    }

    private void createClient() {
        System.out.println("\n--- Create Client ---");
        System.out.print("Enter client name: ");
        String name = scanner.nextLine();
        System.out.print("Enter client address: ");
        String address = scanner.nextLine();
        System.out.print("Enter client phone: ");
        String phone = scanner.nextLine();
        
        Client client = new Client(name, address, phone);
        clientDatabase.addClient(client);
        System.out.println("Client created successfully with ID: " + client.getId());
    }

    private void displayAllClients() {
        System.out.println("\n--- All Clients ---");
        Iterator<Client> iterator = clientDatabase.getClients();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            System.out.println(client.toString());
        }
    }

    private void createProduct() {
        System.out.println("\n--- Create Product ---");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter SKU: ");
        String sku = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        int quantity = getIntInput("Enter quantity: ");
        double price = getDoubleInput("Enter price: ");
        
        Product product = new Product(name, sku, description, quantity, price);
        productCatalog.addProduct(product);
        System.out.println("Product created successfully.");
    }

    private void displayAllProducts() {
        System.out.println("\n--- All Products ---");
        productCatalog.getAllProductInfo(System.out);
    }

    private void addToWishlist() {
        System.out.println("\n--- Add to Wishlist ---");
        String clientId = getStringInput("Enter client ID (e.g., C1): ");
        Client client = clientDatabase.search(clientId);
        
        if (client == null) {
            System.out.println("Client not found.");
            return;
        }
        
        // For simplicity, we'll use a single wishlist per client
        // In a real system, clients might have multiple wishlists
        Wishlist wishlist = null;
        List<Wishlist> wishlists = client.getWishlists();
        if (wishlists.isEmpty()) {
            // Create a new wishlist
            int wishlistId = wishlists.size() + 1;
            wishlist = new Wishlist(wishlistId, Integer.parseInt(clientId.substring(1)), new Date().toString());
            client.addWishlist(wishlist);
        } else {
            // Use the first wishlist
            wishlist = wishlists.get(0);
        }
        
        String sku = getStringInput("Enter product SKU: ");
        Product product = productCatalog.getProductBySku(sku);
        
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        
        int quantity = getIntInput("Enter quantity: ");
        wishlist.addItem(product, quantity);
        System.out.println("Product added to wishlist successfully.");
    }

    private void displayWishlist() {
        System.out.println("\n--- Display Wishlist ---");
        String clientId = getStringInput("Enter client ID (e.g., C1): ");
        Client client = clientDatabase.search(clientId);
        
        if (client == null) {
            System.out.println("Client not found.");
            return;
        }
        
        List<Wishlist> wishlists = client.getWishlists();
        if (wishlists.isEmpty()) {
            System.out.println("No wishlists found for this client.");
            return;
        }
        
        // Display the first wishlist
        Wishlist wishlist = wishlists.get(0);
        System.out.println("Wishlist #" + wishlist.getWishlistID() + ":");
        for (WishlistItem item : wishlist.getItems()) {
            System.out.println("  " + item.toString());
        }
    }

    private void placeOrder() {
        System.out.println("\n--- Place Order ---");
        String clientId = getStringInput("Enter client ID (e.g., C1): ");
        Client client = clientDatabase.search(clientId);
        
        if (client == null) {
            System.out.println("Client not found.");
            return;
        }
        
        List<Wishlist> wishlists = client.getWishlists();
        if (wishlists.isEmpty()) {
            System.out.println("No wishlists found for this client.");
            return;
        }
        
        // Process the first wishlist
        Wishlist wishlist = wishlists.get(0);
        System.out.println("Processing order for wishlist #" + wishlist.getWishlistID());
        
        // Process each item in the wishlist
        List<WishlistItem> items = wishlist.getItems();
        List<WishlistItem> unfulfilledItems = new ArrayList<>();
        double totalCost = 0.0;
        
        for (WishlistItem item : items) {
            String sku = item.getProductID();
            int requestedQty = item.getQuantity();
            Product product = productCatalog.getProductBySku(sku);
            
            if (product == null) {
                System.out.println("Product " + sku + " not found.");
                unfulfilledItems.add(item);
                continue;
            }
            
            int availableQty = product.getQuantity();
            
            if (availableQty >= requestedQty) {
                // Sufficient stock available
                productCatalog.updateProductQuantity(sku, availableQty - requestedQty);
                
                // Calculate cost and update client balance
                double itemCost = requestedQty * product.getDefaultPrice();
                totalCost += itemCost;
                client.addBalance(-itemCost); // Negative because it's a debit
                
                // Generate invoice
                Invoice invoice = new Invoice(clientId, sku, requestedQty, product.getDefaultPrice());
                addInvoice(clientId, invoice);
                System.out.println("Order fulfilled: " + requestedQty + " units of " + sku + " for $" + String.format("%.2f", itemCost));
            } else {
                // Insufficient stock - add to waitlist
                System.out.println("Insufficient stock for " + sku + ". Adding to waitlist.");
                productCatalog.addToWaitlist(sku, clientId, requestedQty, product.getDefaultPrice());
                unfulfilledItems.add(item);
            }
        }
        
        // Update wishlist to remove fulfilled items
        wishlist.clear();
        for (WishlistItem item : unfulfilledItems) {
            Product product = productCatalog.getProductBySku(item.getProductID());
            if (product != null) {
                wishlist.addItem(product, item.getQuantity());
            }
        }
        
        if (totalCost > 0) {
            System.out.println("Total order cost: $" + String.format("%.2f", totalCost));
        }
        System.out.println("Order processing complete.");
    }

    private void recordPayment() {
        System.out.println("\n--- Record Payment ---");
        String clientId = getStringInput("Enter client ID (e.g., C1): ");
        Client client = clientDatabase.search(clientId);
        
        if (client == null) {
            System.out.println("Client not found.");
            return;
        }
        
        double amount = getDoubleInput("Enter payment amount: ");
        client.addBalance(amount); // Positive because it's a credit (payment received)
        System.out.println("Payment of $" + amount + " recorded for client " + clientId);
        System.out.println("New balance: $" + String.format("%.2f", client.getBalance()));
    }

    private void receiveShipment() {
        System.out.println("\n--- Receive Shipment ---");
        String sku = getStringInput("Enter product SKU: ");
        Product product = productCatalog.getProductBySku(sku);
        
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        
        int quantity = getIntInput("Enter shipment quantity: ");
        productCatalog.receiveShipment(sku, quantity);
        System.out.println("Shipment of " + quantity + " units of " + sku + " received.");
    }

    private void displayWaitlist() {
        System.out.println("\n--- Display Waitlist ---");
        String sku = getStringInput("Enter product SKU: ");
        productCatalog.displayWaitlist(sku);
    }

    private void displayInvoices() {
        System.out.println("\n--- Display Invoices ---");
        String clientId = getStringInput("Enter client ID (e.g., C1): ");
        
        List<Invoice> invoices = clientInvoices.get(clientId);
        if (invoices == null || invoices.isEmpty()) {
            System.out.println("No invoices found for client " + clientId);
            return;
        }
        
        System.out.println("Invoices for client " + clientId + ":");
        for (Invoice invoice : invoices) {
            System.out.println("  " + invoice.toString());
        }
    }

    // Helper methods
    private void addInvoice(String clientId, Invoice invoice) {
        clientInvoices.computeIfAbsent(clientId, k -> new ArrayList<>()).add(invoice);
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        UserInterface.instance().process();
    }
}
