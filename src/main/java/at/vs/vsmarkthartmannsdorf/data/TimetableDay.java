package at.vs.vsmarkthartmannsdorf.data;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TimetableDay {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty time= new SimpleStringProperty();
    private SimpleStringProperty monday= new SimpleStringProperty();
    private SimpleStringProperty tuesday= new SimpleStringProperty();
    private SimpleStringProperty wednesday= new SimpleStringProperty();
    private SimpleStringProperty thursday= new SimpleStringProperty();
    private SimpleStringProperty friday= new SimpleStringProperty();

    public TimetableDay(String id, String time, String monday, String tuesday, String wednesday, String thursday, String friday) {
        this.id = new SimpleStringProperty(id);
        this.time = new SimpleStringProperty(time);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
    }

    public void ChangeHour(String day,String subject ){
        SimpleStringProperty changeday = new SimpleStringProperty(day);
        if (monday.equals(changeday)) {
            monday.set(subject);
        } else if (tuesday.equals(changeday)) {
            tuesday.set(subject);
        } else if (wednesday.equals(changeday)) {
            wednesday.set(subject);
        } else if (thursday.equals(changeday)) {
            thursday.set(subject);
        } else if (friday.equals(changeday)) {
            friday.set(subject);
        }
    }

    public String getMonday() {
        return monday.get();
    }

    public void setMonday(String monday) {
        this.monday.set(monday);
    }

    public String getTuesday() {
        return tuesday.get();
    }

    public SimpleStringProperty tuesdayProperty() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday.set(tuesday);
    }

    public String getWednesday() {
        return wednesday.get();
    }

    public SimpleStringProperty wednesdayProperty() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday.set(wednesday);
    }

    public String getThursday() {
        return thursday.get();
    }

    public SimpleStringProperty thursdayProperty() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday.set(thursday);
    }

    public String getFriday() {
        return friday.get();
    }

    public SimpleStringProperty fridayProperty() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday.set(friday);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
