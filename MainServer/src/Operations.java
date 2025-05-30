import java.rmi.*;
import java.rmi.server.*;

public interface Operations extends Remote {
    // User operations
    public Response userSignUp(UserInfo user) throws RemoteException;
    public Response userDelete(UserInfo user) throws RemoteException;
    public Response userLogin(UserInfo user) throws RemoteException;
    public Response userLogout(UserInfo user) throws RemoteException;
    
    // Event operations
    public Response createEvent(EventInfo event) throws RemoteException;
    public Response requestEventInfo(EventInfo event) throws RemoteException;
    public Response cancelEventOrder(EventInfo event) throws RemoteException;
    
    // Additional methods
    public Response getAvailableEvents() throws RemoteException;
}