import java.util.*;

public class ManagerMenuState implements State {
    @Override
    public void handle(Context context) {
        Scanner sc = context.getScanner();
        System.out.println("\n***** Manager Menu *****");
        System.out.println("1. Add product");
        System.out.println("2. Display waitlist");
        System.out.println("3. Receive shipment");
        System.out.println("4. Become clerk");
        System.out.println("5. Logout");
        System.out.print("Select option: ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1: addProduct(context); break;
            case 2: displayWaitlist(context); break;
            case 3: receiveShipment(context); break;
            case 4:
                // Remember: entering Clerk from Manager
                context.setClerkOrigin("Manager");
                context.setState(new ClerkMenuState());
                break;
            case 5:
                // Spec: Logout (no origin rule provided) -> back to Opening
                context.setState(new OpeningState());
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void addProduct(Context context) {
        Scanner sc = context.getScanner();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("SKU: "); String sku = sc.nextLine();
        System.out.print("Description: "); String desc = sc.nextLine();
        System.out.print("Quantity: "); int qty = Integer.parseInt(sc.nextLine());
        System.out.print("Price: "); double price = Double.parseDouble(sc.nextLine());
        Product product = new Product(name, sku, desc, qty, price);
        context.getProductCatalog().addProduct(product);
        System.out.println("Product added.");
    }

    private void displayWaitlist(Context context) {
        Scanner sc = context.getScanner();
        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();
        context.getProductCatalog().displayWaitlist(sku);
    }

    private void receiveShipment(Context context) {
        Scanner sc = context.getScanner();
        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        context.getProductCatalog().receiveShipment(sku, qty);
        System.out.println("Shipment received.");
    }
}
