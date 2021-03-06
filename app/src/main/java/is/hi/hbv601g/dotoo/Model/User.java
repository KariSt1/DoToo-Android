
        package is.hi.hbv601g.dotoo.Model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;
        import java.util.List;

        public class User {

            public String mUsername;
            public String mName;
            public String mPassword;
            private List<TodoList> mTodoLists = new ArrayList<>();
            private List<Event> mEvents = new ArrayList<>();

            @SerializedName("finishedTodoLists")
            private int mStreak;

            public String getUsername() {
            return mUsername;
            }

            public void setUsername(String username) {
            mUsername = username;
            }

            public String getName() {
            return mName;
            }

            public void setName(String name) {
            mName = name;
            }

            public String getPassword() {
            return mPassword;
            }

            public void setPassword(String password) {
            mPassword = password;
            }

            public List<TodoList> getmTodoLists() {
                return mTodoLists;
            }

            public void setmTodoLists(List<TodoList> mTodoLists) {
                this.mTodoLists = mTodoLists;
            }

            public List<Event> getmEvents() {
                return mEvents;
            }

            public void setmEvents(List<Event> mEvents) {
                this.mEvents = mEvents;
            }

            public int getmStreak() {
                return mStreak;
            }

            public void setmStreak(int mStreak) {
                this.mStreak = mStreak;
            }

            public User(String username, String name, String password) {
            mUsername = username;
            mName = name;
            mPassword = password;
            }

            public User() {

            }
        }
