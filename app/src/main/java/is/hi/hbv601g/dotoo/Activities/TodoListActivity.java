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
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.Adapters.ExpandableListAdapter;

public class TodoListActivity extends AppCompatActivity implements NewTodoListDialogFragment.NoticeDialogListener{
    protected BottomNavigationView navigationView;

    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    TextView mStreakView;
    List<Button> mFavoriteButtons;
    List<TodoList> mTodoLists;
    List<Long> mDeletedListIds;
    List<TodoList> mChangedTodoLists = new ArrayList<TodoList>();
    private static final String TAG = "TodoListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NetworkManager networkManager = NetworkManager.getInstance(this);
        User user = networkManager.getUser();
        networkManager.getTodolist(false, new NetworkCallback<List<TodoList>>() {
            @Override
            public void onSuccess(List<TodoList> result) {
                mTodoLists = result;

                mTodoListView = (ExpandableListView) findViewById(R.id.todolist_expandableList);

                mDeletedListIds = new ArrayList<Long>();

                mStreakView = (TextView) findViewById(R.id.list_streak);
                mStreakView.setText(Integer.toString(user.getmStreak()));

                mListAdapter = new ExpandableListAdapter(TodoListActivity.this, mTodoLists, mDeletedListIds, mChangedTodoLists, mTodoListView, user, mStreakView);

                mTodoListView.setAdapter(mListAdapter);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "Failed to get todolists: " + errorString);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);


        // floating action button

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
                        mListAdapter.sendChanges(networkManager);
                        try
                        {
                            Thread.sleep(150);
                        }
                        catch(InterruptedException ex)
                        {
                            Thread.currentThread().interrupt();
                        }
                        Intent home = new Intent(TodoListActivity.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.nav_friendList:
                        Intent friends = new Intent(TodoListActivity.this, FriendListActivity.class);
                        startActivity(friends);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStop() {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        mListAdapter.sendChanges(networkManager);
        super.onStop();
    }

    @Override
    public void onDialogPositiveClick(String name, String color) {
        mListAdapter.newTodoList(name, color);
    }
}