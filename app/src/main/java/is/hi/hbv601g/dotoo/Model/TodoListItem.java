package is.hi.hbv601g.dotoo.Model;

public class TodoListItem {
    private long mId;

    private String mDescription;
    private boolean mChecked;

    private TodoList mTodoList;

    public TodoListItem() {
    }

    public TodoListItem(String description, boolean checked, TodoList todoList) {
        mDescription = description;
        mChecked = checked;
        mTodoList = todoList;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean getChecked() {
        return mChecked;
    }

    public void setChecked(boolean done) {
        mChecked = done;
    }

    public TodoList getTodoList() {
        return mTodoList;
    }

    public void setTodoList(TodoList todoList) {
        mTodoList = todoList;
    }
}
