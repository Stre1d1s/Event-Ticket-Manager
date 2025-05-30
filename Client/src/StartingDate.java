import java.util.Date;

public class StartingDate {
    
    private Date date;
    private String hour;
    private String cost;
    
    public StartingDate(Date d, String h, String c){
        date = d;
        hour = h;
        cost = c;
    }

    public StartingDate() {
    }
    
    
    public Date getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getCost() {
        return cost;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "StartingDate{" + "date=" + date + ", hour=" + hour + ", cost=" + cost + '}';
    }
    
    
}
