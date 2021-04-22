package is.hi.hbv601g.dotoo.Model;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("name")
    private String mName;

    @SerializedName("highestStreak")
    private int mHighestStreak;

    public Friend(String username, String name, int highestStreak) {
        mUsername = username;
        mName = name;
        mHighestStreak = highestStreak;
    }

    public Friend() {
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getName() { return mName; }

    public void setName(String name) { mName = name; }

    public int getHighestStreak() {
        return mHighestStreak;
    }

    public void setHighestStreak(int highestStreak) {
        mHighestStreak = highestStreak;
    }
}
