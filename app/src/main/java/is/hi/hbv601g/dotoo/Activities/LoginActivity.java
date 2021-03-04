package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // mUsername = (EditText) findViewById(R.id.login_username);
       // mPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.print("Ýtt á login");

                String url = "https://dotoo2.herokuapp.com/login";

                List<String> jsonResponses = new ArrayList<>();

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("user");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String username = jsonObject.getString("username");
                                String password = jsonObject.getString("password");

                                jsonResponses.add(username);
                                jsonResponses.add(password);
                            }
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("is.hi.hbv601g.dotoo.user_result", "fakeUser");
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println("Fengum villu við að logga inn");
                    }
                });

                requestQueue.add(jsonObjectRequest);

            }
        });
    }
}