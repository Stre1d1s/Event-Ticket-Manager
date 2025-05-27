//icsd21049 Aristeidis Papadopoulos

package DBServer.src;

import java.io.Serializable;

/**
 * Κλάση για τις απαντήσεις του Server προς τον Client.
 * Περιέχει κατάσταση επιτυχίας/αποτυχίας, μήνυμα και δεδομένα.
 * Υλοποιεί το Serializable για να μπορεί να αποσταλλεί μέσω δικτύου.
 */
public class Response implements Serializable {
    private boolean success;   // True αν η λειτουργία ήταν επιτυχής
    private boolean admin;
    private String message;    // Περιγραφικό μήνυμα
    private UserInfo user;
    private EventInfo event;

    // Constructors
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, boolean admin, String message) {
        this.success = success;
        this.admin = admin;
        this.message = message;
    }

    public Response(boolean success, String message, UserInfo user) {
        this(success, message);
        this.user = user;
    }

    public Response(boolean success, String message, EventInfo event) {
        this(success, message);
        this.event = event;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public boolean isAdmin() { return admin; }
    public String getMessage() { return message; }
    public UserInfo getUserInfo() { return user; }
    public EventInfo getEventInfo() { return event; }
}