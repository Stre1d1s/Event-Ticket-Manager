import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

public class MSOperations extends UnicastRemoteObject implements Operations {
    private MainServer mainServer;
    
    public MSOperations(MainServer mainServer) throws RemoteException {
        super();
        this.mainServer = mainServer;
    }
    
    // User operations
    @Override
    public Response userSignUp(UserInfo user) throws RemoteException {
        return mainServer.userSignUp(user);
    }
    
    @Override
    public Response userDelete(UserInfo user) throws RemoteException {
        return mainServer.userDelete(user);
    }
    
    @Override
    public Response userLogin(UserInfo user) throws RemoteException {
        return mainServer.userLogin(user);
    }
    
    @Override
    public Response userLogout(UserInfo user) throws RemoteException {
        return mainServer.userLogout(user);
    }
    
    // Event operations
    @Override
    public Response createEvent(EventInfo event) throws RemoteException {
        return mainServer.createEvent(event);
    }
    
    @Override
    public Response requestEventInfo(EventInfo event) throws RemoteException {
        return mainServer.requestEventInfo(event);
    }
    
    @Override
    public Response cancelEventOrder(EventInfo event) throws RemoteException {
        return mainServer.cancelEventOrder(event);
    }
    
    @Override
    public Response getAvailableEvents() throws RemoteException {
        return mainServer.getAvailableEvents();
    }
    
    // Helper method to start the RMI service
    public static void startService(MainServer mainServer) {
        try {
            MSOperations msOperations = new MSOperations(mainServer);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("TicketService", msOperations);
            System.out.println("MSOperations RMI service is running...");
        } catch (Exception e) {
            System.err.println("Error starting RMI service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}