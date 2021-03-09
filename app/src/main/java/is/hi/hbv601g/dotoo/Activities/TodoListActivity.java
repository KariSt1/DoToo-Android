package is.hi.hbv601g.dotoo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.R;
import android.view.View;
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
    List<TodoList> mTodoLists;
    HashMap<TodoList, List<TodoListItem>> mTodoListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Er í TodoListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.nav_todolists);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println("on navigation item selected");
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(TodoListActivity.this, TodoListActivity.class);
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
        mTodoListView = (ExpandableListView) findViewById(R.id.todolist_expandableList);

        //TODO: Get request fyrir todolista
        prepareListData();

        mListAdapter = new ExpandableListAdapter(this, mTodoLists, mTodoListItems);

        mTodoListView.setAdapter(mListAdapter);
    }

    private void prepareListData() {
        mTodoLists = new ArrayList<TodoList>();
        mTodoListItems = new HashMap<TodoList, List<TodoListItem>>();

        // Adding child data
        for(int i=0;i<3;i++) {
            TodoList list = new TodoList();
            list.setId(i);
            list.setName("List " + i);
            mTodoLists.add(list);
            List<TodoListItem> items = new ArrayList<TodoListItem>();
            for(int j=0;j<5;j++) {
                TodoListItem item = new TodoListItem();
                item.setId(6*i+j);
                item.setDescription("Item " + (6*i+j));
                if(Math.random() > 0.5) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
                items.add(item);
            }
            mTodoListItems.put(mTodoLists.get(i), items);
        }
        System.out.println("TodoListar voru búnir til");
        System.out.println("Fyrsti listi: " + mTodoLists.get(0).getName());
    }
}