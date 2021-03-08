package is.hi.hbv601g.dotoo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    List<TodoList> mTodoLists;
    HashMap<TodoList, List<TodoListItem>> mTodoListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Er í TodoListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

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