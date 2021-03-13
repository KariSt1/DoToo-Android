package is.hi.hbv601g.dotoo.client;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<TodoList> mTodoLists;

    public ExpandableListAdapter(Context context, List<TodoList> todoLists) {
        this.mContext = context;
        this.mTodoLists = todoLists;
    }

    @Override
    public int getGroupCount() {
        return this.mTodoLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mTodoLists.get(groupPosition).getItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mTodoLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mTodoLists.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TodoList todoList = (TodoList) getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.todolist_group, null);
        }

        TextView listText = (TextView) convertView.findViewById(R.id.todolist_title);
        listText.setText(todoList.getName());

        Button favoriteList = (Button) convertView.findViewById(R.id.todolist_favorite);
        if(todoList.isFavorite()) {
            favoriteList.setBackgroundResource(R.drawable.ic_baseline_star_not_favorite);
        } else {
            favoriteList.setBackgroundResource(R.drawable.ic_baseline_star_favorite);
        }

        favoriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoList.setFavorite(!todoList.isFavorite());
                if(todoList.isFavorite()) {
                    favoriteList.setBackgroundResource(R.drawable.ic_baseline_star_not_favorite);
                } else {
                    favoriteList.setBackgroundResource(R.drawable.ic_baseline_star_favorite);
                }
                notifyDataSetChanged();
            }
        });

        Button deleteList = (Button) convertView.findViewById(R.id.todolist_delete);
        deleteList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mTodoLists.remove(groupPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TodoListItem todoListItem = (TodoListItem) getChild(groupPosition, childPosition);

        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.todolist_item, null);
        }

        CheckBox itemCheckBox = (CheckBox) convertView.findViewById(R.id.todolist_item_checkbox);
        itemCheckBox.setChecked(todoListItem.getChecked());
        itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Erum í onCheckedChanged");
                if(buttonView.isPressed()) {
                    todoListItem.setChecked(!todoListItem.getChecked());
                }

            }
        });

        EditText itemText = (EditText) convertView.findViewById(R.id.todolist_item_text);
        itemText.setText(todoListItem.getDescription());
        itemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("Erum í onFocusChange");
                if(!hasFocus) {
                    System.out.println("Erum í if-setningu í onFocusChange");
                    String text = itemText.getText().toString();
                    if(text.length() == 0) {
                        mTodoLists.get(groupPosition).getItems().remove(childPosition);
                        notifyDataSetChanged();
                    } else {
                        todoListItem.setDescription(text);
                    }
                    //InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);
                }
            }
        });

        Button deleteItemButton = (Button) convertView.findViewById(R.id.todolist_item_delete);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mTodoLists.get(groupPosition).getItems().remove(childPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
