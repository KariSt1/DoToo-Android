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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.dotoo.Activities.HomeActivity;
import is.hi.hbv601g.dotoo.Activities.LoginActivity;
import is.hi.hbv601g.dotoo.Activities.SignupActivity;
import is.hi.hbv601g.dotoo.Model.Event;
import is.hi.hbv601g.dotoo.Model.Friend;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.R;


// sækir hluti frá networkinu og skilar til baka í gegnum callback
public class NetworkManager {

    private static final String BASE_URL = "https://dotoo2.herokuapp.com/";
    // Nota þennan BASE_URL fyrir local database:
    //private static final String BASE_URL = "http://10.0.2.2:8080/";


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

        String requestURL = String.format(BASE_URL + "todolist?username=%1$s&password=%2$s", mUser.getUsername(), mUser.getPassword());
        if(isFavorite) requestURL = String.format(BASE_URL + "favoritetodolists?username=%1$s&password=%2$s", mUser.getUsername(), mUser.getPassword());

        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject test = (JSONObject) response.get(0);
                    System.out.println("Response" +  response.get(0));
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                List<TodoList> todoLists = new ArrayList<TodoList>();
                Gson gson = new Gson(); // nota til að yfirfæra strenginn okkar í object
                Type listType = new TypeToken<List<TodoList>>(){}.getType();
                List<TodoList> todoListBank = gson.fromJson(response.toString(), listType);

                for(int i = 0; i < todoListBank.size(); i++) {
                    todoListBank.get(i).setUser(mUser);
                }
                callback.onSuccess(todoListBank);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    public void deleteTodolist(List<Long> deletedLists) {
        System.out.println("Deleted list size: " + deletedLists.size());
        JSONObject json = new JSONObject();
        try {
            json.put("username", mUser.getUsername());
            json.put("password", mUser.getPassword());
            json.put("todolists", deletedLists);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "deletelists", json, response -> {
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                if (response.data == null || response.data.length == 0) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        };
        mQueue.add(request); // volley sér um að keyra þetta request
    }

    public void postTodolists(List<TodoList> changedTodoLists) {
        Gson gson = new Gson(); // nota til að yfirfæra strenginn okkar í object
        String jsonString = gson.toJson(changedTodoLists);

        JSONArray json = new JSONArray();
        try {
            json = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String uri = String.format(BASE_URL + "todolist?username=%1$s&password=%2$s&finishedTodoLists=%3$s", mUser.getUsername(), mUser.getPassword(), mUser.getmStreak());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, uri, json, response -> {
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

                if (response.data == null || response.data.length == 0) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        };
        mQueue.add(request); // volley sér um að keyra þetta request
    }

    public void postSignup(final NetworkCallback<User> callback, User user) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", user.getUsername());
            json.put("name", user.getName());
            json.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/signup", json, new Response.Listener<JSONObject>() {
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
                callback.onFailure("Username taken");
                error.printStackTrace();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void postLogin(final NetworkCallback<User> callback, String username, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "login", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mUser = new User();
                try {
                    mUser.setName(response.getString("name"));
                    mUser.setPassword(response.getString("password"));
                    mUser.setUsername(response.getString("username"));
                    mUser.setmStreak(response.getInt("finishedTodoLists"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(mUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, R.string.inc_pass_usern, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    public User getUser() {
        return mUser;
    }


    
    public void getEvents(final NetworkCallback<List<Event>> callback) {
        String requestURL = String.format(BASE_URL + "events?username=%1$s&password=%2$s", mUser.getUsername(), mUser.getPassword());

        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Calendar.class, new CalendarFromTimestampJsonDeserializer());
                Gson gson = builder.create();
                List<Event> eventBank = gson.fromJson(response.toString(), new TypeToken<List<Event>>(){}.getType());
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

    public void newEvent(Event newEvent, NetworkCallback<Event> callback) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date sd = newEvent.getStartDate().getTime();
        String strStartDate = dateFormat.format(sd);

        Date ed = newEvent.getEndDate().getTime();
        String strEndDate = dateFormat.format(ed);

        JSONObject json = new JSONObject();
        try {
            json.put("startDate",strStartDate );
            json.put("endDate",strEndDate );
            json.put("title",newEvent.getTitle() );
            json.put("color",newEvent.getColor() );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String uri = String.format(BASE_URL + "events?username=%1$s&password=%2$s", mUser.getUsername(), mUser.getPassword());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, uri, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Calendar.class, new CalendarFromTimestampJsonDeserializer());
                Gson gson = builder.create();
                Event eventBank = gson.fromJson(response.toString(), new TypeToken<Event>(){}.getType());
                callback.onSuccess(eventBank);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        mQueue.add(request); // volley sér um að keyra þetta request
    }

    public void deleteEvent(Long id) {

        JSONObject json = new JSONObject();
        try {
            json.put("username", mUser.getUsername());
            json.put("password", mUser.getPassword());
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "deleteEvent", json, response -> {
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                if (response.data == null || response.data.length == 0) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        };
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

    public void postAddFriend(String username, final NetworkCallback<Friend> callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("friendUsername", username );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String uri = String.format(BASE_URL + "addFriend?username=%1$s&password=%2$s", mUser.getUsername(), mUser.getPassword());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, uri, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("error")) {
                    try {
                        callback.onFailure(response.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Friend newFriend = new Friend(response.getString("friendUsername"),
                                                      response.getString("friendName"),
                                                      response.getInt("streak"));
                        callback.onSuccess(newFriend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void getFriends(final NetworkCallback<List<Friend>> callback) {

        String requestURL = String.format(BASE_URL + "friends?username=%1$s&password=%2$s", mUser.getUsername(), mUser.getPassword());
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson(); // nota til að yfirfæra strenginn okkar í object
                Type listType = new TypeToken<List<Friend>>(){}.getType();
                List<Friend> friends = gson.fromJson(response.toString(), listType);
                callback.onSuccess(friends);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFailure("Error getting friends");
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request

    }

    public void getQuoteOfDay(final NetworkCallback<String> callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://quotes.rest/qod?language=en", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String quote = "";
                try {
                    JSONArray quoteInfo = response.getJSONObject("contents").getJSONArray("quotes");
                    quote = quoteInfo.getJSONObject(0).getString("quote");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(quote);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFailure("Error getting quote of the day");
            }
        });
        mQueue.add(request); // volley sér um að keyra þetta request
    }
}
