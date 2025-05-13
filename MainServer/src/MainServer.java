import java.net.*;
import java.io.*;

public class MainServer {
    private static final String SERVER_HOST = "localhost"; // Διεύθυνση Server
    private static final int SERVER_PORT = 8888;

    private Response sendRequest(Request request) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            oos.writeObject(request);  // Αποστολή αιτήματος
            oos.flush();
            return (Response) ois.readObject();  // Λήψη απάντησης
            
        } catch (IOException | ClassNotFoundException e) {
            return new Response(false, "Σφάλμα δικτύου");
        }
    }

    public void userSignUp(UserInfo user){
        Response response = sendRequest(new Request(Request.RequestType.USER_SIGNUP, user));
    }

    public void userDeleteUser(UserInfo user){
        Response response = sendRequest(new Request(Request.RequestType.USER_DELETE, user));
    }

    public void userLogin(UserInfo user){
        Response response = sendRequest(new Request(Request.RequestType.USER_LOGIN, user));
    }

    public void userLogout(UserInfo user){
        Response response = sendRequest(new Request(Request.RequestType.USER_LOGOUT, user));
    }

    public void createEvent(EventInfo event){
        Response response = sendRequest(new Request(Request.RequestType.CREATE_EVENT, event));
    }

    public void requestEventInfo(EventInfo event){
        Response response = sendRequest(new Request(Request.RequestType.REQUEST_EVENT_INFO, event));
    }

    public void cancelEventOrder(EventInfo event){
        Response response = sendRequest(new Request(Request.RequestType.CANCEL_EVENT_ORDER, event));
    }
}
