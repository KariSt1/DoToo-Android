package is.hi.hbv601g.dotoo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.R;
import is.hi.hbv601g.dotoo.client.ExpandableListAdapter;

public class TodoListActivity extends AppCompatActivity {
    protected BottomNavigationView navigationView;

    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    List<Button> mFavoriteButtons;
    List<TodoList> mTodoLists;
    List<TodoList> mTodoListsPrufa; // prufa fyrir network
    private static final String TAG = "TodoListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // networking prufa

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getTodolist(new NetworkCallback<List<TodoList>>() {
            @Override
            public void onSuccess(List<TodoList> result) {
                mTodoListsPrufa = result;
                Log.d(TAG, "texti í todolista"+ mTodoListsPrufa.get(0).getItems().get(0).getDescription());
                System.out.println("Verið er að ná networking tenging.");

            }

            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "Failed to get todolists: " + errorString);
                System.out.println("Erum í onFailur frá networking tengingunni.");
            }
        });

        System.out.println("Er í TodoListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.nav_todolists);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println("on navigation item selected");
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(TodoListActivity.this, CalendarActivity.class);
                        startActivity(cal);
                        return true;
                    case R.id.nav_todolists:
                        return true;
                    case R.id.nav_home:
                        Intent home = new Intent(TodoListActivity.this, HomeActivity.class);
                        startActivity(home);
                }
                return false;
            }
        });

       /** toggleFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleFavorite.getBackground().equals(R.drawable.ic_baseline_star_favorite)) {
                    toggleFavorite.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_not_favorite));
                }
                else toggleFavorite.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_favorite));
            }
        });*/
        mTodoListView = (ExpandableListView) findViewById(R.id.todolist_expandableList);

        //TODO: Get request fyrir todolista
        prepareListData();

        mListAdapter = new ExpandableListAdapter(this, mTodoLists);

        mTodoListView.setAdapter(mListAdapter);
    }

    private void prepareListData() {
        mTodoLists = new ArrayList<TodoList>();
        mFavoriteButtons = new ArrayList<Button>();

        // Adding child data
        for(int i=0;i<5;i++) {
            TodoList list = new TodoList();
            list.setId(i);
            list.setName("List " + i);
            if(Math.random() > 0.5) {
                list.setFavorite(true);
            } else {
                list.setFavorite(false);
            }
            mTodoLists.add(list);
            List<TodoListItem> items = new ArrayList<TodoListItem>();
            for(int j=0;j<5;j++) {
                TodoListItem item = new TodoListItem();
                item.setId(5*i+j);
                item.setDescription("Item " + (5*i+j));
                if(Math.random() > 0.5) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
                items.add(item);
            }
            mTodoLists.get(i).setItems(items);
        }
        System.out.println("TodoListar voru búnir til");
        System.out.println("Fyrsti listi: " + mTodoLists.get(0).getName());
    }
}