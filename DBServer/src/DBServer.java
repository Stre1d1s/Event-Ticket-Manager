package DBServer.src;

import java.io.*;
import java.net.*;

public class DBServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        new DBServer().start();
    }

    public void start() {
        System.out.println("Starting server on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
                    
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    
                    Request request = (Request) ois.readObject();
                    System.out.println("Processing request: " + request.getType());

                    Response response = processRequest(request);
                    oos.writeObject(response);
                    oos.flush();

                } catch (Exception e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private Response processRequest(Request request) {
        switch (request.getType()) {
            case USER_SIGNUP:
                return userSignUp(request.getUserInfo());
            case USER_DELETE:
                return userDelete(request.getFullname());
            case USER_LOGIN:
                return userLogin(request.getUserInfo());
            case USER_LOGOUT:
                return userLogout(request.getFullname());
            case CREATE_EVENT:
                return createEvent(request.getEventInfo());
            case REQUEST_EVENT_INFO:
                return requestEventInfo(request.getEventInfo());
            case CANCEL_EVENT_ORDER:
                return cancelEventOrder(request.getEventInfo());
            default:
                return new Response(false, "Unknown request type");
        }
    }

    private Response userSignUp(UserInfo user) {
        return new Response(true, "User signed up successfully");
    }

    private Response userDelete(String fullname) {
        return new Response(true, "User deleted successfully");
    }

    private Response userLogin(UserInfo user) {
        return new Response(true, "User logged in successfully");
    }

    private Response userLogout(String fullname) {
        return new Response(true, "User logged out successfully");
    }

    private Response createEvent(EventInfo event) {
        return new Response(true, "Event created successfully");
    }

    private Response requestEventInfo(EventInfo event) {
        return new Response(true, "Event info retrieved");
    }

    private Response cancelEventOrder(EventInfo event) {
        return new Response(true, "Event order canceled successfully");
    }
}
