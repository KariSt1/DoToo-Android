package is.hi.hbv601g.dotoo.Model;

import java.util.Calendar;

public class Event {
    private long id;

    private Calendar startDate;
    private Calendar endDate;
    private String title;
    private String category;
    private String color;
    private User user;

    public Event(Calendar startDate, Calendar endDate, String title, String category, String color, User user) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.category = category;
        this.color = color;
        this.user = user;
    }

    public Event() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
