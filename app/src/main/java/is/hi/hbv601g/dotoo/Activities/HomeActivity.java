package is.hi.hbv601g.dotoo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import is.hi.hbv601g.dotoo.client.ExpandableListAdapter;

public class HomeActivity extends AppCompatActivity {

    private TextView mWelcomeText;
    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    protected BottomNavigationView navigationView;
    List<TodoList> mFavouriteLists;
    List<TodoList> mDeletedLists;
    List<TodoList> mChangedTodoLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("home activity on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mWelcomeText = (TextView) findViewById(R.id.welcomeUser);
        String user = getIntent().getStringExtra("is.hi.hbv601g.dotoo.user_result");
        mWelcomeText.setText(getString(R.string.welcome_user, user));

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getTodolist(true, new NetworkCallback<List<TodoList>>() {
            @Override
            public void onSuccess(List<TodoList> result) {
                mFavouriteLists = result;

                System.out.print("Home activity on successs");

                mTodoListView = (ExpandableListView) findViewById(R.id.home_expandableList);

                mListAdapter = new ExpandableListAdapter(HomeActivity.this, mFavouriteLists, mDeletedLists, mChangedTodoLists, mTodoListView);

                mTodoListView.setAdapter(mListAdapter);

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
                        System.out.println("Test");
                        Intent todo = new Intent(HomeActivity.this, TodoListActivity.class);
                        startActivity(todo);
                        System.out.println("Test2");
                        return true;
                    case R.id.nav_home:
                        return true;
                }
                return false;
            }
        });

    }
}
