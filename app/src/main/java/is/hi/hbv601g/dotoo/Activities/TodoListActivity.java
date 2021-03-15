package is.hi.hbv601g.dotoo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import is.hi.hbv601g.dotoo.Fragments.NewTodoListDialogFragment;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.R;
import is.hi.hbv601g.dotoo.client.ExpandableListAdapter;

public class TodoListActivity extends AppCompatActivity implements NewTodoListDialogFragment.NoticeDialogListener{
    protected BottomNavigationView navigationView;

    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    List<TodoList> mTodoLists;
    List<Long> mDeletedListIds;
    List<TodoList> mChangedTodoLists = new ArrayList<TodoList>();
    private static final String TAG = "TodoListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getTodolist(false, new NetworkCallback<List<TodoList>>() {
            @Override
            public void onSuccess(List<TodoList> result) {
                mTodoLists = result;

                mTodoListView = (ExpandableListView) findViewById(R.id.todolist_expandableList);

                mDeletedListIds = new ArrayList<Long>();

                mListAdapter = new ExpandableListAdapter(TodoListActivity.this, mTodoLists, mDeletedListIds, mChangedTodoLists, mTodoListView);

                mTodoListView.setAdapter(mListAdapter);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "Failed to get todolists: " + errorString);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);


        // floating action button logic
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NewTodoListDialogFragment();
                dialog.show(getSupportFragmentManager(),"newTodoList");
            }
        });

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.nav_todolists);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(TodoListActivity.this, CalendarActivity.class);
                        startActivity(cal);
                        return true;
                    case R.id.nav_todolists:
                        return true;
                    case R.id.nav_home:
                        Intent home = new Intent(TodoListActivity.this, HomeActivity.class);
                        startActivity(home);
                }
                return false;
            }
        });

    }

    @Override
    protected void onStop() {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        mDeletedListIds = mListAdapter.getDeletedLists();
        try {
            networkManager.deleteTodolist(mDeletedListIds);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        super.onStop();
    }

    /**
     * Create new todolist using a dialog
     * @param name name of todolist
     * @param color color of todolist
     */
    @Override
    public void onDialogPositiveClick(String name, String color) {

        TodoList list = new TodoList();
        list.setName(name);
        list.setColor(color);
        mTodoLists.add(list);

    }
}