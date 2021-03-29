package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        mName = (EditText) findViewById(R.id.signup_name) ;
        mUsername = (EditText) findViewById(R.id.signup_username);
        mPassword =  (EditText) findViewById(R.id.signup_password);
        mSignupButton = (Button) findViewById(R.id.button_signup);
        mSignupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO: Move to NetworkManager

                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                String url = "https://dotoo2.herokuapp.com/signup";

                if(mPassword.getText().toString().equals("")) {
                    mPassword.setError(getString(R.string.signup_no_password));
                    mPassword.requestFocus();
                }
                else if(mPassword.getText().toString().length() < 8) {
                    mPassword.setError(getString(R.string.signup_password_incorrect_length));
                    mPassword.requestFocus();
                } else {
                    User user = new User(mUsername.getText().toString(), mName.getText().toString(), mPassword.getText().toString());
                    //String json = gson.toJson(user);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("username", user.getUsername());
                        json.put("name", user.getName());
                        json.put("password", user.getPassword());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response);
                            Toast.makeText(SignupActivity.this,R.string.thank_you_sign_up + user.getName() + "!",Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mUsername.setError(getString(R.string.toast_signup_username_taken));
                            mUsername.requestFocus();
                            error.printStackTrace();
                        }
                    });
                    queue.add(jsonObjectRequest);
                }
            }
        });
    }
}
