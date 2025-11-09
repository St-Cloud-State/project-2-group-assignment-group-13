import java.io.*;
import java.util.*;

/**
 * ClientDatabase
 * Acts like a collection of all clients.
 * Uses the Singleton pattern (only one instance of this database exists).
 * Supports persistence (save/retrieve) and the Iterator Pattern.
 */
public class ClientDatabase implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients = new LinkedList<>();
    private static ClientDatabase clientDB;

    // Private constructor so no one can make a new instance directly
    private ClientDatabase() {}

    // Returns the one and only instance
    public static ClientDatabase instance() {
        if (clientDB == null) {
            clientDB = new ClientDatabase();
        }
        return clientDB;
    }

    // Add a client into the list
    public boolean addClient(Client client) {
        return clients.add(client);
    }

    // Iterator Pattern: return an iterator instead of the raw list
    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    // Search for a client by their ID
    public Client search(String clientId) {
        for (Client c : clients) {
            if (c.getId().equals(clientId)) {
                return c;
            }
        }
        return null; // not found
    }

    // Save this object to a file
    public static void save(ObjectOutputStream output) throws IOException {
        output.writeObject(clientDB);
    }

    // Retrieve object back from a file
    public static void retrieve(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        clientDB = (ClientDatabase) input.readObject();
    }
}
