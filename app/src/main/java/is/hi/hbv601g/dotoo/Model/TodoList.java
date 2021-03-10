package is.hi.hbv601g.dotoo.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TodoList {

    @SerializedName("id")
    private long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("color")
    private String mColor;
    @SerializedName("favorite")
    private boolean mFavorite;
    @SerializedName("items")
    private List<TodoListItem> mItems = new ArrayList<>();
    @SerializedName("user")
    private User mUser;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.mFavorite = favorite;
    }

    public List<TodoListItem> getItems() {
        return mItems;
    }

    public void setItems(List<TodoListItem> items) {
        mItems = items;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public TodoList(String name, String color, User user) {
        mName = name;
        mColor = color;
        mUser = user;
    }

    public TodoList() {
    }
}
