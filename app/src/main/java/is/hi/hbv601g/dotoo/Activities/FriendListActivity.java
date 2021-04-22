package is.hi.hbv601g.dotoo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.dotoo.Adapters.ExpandableListAdapter;
import is.hi.hbv601g.dotoo.Adapters.FriendListAdapter;
import is.hi.hbv601g.dotoo.Fragments.NewFriendDialogFragment;
import is.hi.hbv601g.dotoo.Model.Friend;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;

public class FriendListActivity extends AppCompatActivity implements NewFriendDialogFragment.NoticeDialogListener {

    protected BottomNavigationView navigationView;
    private ListView friendList;
    private ArrayList<Friend> friends;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        friendList = findViewById(R.id.friend_list);
        getFriends();

        // floating action button
        FloatingActionButton fab = findViewById(R.id.button_addFriend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Notandi ýtti á floating action button.");
                DialogFragment dialog = new NewFriendDialogFragment();
                dialog.show(getSupportFragmentManager(),"newFriend");
            }
        });

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.nav_friendList);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(FriendListActivity.this, CalendarActivity.class);
                        startActivity(cal);
                        return true;
                    case R.id.nav_todolists:
                        Intent todo = new Intent(FriendListActivity.this, TodoListActivity.class);
                        startActivity(todo);
                        return true;
                    case R.id.nav_home:
                        Intent home = new Intent(FriendListActivity.this, HomeActivity.class);
                        startActivity(home);
                    case R.id.nav_friendList:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDialogPositiveClick(String username) {
        addFriend(username);
    }

    public void addFriend(String username) {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.postAddFriend(username, new NetworkCallback<Friend>() {
            @Override
            public void onSuccess(Friend result) {
                friends.add(result);
                friendList.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorString) {
                Toast.makeText(FriendListActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getFriends() {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getFriends(new NetworkCallback<List<Friend>>() {
            @Override
            public void onSuccess(List<Friend> result) {
                friends = new ArrayList<>();
                //adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, friends);
                for(Friend friend: result) {
                    System.out.println("Vinur: " + friend.getName());
                    System.out.println("Streak: " + friend.getHighestStreak());
                    friends.add(friend);
                }
                adapter = new FriendListAdapter(friends, FriendListActivity.this);
                friendList.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorString) {
                Toast.makeText(FriendListActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
        });
    }
}