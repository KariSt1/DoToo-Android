
        package is.hi.hbv601g.dotoo.Model;

        import java.util.ArrayList;
        import java.util.List;

        public class User {

            public String mUsername;
            public String mName;
            public String mPassword;
            private List<TodoList> mTodoLists = new ArrayList<>();
            private List<Event> mEvents = new ArrayList<>();

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

            public User(String username, String name, String password) {
            mUsername = username;
            mName = name;
            mPassword = password;
            }

            public User() {

            }
        }
