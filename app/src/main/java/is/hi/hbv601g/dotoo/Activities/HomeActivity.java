package is.hi.hbv601g.dotoo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.R;

public class HomeActivity extends AppCompatActivity {

    private TextView mWelcomeText;
    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("home activity on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mWelcomeText = (TextView) findViewById(R.id.welcomeUser);
        String user = getIntent().getStringExtra("is.hi.hbv601g.dotoo.user_result");
        mWelcomeText.setText(getString(R.string.welcome_user, user));

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        System.out.println("found navigation view");
        navigationView.setSelectedItemId(R.id.nav_home);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(navigationView.getSelectedItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(HomeActivity.this, TodoListActivity.class);
                        startActivity(cal);
                        return true;
                    case R.id.nav_todolists:
                        Intent todo = new Intent(HomeActivity.this, TodoListActivity.class);
                        startActivity(todo);
                        return true;
                    case R.id.nav_home:
                        return true;
                }
                return false;
            }
        });

    }
}
