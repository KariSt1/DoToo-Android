package is.hi.hbv601g.dotoo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import is.hi.hbv601g.dotoo.Fragments.NewFriendDialogFragment;
import is.hi.hbv601g.dotoo.Fragments.NewTodoListDialogFragment;
import is.hi.hbv601g.dotoo.R;

public class FriendListActivity extends AppCompatActivity implements NewFriendDialogFragment.NoticeDialogListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

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
    public void onDialogPositiveClick(String name) {
        System.out.println("Ýtt á add friend");
    }
}