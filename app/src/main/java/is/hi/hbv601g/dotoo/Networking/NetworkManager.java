package is.hi.hbv601g.dotoo.Networking;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.dotoo.Activities.HomeActivity;
import is.hi.hbv601g.dotoo.Activities.LoginActivity;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.User;


// sækir hluti frá networkinu og skilar til baka í gegnum callback
public class NetworkManager {

    private static final String BASE_URL = "https://dotoo2.herokuapp.com/";

    private static NetworkManager mInstance;
    private static RequestQueue mQueue;
    private Context mContext;
    private User mUser;

    public static synchronized NetworkManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    private NetworkManager(Context context){
        mContext = context;
        mQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue() {
        if(mQueue == null)
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mQueue;
    }

    public void getTodolist(final NetworkCallback<List<TodoList>> callback) {

        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "todolist",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            // getum hér vitnað í widged í viðmótinu okkar
                Gson gson = new Gson (); // nota til að yfirfæra strenginn okkar í object
                Type listType = new TypeToken<List<TodoList>>(){}.getType();
                List<TodoList> todoListBank = gson.fromJson(response, listType);
                System.out.println("Fyrsti todolisti: " + todoListBank.get(0).getName());
                callback.onSuccess(todoListBank);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());

            }
        }
        );
        mQueue.add(request); // volley sér um að keyra þetta request
    }

    public void postLogin(final NetworkCallback<User> callback, String username, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(json);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "login", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mUser = new User();
                try {
                    mUser.setName(response.getString("name"));
                    mUser.setPassword(response.getString("password"));
                    mUser.setUsername(response.getString("username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(mUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Fengum error í login");
                error.printStackTrace();
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    public User getUser() {
        return mUser;
    }

}
