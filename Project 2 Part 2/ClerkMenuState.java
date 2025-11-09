import java.util.*;

public class ClerkMenuState implements State {
    @Override
    public void handle(Context context) {
        Scanner sc = context.getScanner();
        System.out.println("\n--- Clerk Menu ---");
        System.out.println("1. Add client");
        System.out.println("2. Show products");
        System.out.println("3. Show clients");
        System.out.println("4. Show clients with balance");
        System.out.println("5. Record payment");
        System.out.println("6. Become client");
        System.out.println("7. Logout");
        System.out.print("Select option: ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1: createClient(context); break;
            case 2: context.getProductCatalog().getAllProductInfo(System.out); break;
            case 3: displayAllClients(context); break;
            case 4: displayClientsWithBalance(context); break;
            case 5: recordPayment(context); break;
            case 6:
                System.out.print("Enter Client ID: ");
                int clientID = parseClientIdToInt(sc.nextLine());
                if (clientID < 0) {
                    System.out.println("Invalid Client ID.");
                    break;
                }
                context.setClientID(clientID);
                context.setStartedFromClerk(true);
                context.setState(new ClientMenuState());
                break;

            case 7: {
                // NEW: origin-aware logout for Clerk
                String origin = context.getClerkOrigin();
                if ("Manager".equals(origin)) {
                    context.setState(new ManagerMenuState());
                } else {
                    // Default: back to Opening if Clerk started from Opening
                    context.setState(new OpeningState());

                    // --- OPTIONAL EXIT POLICY ---
                    // If your interpretation requires exiting the program when
                    // Clerk was started from Opening, replace the line above with:
                    // System.out.println("Exiting program...");
                    // System.exit(0);
                }
                break;
            }

            default:
                System.out.println("Invalid option.");
        }
    }

    private int parseClientIdToInt(String raw) {
        String digits = raw == null ? "" : raw.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return -1;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createClient(Context context) {
        Scanner sc = context.getScanner();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Address: "); String address = sc.nextLine();
        System.out.print("Phone: "); String phone = sc.nextLine();
        Client client = new Client(name, address, phone);
        context.getClientDatabase().addClient(client);
        System.out.println("Client created: " + client.getId());
    }

    private void displayAllClients(Context context) {
        Iterator<Client> it = context.getClientDatabase().getClients();
        while (it.hasNext()) System.out.println(it.next());
    }

    private void displayClientsWithBalance(Context context) {
        Iterator<Client> it = context.getClientDatabase().getClients();
        while (it.hasNext()) {
            Client c = it.next();
            if (c.getBalance() != 0.0) System.out.println(c);
        }
    }

    private void recordPayment(Context context) {
        Scanner sc = context.getScanner();
        System.out.print("Enter Client ID: ");
        String clientId = sc.nextLine();
        Client client = context.getClientDatabase().search(clientId);
        if (client == null) { System.out.println("Client not found."); return; }
        System.out.print("Payment amount: ");
        double amt;
        try {
            amt = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        // NOTE: If your model defines positive balance as "owed", a payment reduces balance.
        // If your existing code expects addBalance to *add* debt, then subtract here:
        client.addBalance(-amt);
        System.out.println("New balance: $" + client.getBalance());
    }
}
