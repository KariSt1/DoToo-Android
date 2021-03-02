package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
                System.out.print("Ýtt á signup");

                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                String url = "http://10.0.2.2:8080/signup";
                //Gson gson = new Gson();
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
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Fengum villu, notandi líklega til");
                        error.printStackTrace();
                    }
                });
                queue.add(jsonObjectRequest);
            }
        });
    }
}
