import java.rmi.*;

public interface Operations extends Remote {
    public void userSignUp(UserInfo user) throws RemoteException;
    public void userDeleteUser(UserInfo user) throws RemoteException;
    public void userLogin(UserInfo user) throws RemoteException;
    public void userLogout(UserInfo user) throws RemoteException;
    public void createEvent(EventInfo event) throws RemoteException;
    public void deleteEvent(EventInfo event) throws RemoteException;
    public void requestEventInfo(EventInfo event) throws RemoteException;
    public void cancelEventOrder(EventInfo event) throws RemoteException;
}