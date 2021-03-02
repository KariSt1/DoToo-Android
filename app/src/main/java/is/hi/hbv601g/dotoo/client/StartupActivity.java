package is.hi.hbv601g.dotoo.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import is.hi.hbv601g.dotoo.R;

public class StartupActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        mLoginButton = (Button) findViewById(R.id.button_startup_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        mSignupButton = (Button) findViewById(R.id.button_startup_signup);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartupActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

    }
}