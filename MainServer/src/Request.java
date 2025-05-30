//icsd21049 Aristeidis Papadopoulos

import java.io.Serializable;

/**
 * Κλάση για τα αιτήματα που στέλνει ο Client στον Server.
 * Καθορίζει τον τύπο της λειτουργίας και περιέχει τα απαραίτητα δεδομένα.
 * Υλοποιεί το Serializable για να μπορεί να αποσταλλεί μέσω δικτύου.
 */
public class Request implements Serializable {
    public enum RequestType {
        USER_SIGNUP,
        USER_DELETE,
        USER_LOGIN,
        USER_LOGOUT,
        CREATE_EVENT,
        DELETE_EVENT,
        REQUEST_EVENT_INFO,
        CANCEL_EVENT_ORDER,
        GET_AVAILABLE_EVENTS
    }

    private RequestType type;  // Τύπος αίτηματος
    private UserInfo user;
    private String fullname;
    private EventInfo event;

    // Constructors
    public Request(RequestType type) {
        this.type = type;
    }

    public Request(RequestType type, UserInfo user) {
        this.type = type;
        this.user = user;
    }

    public Request(RequestType type, String fullname) {
        this.type = type;
        this.fullname = fullname;
    }

    public Request(RequestType type, EventInfo event) {
        this.type = type;
        this.event = event;
    }

    // Getters
    public RequestType getType() { return type; }
    public UserInfo getUserInfo() { return user; }
    public String getFullname() { return fullname; }
    public EventInfo getEventInfo() { return event; }
}