package is.hi.hbv601g.dotoo.Model;

import java.util.ArrayList;
import java.util.List;

public class TodoList {
    private long mId;

    private String mName;
    private String mColor;
    private List<TodoListItem> mItems = new ArrayList<>();

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
