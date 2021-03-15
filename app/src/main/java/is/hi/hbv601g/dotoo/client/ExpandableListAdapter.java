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

import is.hi.hbv601g.dotoo.Activities.TodoListActivity;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.R;

/**
 * Class that takes care of all functionality when interacting with todolists on the UI.
 *
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<TodoList> mTodoLists;
    private List<Long> mDeletedListIds;
    private List<TodoList> mChangedTodoLists;
    private TodoListItem mNewItem;
    private ExpandableListView mListView;

    /**
     * Constructor
     * @param context Current state of the application
     * @param todoLists Users todolists
     * @param deletedLists List of ids of todolists that have been deleted
     * @param changedTodoLists List of todolists that have been changed
     * @param listView ExpandableListView that the todolists are in
     */
    public ExpandableListAdapter(Context context, List<TodoList> todoLists, List<Long> deletedLists,
                                 List<TodoList> changedTodoLists, ExpandableListView listView) {
        this.mContext = context;
        this.mTodoLists = todoLists;
        this.mListView = listView;
        this.mDeletedListIds = deletedLists;
        this.mChangedTodoLists = changedTodoLists;
    }

    /**
     * Gets the number of todolists and returns it
     * @return number of todolists
     */
    @Override
    public int getGroupCount() {
        return this.mTodoLists.size();
    }

    /**
     * Gets the number of items for one todolist and returns it
     * @param groupPosition todolist position in the list of todolists
     * @return number of items for a todolist
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mTodoLists.get(groupPosition).getItems().size();
    }

    /**
     * Gets a todolist
     * @param groupPosition todolist position in the list of todolists
     * @return TodoList
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.mTodoLists.get(groupPosition);
    }

    /**
     * Gets an item in a todolist
     * @param groupPosition todolist position in the list of todolists
     * @param childPosition TodoListItem position in the list of todolist items
     * @return TodoListItem
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mTodoLists.get(groupPosition).getItems().get(childPosition);
    }

    /**
     * Gets the position of a todolist
     * @param groupPosition todolist position in the list of todolists
     * @return int, todolist position in the list of todolists
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets the position of a TodoListItem
     * @param groupPosition todolist position in the list of todolists
     * @param childPosition TodoListItem position in the list of TodoListItems
     * @return int, TodoListItem position in the list of TodoListItems
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Indicates whether group and child ids are stable across changes to the underlying data
     * @return false
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Gets the ids of the TodoLists that have been deleted
     * @return List of ids of deleted todolists
     */
    public List<Long> getDeletedLists() {
        return this.mDeletedListIds;
    }

    /**
     * Sets the list of deleted TodoLists
     * @param mDeletedLists list of deleted TodoLists
     */
    public void setDeletedLists(List<Long> mDeletedLists) {
        this.mDeletedListIds = mDeletedLists;
    }

    /**
     * Gets a View that displays the given group.
     * @param groupPosition todolist position in the list of todolists
     * @param isExpanded if the given todolist is expanded or not
     * @param convertView the old View to use if it exists
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
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
                // Show keyboard when adding an item so the user can edit it
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
                todoListChanged(todoList);
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
                List<Long> currentDeleted = getDeletedLists();
                currentDeleted.add(mTodoLists.get(groupPosition).getId());
                setDeletedLists(currentDeleted);
                mTodoLists.remove(groupPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     *
     * @param groupPosition todolist position in the list of todolists
     * @param childPosition TodoListItem position in the list of TodoListItems
     * @param isLastChild boolean: whether the TodoListItem is the last child within the TodoList
     * @param convertView the old View to use if it exists
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
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
                if(buttonView.isPressed()) {
                    todoListItem.setChecked(!todoListItem.getChecked());
                    todoListChanged(mTodoLists.get(groupPosition));
                }

            }
        });


        EditText itemText = (EditText) convertView.findViewById(R.id.todolist_item_text);
        itemText.setText(todoListItem.getDescription());
        itemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String text = itemText.getText().toString();
                    // If EditText has lost focus, set the text to the corresponding
                    // TodoListItem or delete it if the EditText field is empty
                    if(text.length() == 0 && todoListItem != mNewItem) {
                        mTodoLists.get(groupPosition).getItems().remove(childPosition);
                        notifyDataSetChanged();
                    } else {
                        todoListItem.setDescription(text);
                        mNewItem = null;
                    }
                    todoListChanged(mTodoLists.get(groupPosition));
                    itemText.clearFocus();
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
                todoListChanged(mTodoLists.get(groupPosition));
                notifyDataSetChanged();
            }
        });

        if(todoListItem == mNewItem) {
            itemText.requestFocus();
        }

        return convertView;
    }

    /**
     * Whether the child in the specified position is selectable
     * @param groupPosition todolist position in the list of todolists
     * @param childPosition TodoListItem position in the list of TodoListItems
     * @return boolean, whether the child in the specified position is selectable
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Sets the header color for a TodoList on the View
     * @param view View for  the TodoList
     * @param list TodoList
     */
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

    /**
     * Sets the item color for a TodoList on the View
     * @param view View for the TodoList
     * @param list TodoList
     */
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

    /**
     * Checks if a TodoList has already been changed, if not,
     * add it to the list of changed TodoLists
     * @param changedTodoList TodoList that has been changed
     */
    public void todoListChanged(TodoList changedTodoList) {
        for(TodoList list: mChangedTodoLists) {
            if (list == changedTodoList) {
                return;
            }
        }
        mChangedTodoLists.add(changedTodoList);
    }
}
