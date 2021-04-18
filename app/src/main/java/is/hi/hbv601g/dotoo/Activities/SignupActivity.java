package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import is.hi.hbv601g.dotoo.Model.User;

public class SignupActivity extends AppCompatActivity {

    private Button mSignupButton;
    private EditText mName;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        NetworkManager networkManager = NetworkManager.getInstance(this);

        mName = (EditText) findViewById(R.id.signup_name) ;
        mUsername = (EditText) findViewById(R.id.signup_username);
        mPassword =  (EditText) findViewById(R.id.signup_password);
        mSignupButton = (Button) findViewById(R.id.button_signup);
        mSignupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mPassword.getText().toString().equals("")) {
                    mPassword.setError(getString(R.string.signup_no_password));
                    mPassword.requestFocus();
                }
                else if(mPassword.getText().toString().length() < 8) {
                    mPassword.setError(getString(R.string.signup_password_incorrect_length));
                    mPassword.requestFocus();
                } else {
                    User user = new User(mUsername.getText().toString(), mName.getText().toString(), mPassword.getText().toString());
                    networkManager.postSignup(new NetworkCallback<User>() {
                        @Override
                        public void onSuccess(User result) {
                            Toast.makeText(SignupActivity.this,"Thank you for signing up " + result.getName() + "!",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignupActivity.this, HomeActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(String errorString) {
                            mUsername.setError(getString(R.string.toast_signup_username_taken));
                            mUsername.requestFocus();
                        }
                    }, user);
                }
            }
        });
    }
}
