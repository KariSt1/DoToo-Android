package is.hi.hbv601g.dotoo.Model;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {

    @Expose
    @SerializedName("id")
    private long mId;

    @Expose
    @SerializedName("startDate")
    private Calendar mStartDate;

    @Expose
    @SerializedName("endDate")
    private Calendar mEndDate;

    @Expose
    @SerializedName("title")
    private String mTitle;

    @Expose
    @SerializedName("category")
    private String mCategory;

    @Expose
    @SerializedName("color")
    private String mColor;

    @Expose
    @SerializedName("user")
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

    @SuppressLint("SimpleDateFormat")
    public WeekViewEvent toWeekViewEvent(){


        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName(getTitle());
        weekViewEvent.setStartTime(getStartDate());
        weekViewEvent.setEndTime(getEndDate());
        weekViewEvent.setColor(Color.parseColor(getColor()));

        return weekViewEvent;
    }

}
