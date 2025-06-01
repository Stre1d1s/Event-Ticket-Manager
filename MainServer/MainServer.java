//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package MainServer;

import DataBaseServer.Event;
import DataBaseServer.Request;
import DataBaseServer.RequestType;
import DataBaseServer.Reservation;
import DataBaseServer.Response;
import DataBaseServer.ResponseStatus;
import DataBaseServer.User;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.concurrent.*;

public class MainServer extends UnicastRemoteObject implements ReservationSystem {
    private static final int DB_PORT = 54321;
    private static final int RMI_PORT = 1099;
    private static final String DB_HOST = "localhost";
    
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    
    public MainServer() throws RemoteException {
        super();
    }
    
    public static void main(String[] args) {
        try {
            // Start RMI registry
            LocateRegistry.createRegistry(RMI_PORT);
            
            // Create and bind the server instance
            MainServer server = new MainServer();
            Naming.rebind("ReservationSystem", server);
            System.out.println("MainServer ready...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Helper method to communicate with DatabaseServer
    private Response sendRequestToDB(Request request) {
        try (Socket socket = new Socket(DB_HOST, DB_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            out.writeObject(request);
            return (Response) in.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with DB server: " + e.getMessage());
            return new Response(ResponseStatus.ERROR, "Database communication error", null);
        }
    }
    
    // RMI Interface implementations
    @Override
    public Response registerUser(User user) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.REGISTER_USER, user));
    }
    
    @Override
    public Response deleteUser(String username) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.DELETE_USER, username));
    }
    
    @Override
    public Response addEvent(Event event) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.ADD_EVENT, event));
    }
    
    @Override
    public Response deactivateEvent(String eventId) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.DEACTIVATE_EVENT, eventId));
    }
    
    @Override
    public Response getActiveEvents() throws RemoteException {
        return sendRequestToDB(new Request(RequestType.GET_EVENTS, null));
    }
    
    @Override
    public Response getEventDetails(String eventId) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.GET_EVENT_DETAILS, eventId));
    }
    
    @Override
    public Response reserveTickets(Reservation reservation) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.RESERVE_TICKETS, reservation));
    }
    
    @Override
    public Response processPayment(String reservationId) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.PROCESS_PAYMENT, reservationId));
    }
    
    @Override
    public Response cancelReservation(String reservationId) throws RemoteException {
        return sendRequestToDB(new Request(RequestType.CANCEL_RESERVATION, reservationId));
    }
    
    @Override
    public Response authenticateUser(String username, String password) throws RemoteException {
        // This would need to be implemented with local user authentication
        // or by adding AUTHENTICATE_USER to RequestType and handling in DataBaseManager
        return new Response(ResponseStatus.ERROR, "Authentication not implemented", null);
    }
}