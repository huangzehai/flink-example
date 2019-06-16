package huangzehai.model;

import java.time.LocalDateTime;

public class VehicleEvent implements Comparable<VehicleEvent> {
    private int id;
    private String vin;
    private LocalDateTime dateTime;
    private long timestamp;
    private String event;
    private String alerts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VehicleEvent{" +
                "id=" + id +
                ", vin='" + vin + '\'' +
                ", dateTime=" + dateTime +
                ", timestamp=" + timestamp +
                ", event='" + event + '\'' +
                ", alerts='" + alerts + '\'' +
                '}';
    }

    @Override
    public int compareTo(VehicleEvent o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}
