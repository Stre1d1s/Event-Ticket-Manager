import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

public class MainServer extends UnicastRemoteObject implements Operations {
    private static final String DB_SERVER_HOST = "localhost";
    private static final int DB_SERVER_PORT = 8888;
    private static final int RMI_PORT = 1099;
    private static final String RMI_SERVICE_NAME = "TicketService";

    public MainServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            // Δημιουργία και εκκίνηση του MainServer
            MainServer server = new MainServer();
            
            // Δημιουργία RMI registry
            LocateRegistry.createRegistry(RMI_PORT);
            System.out.println("RMI registry ready.");
            
            // Δημοσίευση του RMI service
            Naming.rebind("rmi://localhost:" + RMI_PORT + "/" + RMI_SERVICE_NAME, server);
            System.out.println("MainServer is ready and waiting for connections...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    // Βοηθητική μέθοδος για επικοινωνία με τον DBServer μέσω sockets
    private Response communicateWithDBServer(Request request) {
        try (Socket socket = new Socket(DB_SERVER_HOST, DB_SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            oos.writeObject(request);
            oos.flush();
            return (Response) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with DB Server: " + e.getMessage());
            return new Response(false, "Database communication error");
        }
    }

    // Υλοποίηση των μεθόδων του Operations interface
    @Override
    public Response userSignUp(UserInfo user) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.USER_SIGNUP, user));
    }

    @Override
    public Response userDelete(UserInfo user) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.USER_DELETE, user));
    }

    @Override
    public Response userLogin(UserInfo user) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.USER_LOGIN, user));
    }

    @Override
    public Response userLogout(UserInfo user) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.USER_LOGOUT, user));
    }

    @Override
    public Response createEvent(EventInfo event) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.CREATE_EVENT, event));
    }

    @Override
    public Response requestEventInfo(EventInfo event) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.REQUEST_EVENT_INFO, event));
    }

    @Override
    public Response cancelEventOrder(EventInfo event) throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.CANCEL_EVENT_ORDER, event));
    }

    @Override
    public Response getAvailableEvents() throws RemoteException {
        return communicateWithDBServer(new Request(Request.RequestType.GET_AVAILABLE_EVENTS));
    }
}