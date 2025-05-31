//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package Client.src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private String eventId;
    private String title;
    private EventType type;
    private boolean isActive;
    private List<Performance> performances;

    public Event(String eventId, String title, EventType type) {
        this.eventId = eventId;
        this.title = title;
        this.type = type;
        this.isActive = true;
        this.performances = new ArrayList<>();
    }

    public void addPerformance(Performance performance) {
        performances.add(performance);
    }

    public void deactivate() {
        isActive = false;
    }

    // Getters
    public String getEventId() { return eventId; }
    public String getTitle() { return title; }
    public EventType getType() { return type; }
    public boolean isActive() { return isActive; }
    public List<Performance> getPerformances() { return performances; }
    public Performance getPerformance(String performanceId) {
        return performances.stream()
            .filter(p -> p.getPerformanceId().equals(performanceId))
            .findFirst()
            .orElse(null);
    }
}
