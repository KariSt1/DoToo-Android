package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;

    //networking
    User userNetworking;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // networking
        NetworkManager networkManager = NetworkManager.getInstance(this);

        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                networkManager.postLogin(new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        userNetworking = result;
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        User user = networkManager.getUser();
                        i.putExtra("is.hi.hbv601g.dotoo.user_result", user.getName());
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(String errorString) {
                        Log.d(TAG, "Failed to get todolists: " + errorString);
                    }
                }, mUsername.getText().toString(), mPassword.getText().toString());


            }
        });
    }
}