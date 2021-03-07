package is.hi.hbv601g.dotoo.Model;

public class Friend {
    private String Username;
    private int HighestStreak;

    public Friend(String username, int highestStreak) {
        Username = username;
        HighestStreak = highestStreak;
    }

    public Friend() {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getHighestStreak() {
        return HighestStreak;
    }

    public void setHighestStreak(int highestStreak) {
        HighestStreak = highestStreak;
    }
}
