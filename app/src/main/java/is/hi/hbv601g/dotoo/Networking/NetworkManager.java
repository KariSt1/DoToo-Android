package is.hi.hbv601g.dotoo.Networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;


// sækir hluti frá networkingu og skila til baka í gegnum callback
public class NetworkManager {

    private static final String BASE_URL = "https://dotoo2.herokuapp.com/";

    private static NetworkManager mInstance;
    private static RequestQueue mQueue;
    private Context mContext;

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
                Request.Method.GET, BASE_URL + "todolist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            // getum hér vitnað í widged í viðmótinu okkar
                Gson gson = new Gson (); // nota til að yfirfæra strenginn okkar í object
                Type listType = new TypeToken<List<TodoList>>(){}.getType();
                List<TodoList> todoListBank = gson.fromJson(response, listType);
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
}
