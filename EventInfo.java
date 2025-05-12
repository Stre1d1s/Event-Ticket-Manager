/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbserver;

/**
 *
 * @author aisxi
 */
public class EventInfo {
    private String title;
    private String type;
    private StartingDate eventDate;
    private float balance;

    public EventInfo(String title, String type, StartingDate eventDate, float balance) {
        this.title = title;
        this.type = type;
        this.eventDate = eventDate;
        this.balance = balance;
    }

    public EventInfo() {
    }
    
    
    
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public StartingDate getEventDate() {
        return eventDate;
    }

    public float getBalance() {
        return balance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEventDate(StartingDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "EventInfo{" + "title=" + title + ", type=" + type + ", eventDate=" + eventDate + ", balance=" + balance + '}';
    }
    
    
}
