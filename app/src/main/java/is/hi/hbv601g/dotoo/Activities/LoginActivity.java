package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;

    //networking
    User userPrufa; // prufa fyrir network
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
                System.out.print("Ýtt á login");


                networkManager.postLogin(new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        userPrufa = result;
                        Log.d(TAG, "texti í todolista"+ userPrufa.getName());

                    }

                    @Override
                    public void onFailure(String errorString) {
                        Log.d(TAG, "Failed to get todolists: " + errorString);
                    }
                }, mUsername.getText().toString(), mPassword.getText().toString());

                /*
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url = "https://dotoo2.herokuapp.com/login";


                JSONObject json = new JSONObject();
                try {
                    json.put("username", mUsername.getText().toString());
                    json.put("password", mPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        String user = "Vitlaust nafn";
                         try {
                            user = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        i.putExtra("is.hi.hbv601g.dotoo.user_result", user);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Fengum error í login");
                        error.printStackTrace();
                    }
                });

                queue.add(jsonObjectRequest);
*/


            }
        });
    }
}