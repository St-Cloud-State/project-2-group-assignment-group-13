import java.util.*;

public class Context {
    private State currentState;
    private int clientID;
    private boolean startedFromClerk;

    // NEW: track how Clerk was entered ("Opening" or "Manager")
    private String clerkOrigin = "Opening";

    private ProductCatalog productCatalog;
    private ClientDatabase clientDatabase;
    private Map<String, List<Invoice>> clientInvoices;
    private Scanner scanner;

    public Context() {
        scanner = new Scanner(System.in);
        productCatalog = new ProductCatalog();
        clientDatabase = ClientDatabase.instance();
        clientInvoices = new HashMap<>();
    }

    public void setState(State state) { currentState = state; }
    public State getState() { return currentState; }

    public int getClientID() { return clientID; }
    public void setClientID(int clientID) { this.clientID = clientID; }

    public boolean isStartedFromClerk() { return startedFromClerk; }
    public void setStartedFromClerk(boolean val) { this.startedFromClerk = val; }

    // NEW: Clerk origin tracking
    public String getClerkOrigin() { return clerkOrigin; }
    public void setClerkOrigin(String origin) { this.clerkOrigin = origin; }

    public ProductCatalog getProductCatalog() { return productCatalog; }
    public ClientDatabase getClientDatabase() { return clientDatabase; }
    public Map<String, List<Invoice>> getClientInvoices() { return clientInvoices; }
    public Scanner getScanner() { return scanner; }

    public void run() {
        while (true) {
            currentState.handle(this);
        }
    }
}
