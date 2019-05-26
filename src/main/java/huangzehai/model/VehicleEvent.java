package huangzehai.model;

import java.time.LocalDateTime;

public class VehicleEvent {
    private int id;
    private String vin;
    private LocalDateTime dateTime;
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

    @Override
    public String toString() {
        return "VehicleEvent{" +
                "id=" + id +
                ", vin='" + vin + '\'' +
                ", dateTime=" + dateTime +
                ", event='" + event + '\'' +
                ", alerts='" + alerts + '\'' +
                '}';
    }
}
