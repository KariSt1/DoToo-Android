package is.hi.hbv601g.dotoo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import is.hi.hbv601g.dotoo.Adapters.ExpandableListAdapter;

public class HomeActivity extends AppCompatActivity {

    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    EditText mStreakView;
    protected BottomNavigationView navigationView;
    List<TodoList> mFavouriteLists;
    List<Long> mDeletedListIds;
    List<TodoList> mChangedTodoLists = new ArrayList<TodoList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("home activity on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NetworkManager networkManager = NetworkManager.getInstance(this);
        User user = networkManager.getUser();
        networkManager.getTodolist(true, new NetworkCallback<List<TodoList>>() {
            @Override
            public void onSuccess(List<TodoList> result) {
                mFavouriteLists = result;

                System.out.print("Home activity on successs");

                mTodoListView = (ExpandableListView) findViewById(R.id.home_expandableList);

                mDeletedListIds = new ArrayList<Long>();

                mListAdapter = new ExpandableListAdapter(HomeActivity.this, mFavouriteLists, mDeletedListIds, mChangedTodoLists, mTodoListView);

                mTodoListView.setAdapter(mListAdapter);

                mStreakView = (EditText) findViewById(R.id.list_streak);
                mStreakView.setText(Integer.toString(user.getmStreak()));

            }

            @Override
            public void onFailure(String errorString) {
                System.out.println("Erum í onFailure í homeActivity.");
            }
        });

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        System.out.println("found navigation view");
        navigationView.setSelectedItemId(R.id.nav_home);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(HomeActivity.this, CalendarActivity.class);
                        startActivity(cal);
                        return true;
                    case R.id.nav_todolists:
                        mListAdapter.sendChanges(networkManager);
                        System.out.println("Test");
                        Intent todo = new Intent(HomeActivity.this, TodoListActivity.class);
                        startActivity(todo);
                        System.out.println("Test2");
                        return true;
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_friendList:
                        mListAdapter.sendChanges(networkManager);
                        Intent friends = new Intent(HomeActivity.this, FriendListActivity.class);
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

    public void sendChanges() {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        mDeletedListIds = mListAdapter.getDeletedLists();
        if(mDeletedListIds.size() > 0) {
            System.out.println("Eyði listum");
            try {
                networkManager.deleteTodolist(mDeletedListIds);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        mChangedTodoLists = mListAdapter.getChangedLists();
        if(mChangedTodoLists.size() > 0) {
            System.out.println("Sendi breytta lista");
            try {
                networkManager.postTodolists(mChangedTodoLists);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
