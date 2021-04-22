package is.hi.hbv601g.dotoo.Adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<TodoList> mTodoLists;
    private List<Long> mDeletedListIds;
    private List<TodoList> mChangedTodoLists;
    private TodoListItem mNewItem;
    private EditText mNewItemEditText;
    private ExpandableListView mListView;
    private User mUser;
    private TextView mStreakView;

    public ExpandableListAdapter(Context context, List<TodoList> todoLists, List<Long> deletedLists,
                                 List<TodoList> changedTodoLists, ExpandableListView listView,
                                 User user, TextView streakView) {
        this.mContext = context;
        this.mTodoLists = todoLists;
        this.mListView = listView;
        this.mDeletedListIds = deletedLists;
        this.mChangedTodoLists = changedTodoLists;
        this.mUser = user;
        this.mStreakView = streakView;
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

    public List<Long> getDeletedLists() {
        return this.mDeletedListIds;
    }

    public List<TodoList> getChangedLists() { return this.mChangedTodoLists; }

    public void setDeletedLists(List<Long> mDeletedLists) {
        this.mDeletedListIds = mDeletedLists;
    }

    public int updateStreak(TodoList list) {
        boolean wasFinished = list.isFinished();
        boolean finished = true;
        for(TodoListItem item : list.getItems()) {
            if(!item.getChecked()) {
                finished = false;
                break;
            }
        }

        list.setIsFinished(finished);
        int streak = finished ? 1 : 0;
        if(wasFinished && !finished) streak -= 1;
        return streak;
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
                mListView.expandGroup(groupPosition);
                mListView.setSelectedGroup(groupPosition);
                mNewItem = new TodoListItem();
                todoList.addItem(mNewItem);

                int streak = updateStreak(todoList);
                mUser.setmStreak(mUser.getmStreak() + streak);
                mStreakView.setText(Integer.toString(mUser.getmStreak()));
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
                mDeletedListIds.add(mTodoLists.get(groupPosition).getId());
                mChangedTodoLists.remove(mTodoLists.get(groupPosition));
                mTodoLists.remove(groupPosition);
                System.out.println("currently deleted" + getDeletedLists());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TodoListItem todoListItem = (TodoListItem) getChild(groupPosition, childPosition);
        TodoList todoList = (TodoList) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.todolist_item, null);
        }

        System.out.println("Group: " + groupPosition + " child: " + childPosition);
        setItemColor(convertView, todoList);

        CheckBox itemCheckBox = (CheckBox) convertView.findViewById(R.id.todolist_item_checkbox);
        itemCheckBox.setChecked(todoListItem.getChecked());
        itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isPressed()) {
                    todoListItem.setChecked(!todoListItem.getChecked());
                    todoListChanged(mTodoLists.get(groupPosition));

                    int streak = updateStreak(todoList);
                    mUser.setmStreak(mUser.getmStreak() + streak);
                    mStreakView.setText(Integer.toString(mUser.getmStreak()));
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
                    if(text.length() == 0) {
                        mTodoLists.get(groupPosition).getItems().remove(childPosition);
                        notifyDataSetChanged();
                    } else {
                        todoListItem.setDescription(text);
                        if(todoListItem == mNewItem) {
                            mNewItemEditText = null;
                            mNewItem = null;
                        }
                    }
                    todoListChanged(mTodoLists.get(groupPosition));
                    if(mListView.hasFocus()) {
                        InputMethodManager imm =
                            (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);
                    }
                } else {
                    InputMethodManager imm =
                            (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    imm.showSoftInput(mListView, InputMethodManager.SHOW_IMPLICIT);
                    itemText.setSelection(itemText.getText().length());
                }
            }
        });
        itemText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    mListView.requestFocus();
                    if(todoListItem == mNewItem) {
                        mNewItemEditText = null;
                        mNewItem = null;
                    }
                    InputMethodManager imm =
                            (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        Button deleteItemButton = (Button) convertView.findViewById(R.id.todolist_item_delete);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TodoList list = mTodoLists.get(groupPosition);
                TodoListItem item = list.getItems().get(childPosition);
                boolean checked = item.getChecked();
                list.getItems().remove(childPosition);

                int streak = updateStreak(list);
                if(checked && streak > 0) streak -= 1; // lazy fix for deletion
                mUser.setmStreak(mUser.getmStreak() + streak);
                mStreakView.setText(Integer.toString(mUser.getmStreak()));
                todoListChanged(mTodoLists.get(groupPosition));
                notifyDataSetChanged();
            }
        });

        if(mNewItem != null) {
            if(todoListItem == mNewItem) {
                mNewItemEditText = itemText;
                itemText.requestFocus();
            } else if(mNewItemEditText != null) {
                mNewItemEditText.requestFocus();
            }
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

    public void todoListChanged(TodoList changedTodoList) {
        System.out.println("Erum í todoListChanged");
        for(TodoList list: mChangedTodoLists) {
            if (list == changedTodoList) {
                return;
            }
        }
        mChangedTodoLists.add(changedTodoList);
        System.out.println("Todolista breytt, fjöldi breyttra: " + mChangedTodoLists.size());
    }

    public void newTodoList(String name, String color) {
        TodoList list = new TodoList();
        //list.setUser(mUser);
        list.setName(name);
        list.setColor(color);
        mTodoLists.add(list);
        mChangedTodoLists.add(list);
        notifyDataSetChanged();
    }

    public void sendChanges(NetworkManager networkManager) {
        if(mDeletedListIds.size() > 0) {
            System.out.println("Eyði listum");
            try {
                networkManager.deleteTodolist(mDeletedListIds);
                mDeletedListIds = new ArrayList<Long>();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if(mChangedTodoLists.size() > 0) {
            System.out.println("Sendi breytta lista");
            try {
                networkManager.postTodolists(mChangedTodoLists);
                mChangedTodoLists = new ArrayList<TodoList>();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
