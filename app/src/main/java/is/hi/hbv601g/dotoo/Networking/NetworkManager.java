package is.hi.hbv601g.dotoo.Networking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import is.hi.hbv601g.dotoo.Activities.HomeActivity;
import is.hi.hbv601g.dotoo.Activities.LoginActivity;
import is.hi.hbv601g.dotoo.Model.Event;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.Model.User;


// sækir hluti frá networkinu og skilar til baka í gegnum callback
public class NetworkManager {

     private static final String BASE_URL = "https://dotoo2.herokuapp.com/";


   // private static final String BASE_URL = "http://10.0.2.2:8080/";


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

    public void setContext(Context context) {
        mContext = context;
    }

    public RequestQueue getRequestQueue() {
        if(mQueue == null)
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mQueue;
    }

    public void getTodolist(boolean isFavorite, final NetworkCallback<List<TodoList>> callback) {

        JSONObject json = new JSONObject();
        try {
            json.put("username", mUser.getUsername());
            json.put("password", mUser.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);

        String requestURL = "todolist";
        if(isFavorite) requestURL = "favoritetodolists";

        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.POST, BASE_URL + requestURL, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject test = (JSONObject) response.get(0);
                    System.out.println("Response" +  response.get(0));
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                List<TodoList> todoLists = new ArrayList<TodoList>();
                Gson gson = new Gson (); // nota til að yfirfæra strenginn okkar í object
                Type listType = new TypeToken<List<TodoList>>(){}.getType();
                List<TodoList> todoListBank = gson.fromJson(response.toString(), listType);

                System.out.println("Todolistbank: " + todoListBank);
                System.out.println("Todolistbank items: " + todoListBank.get(0).getItems().get(0).getDescription());
                callback.onSuccess(todoListBank);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error við að fá todo lista");
                error.printStackTrace();
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    public void deleteTodolist(List<Long> deletedLists) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", mUser.getUsername());
            json.put("password", mUser.getPassword());
            json.put("todolists", deletedLists);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "deletelists", json, response -> {

            System.out.println("delete response " + response.toString());
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error við að deleta todo lista");
                error.printStackTrace();
            }
        });
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
                Toast.makeText(mContext,"Username and or password incorrect.", Toast.LENGTH_SHORT).show();
                System.out.println("Fengum error í login");
                error.printStackTrace();
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    public User getUser() {
        return mUser;
    }


    
    public void getEvents(final NetworkCallback<List<Event>> callback) {

        JSONObject json = new JSONObject();
        try {
            json.put("username", mUser.getUsername());
            json.put("password", mUser.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);

        String requestURL = "events";

        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.POST, BASE_URL + requestURL, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println("Response" +  response.get(0));
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson (); // nota til að yfirfæra strenginn okkar í object
                Type listType = new TypeToken<List<Event>>(){}.getType();
                List<Event> eventBank = gson.fromJson(response.toString(), listType);
                callback.onSuccess(eventBank);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    private class CustomJsonArrayRequest extends JsonRequest<JSONArray> {
        public CustomJsonArrayRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

}
