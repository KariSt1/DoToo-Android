package is.hi.hbv601g.dotoo.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class Event {

    @Expose
    @SerializedName("id")
    private long mId;

    @Expose
    @SerializedName("startDate")
    private String mStartDate;

    @Expose
    @SerializedName("endDate")
    private String mEndDate;

    @Expose
    @SerializedName("tile")
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

    public Event(String startDate, String endDate, String title, String category, String color, User user) {
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

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
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
