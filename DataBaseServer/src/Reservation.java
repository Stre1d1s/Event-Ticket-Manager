//ICSD21028 -- Konstantinos Katsaros

package DataBaseServer.src;

import java.io.Serializable;

public class Reservation implements Serializable {

    private String reservationId;
    private String username;
    private String eventId;
    private String performanceId;
    private int seats;
    private boolean isPaid;
    private boolean isCancelled;

    public Reservation(String reservationId, String username, String eventId, String performanceId, int seats) {
        this.reservationId = reservationId;
        this.username = username;
        this.eventId = eventId;
        this.performanceId = performanceId;
        this.seats = seats;
        this.isPaid = false;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(String performanceId) {
        this.performanceId = performanceId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean isIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

}
