//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package MainServer;

import DataBaseServer.Event;
import DataBaseServer.Reservation;
import DataBaseServer.Response;
import DataBaseServer.User;
import java.rmi.*;

public interface ReservationSystem extends Remote {
    Response registerUser(User user) throws RemoteException;
    Response deleteUser(String username) throws RemoteException;
    Response addEvent(Event event, String username) throws RemoteException;
    Response deactivateEvent(String eventId) throws RemoteException;
    Response getActiveEvents() throws RemoteException;
    Response getEventDetails(String eventId) throws RemoteException;
    Response reserveTickets(Reservation reservation) throws RemoteException;
    Response processPayment(String reservationId) throws RemoteException;
    Response cancelReservation(String reservationId, String username) throws RemoteException;
    Response authenticateUser(String username, String password) throws RemoteException;
    Response logout(String username) throws RemoteException;
    Response getUserReservations(String username) throws RemoteException;
}