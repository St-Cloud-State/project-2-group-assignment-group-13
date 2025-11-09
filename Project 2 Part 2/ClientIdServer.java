import java.io.*;

/**
 * ClientIdServer
 * Generates unique IDs for clients (like C1, C2, C3...).
 * Also uses Singleton pattern so IDs don’t overlap.
 */
public class ClientIdServer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idCounter;
    private static ClientIdServer server;

    // private so no one else makes instances
    private ClientIdServer() {
        idCounter = 1;
    }

    // Always return the same single instance
    public static ClientIdServer instance() {
        if (server == null) {
            server = new ClientIdServer();
        }
        return server;
    }

    // Returns the next ID, then increases the counter
    public int getId() {
        return idCounter++;
    }

    // Save to file
    public static void save(ObjectOutputStream output) throws IOException {
        output.writeObject(server);
    }

    // Retrieve from file
    public static void retrieve(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        server = (ClientIdServer) input.readObject();
    }
}
