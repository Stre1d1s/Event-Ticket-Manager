//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package DataBaseServer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Performance implements Serializable {
    private String performanceId;
    private LocalDateTime startTime;
    private int totalSeats;
    private int availableSeats;
    private double price;

    public Performance(String performanceId, LocalDateTime startTime, int totalSeats, double price) {
        this.performanceId = performanceId;
        this.startTime = startTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;  // Αρχικά όλες οι θέσεις διαθέσιμες
        this.price = price;
    }

    // Getters και Setters
    public String getPerformanceId() { return performanceId; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int seats) { availableSeats = seats; }
    public double getPrice() { return price; }
    public LocalDateTime getStartTime() { return startTime; }
}
