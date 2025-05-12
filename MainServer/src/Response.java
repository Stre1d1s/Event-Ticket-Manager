//icsd21049 Aristeidis Papadopoulos

import java.io.Serializable;
import java.util.List;

/**
 * Κλάση για τις απαντήσεις του Server προς τον Client.
 * Περιέχει κατάσταση επιτυχίας/αποτυχίας, μήνυμα και δεδομένα.
 * Υλοποιεί το Serializable για να μπορεί να αποσταλλεί μέσω δικτύου.
 */
public class Response implements Serializable {
    private boolean success;   // True αν η λειτουργία ήταν επιτυχής
    private String message;    // Περιγραφικό μήνυμα
    private Album album;       // Αντικείμενο Album (για GET_ALBUM)
    private List<Album> albums;// Λίστα αλμπουμ (για GET_ALL_ALBUMS)

    // Constructors
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, Album album) {
        this(success, message);
        this.album = album;
    }

    public Response(boolean success, String message, List<Album> albums) {
        this(success, message);
        this.albums = albums;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Album getAlbum() { return album; }
    public List<Album> getAlbums() { return albums; }
}