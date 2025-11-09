import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class ProductCatalog {
    private LinkedList<Product> list;
    private final String PRODUCT_CATALOG_FILE = "productcatalog.txt";
    private final String WAITLIST_FILE = "waitlist.txt";

    public ProductCatalog() {
        this.list = new LinkedList<>();
        loadProducts();
    }

    private void loadProducts() {
        File file = new File(PRODUCT_CATALOG_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String name = data[0];
                    String sku = data[1];
                    String description = data[2];
                    int quantity = Integer.parseInt(data[3]);
                    double price = Double.parseDouble(data[4]);
                    Product product = new Product(name, sku, description, quantity, price);
                    list.add(product);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    private void saveProducts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCT_CATALOG_FILE))) {
            for (Product product : list) {
                writer.println(product.getName() + "," + product.getSku() + "," + 
                              product.getDescription() + "," + product.getQuantity() + "," + 
                              product.getDefaultPrice());
            }
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    public void getAllProductInfo(PrintStream outputStream) {
        for (Product product : list) {
            outputStream.println(product.getDetails());
        }
    }

    public Product getProduct(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    public Product getProductBySku(String sku) {
        int index = search(sku);
        if (index != -1) return list.get(index);
        return null;
    }

    public int search(String sku) {
        for (int x = 0; x < list.size(); x++) {
            if (list.get(x).getSku().equals(sku)) return x;
        }
        return -1;
    }

    public String getProductDetails(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index).getDetails();
        }
        return "Product not found.";
    }

    public void addProduct(Product product) {
        list.add(product);
        saveProducts();
    }

    public boolean updateProductQuantity(String sku, int newQuantity) {
        int index = search(sku);
        if (index != -1) {
            Product product = list.get(index);
            int oldQty = product.getQuantity();
            product.updateStock(newQuantity);
            saveProducts();
            
            // If stock was zero and now is positive, fulfill waitlist
            if (oldQty == 0 && newQuantity > 0) {
                fulfillWaitlist(sku);
            }
            
            return true;
        }
        return false;
    }

    public void addToWaitlist(String sku, String clientId, int quantity, double price) {
        Product product = getProductBySku(sku);
        if (product != null) {
            product.addToWaitlist(clientId, quantity, price);
            // Also save to file for persistence
            try (PrintWriter writer = new PrintWriter(new FileWriter(WAITLIST_FILE, true))) {
                writer.println(sku + "," + clientId + "," + quantity + "," + price);
            } catch (IOException e) {
                System.out.println("Error adding to waitlist file: " + e.getMessage());
            }
            System.out.println("Added to waitlist: Client " + clientId + ", Product " + sku + ", Quantity " + quantity);
        } else {
            System.out.println("Product not found.");
        }
    }

    public void displayWaitlist(String sku) {
        Product product = getProductBySku(sku);
        if (product != null) {
            Queue<WaitlistItem> waitlist = product.getWaitlist();
            if (waitlist.isEmpty()) {
                System.out.println("No items in waitlist for product " + sku);
            } else {
                System.out.println("Waitlist for product " + sku + ":");
                for (WaitlistItem item : waitlist) {
                    System.out.println("  " + item.toString());
                }
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    public void receiveShipment(String sku, int quantity) {
        Product product = getProductBySku(sku);
        if (product != null) {
            int currentQty = product.getQuantity();
            updateProductQuantity(sku, currentQty + quantity);
            System.out.println("Shipment received. New quantity: " + (currentQty + quantity));
        } else {
            System.out.println("Product not found.");
        }
    }

    private void fulfillWaitlist(String sku) {
        Product product = getProductBySku(sku);
        if (product == null || !product.hasWaitlistItems()) {
            return;
        }

        int availableQty = product.getQuantity();
        while (availableQty > 0 && product.hasWaitlistItems()) {
            WaitlistItem item = product.removeNextWaitlistItem();
            int requestedQty = item.getQuantity();
            
            if (requestedQty <= availableQty) {
                // Can fulfill entire request
                System.out.println("Fulfilled waitlist item: " + item.toString());
                System.out.println("Invoice generated for client " + item.getClientId());
                availableQty -= requestedQty;
            } else {
                // Partial fulfillment - put remainder back on waitlist
                System.out.println("Partially fulfilled waitlist item: " + item.toString());
                System.out.println("Remaining quantity " + (requestedQty - availableQty) + " kept on waitlist");
                product.addToWaitlist(item.getClientId(), requestedQty - availableQty, item.getUnitPrice());
                availableQty = 0;
            }
        }
        
        // Update product quantity
        product.updateStock(availableQty);
        saveProducts();
    }
}
