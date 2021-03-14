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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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
    private TodoListItem mNewItem;
    private ExpandableListView mListView;

    public ExpandableListAdapter(Context context, List<TodoList> todoLists,
                                 ExpandableListView listView) {
        this.mContext = context;
        this.mTodoLists = todoLists;
        this.mListView = listView;
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

        setHeaderColor(convertView, todoList);

        TextView listText = (TextView) convertView.findViewById(R.id.todolist_title);
        listText.setText(todoList.getName());

        Button newItemButton = (Button) convertView.findViewById(R.id.todolist_newitem);
        newItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mNewItem = new TodoListItem();
                mTodoLists.get(groupPosition).addItem(mNewItem);
                InputMethodManager imm =
                        (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                mListView.expandGroup(groupPosition);
                notifyDataSetChanged();
            }
        });

        Button favoriteList = (Button) convertView.findViewById(R.id.todolist_favorite);
        if(todoList.isFavorite()) {
            favoriteList.setBackgroundResource(R.drawable.ic_baseline_star_favorite);
        } else {
            favoriteList.setBackgroundResource(R.drawable.ic_baseline_star_not_favorite);
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

        setItemColor(convertView, (TodoList) getGroup(groupPosition));

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
                    if(text.length() == 0 && todoListItem != mNewItem) {
                        mTodoLists.get(groupPosition).getItems().remove(childPosition);
                        notifyDataSetChanged();
                    } else {
                        todoListItem.setDescription(text);
                        mNewItem = null;
                    }
                    itemText.clearFocus();
//                    InputMethodManager imm =
//                            (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);
                } else {
                    itemText.requestFocus();
                    itemText.setSelection(itemText.getText().length());
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

        if(todoListItem == mNewItem) {
            itemText.requestFocus();
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setHeaderColor(View view, TodoList list) {
        switch (list.getColor()) {
            case "yellow":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_darker));
                break;
            case "orange":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.orange_darker));
                break;
            case "red":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.red_darker));
                break;
            case "green":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.green_darker));
                break;
            case "blue":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.blue_darker));
                break;
            case "pink":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.pink_darker));
                break;
            case "purple":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.purple_darker));
                break;
        }
    }

    public void setItemColor(View view, TodoList list) {
        switch (list.getColor()) {
            case "yellow":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_lighter));
                break;
            case "orange":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.orange_lighter));
                break;
            case "red":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.red_lighter));
                break;
            case "green":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.green_lighter));
                break;
            case "blue":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.blue_lighter));
                break;
            case "pink":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.pink_lighter));
                break;
            case "purple":
                view.setBackgroundColor(mContext.getResources().getColor(R.color.purple_lighter));
                break;
        }
    }
}
