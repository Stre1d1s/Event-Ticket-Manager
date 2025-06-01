//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package DataBaseServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private Socket clientSocket;
    private DataBaseManager dbManager;

    public RequestHandler(Socket socket, DataBaseManager dbManager) {
        this.clientSocket = socket;
        this.dbManager = dbManager;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
             
            Request request = (Request) in.readObject();
            Response response = dbManager.handleRequest(request);
            out.writeObject(response);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
