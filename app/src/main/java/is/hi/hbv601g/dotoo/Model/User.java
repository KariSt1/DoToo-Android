
        package is.hi.hbv601g.dotoo.Model;

        import java.util.ArrayList;
        import java.util.List;

        public class User {

            public String username;
            public String name;
            public String password;
            private List<TodoList> todoLists = new ArrayList<>();
            private List<Event> events = new ArrayList<>();

            public String getUsername() {
            return username;
            }

            public void setUsername(String username) {
            this.username = username;
            }

            public String getName() {
            return name;
            }

            public void setName(String name) {
            this.name = name;
            }

            public String getPassword() {
            return password;
            }

            public void setPassword(String password) {
            this.password = password;
            }

            public User(String username, String name, String password) {
            this.username = username;
            this.name = name;
            this.password = password;
            }

            public User() {

            }
        }
