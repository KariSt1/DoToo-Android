package is.hi.hbv601g.dotoo.Model;

public class Friend {
    private String mUsername;
    private int mHighestStreak;

    public Friend(String username, int highestStreak) {
        mUsername = username;
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

    public int getHighestStreak() {
        return mHighestStreak;
    }

    public void setHighestStreak(int highestStreak) {
        mHighestStreak = highestStreak;
    }
}
