package is.hi.hbv601g.dotoo.Model;

import java.util.Calendar;

public class Event {
    private long mId;

    private Calendar mStartDate;
    private Calendar mEndDate;
    private String mTitle;
    private String mCategory;
    private String mColor;
    private User mUser;

    public Event(Calendar startDate, Calendar endDate, String title, String category, String color, User user) {
        mStartDate = startDate;
        mEndDate = endDate;
        mTitle = title;
        mCategory = category;
        mColor = color;
        mUser = user;
    }

    public Event() {

    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar startDate) {
        mStartDate = startDate;
    }

    public Calendar getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Calendar endDate) {
        mEndDate = endDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
