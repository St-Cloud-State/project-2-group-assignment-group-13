import java.util.Scanner;

public class OpeningState implements State {
    @Override
    public void handle(Context context) {
        Scanner sc = context.getScanner();
        System.out.println("\n***** Warehouse System *****");
        System.out.println("1. Login As Client");
        System.out.println("2. Login As Clerk");
        System.out.println("3. Login As Manager");
        System.out.println("4. Exit Program");
        System.out.print("Choose user type: ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to main menu.");
            return;
        }

        switch (choice) {
            case 1:
                System.out.print("Enter Client ID (e.g., C1): ");
                String input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("No Client ID entered. Returning to main menu.");
                    break;
                }
                if (!input.matches("C\\d+")) {
                    System.out.println("Invalid Client ID format. Returning to main menu.");
                    break;
                }

                Client client = ClientDatabase.instance().search(input);
                if (client == null) {
                    System.out.println("Client not found. Returning to main menu.");
                } else {
                    int clientID = Integer.parseInt(input.substring(1));
                    context.setClientID(clientID);
                    // Ensure Client logout returns to Opening (not Clerk)
                    context.setStartedFromClerk(false);
                    context.setState(new ClientMenuState());
                }
                break;

            case 2:
                // Remember origin for Clerk
                context.setClerkOrigin("Opening");
                context.setState(new ClerkMenuState());
                break;

            case 3:
                context.setState(new ManagerMenuState());
                break;

            case 4:
                System.out.println("Exiting program...");
                System.exit(0);
                break;

            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
}
