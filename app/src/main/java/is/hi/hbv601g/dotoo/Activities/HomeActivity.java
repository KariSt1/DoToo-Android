package is.hi.hbv601g.dotoo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.dotoo.Model.Event;
import is.hi.hbv601g.dotoo.Model.TodoList;
import is.hi.hbv601g.dotoo.Model.TodoListItem;
import is.hi.hbv601g.dotoo.Model.User;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;
import is.hi.hbv601g.dotoo.Adapters.ExpandableListAdapter;

public class HomeActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {

    ExpandableListAdapter mListAdapter;
    ExpandableListView mTodoListView;
    TextView mStreakView;
    TextView mQuoteView;
    protected BottomNavigationView navigationView;
    List<TodoList> mFavouriteLists;
    List<Long> mDeletedListIds;
    List<TodoList> mChangedTodoLists = new ArrayList<TodoList>();
    private WeekView mWeekView;
    ArrayList<WeekViewEvent> mNewEvents;
    List<Event> mEvents = new ArrayList<Event>();
    CalendarActivity cal = new CalendarActivity();
    Button mSignoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NetworkManager networkManager = NetworkManager.getInstance(this);
        User user = networkManager.getUser();
        networkManager.getQuoteOfDay(new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mQuoteView = (TextView) findViewById(R.id.quoteText);
                mQuoteView.setText(result);
            }

            @Override
            public void onFailure(String errorString) {
                System.out.println("failed to get quote of day");
            }
        });
        networkManager.getTodolist(true, new NetworkCallback<List<TodoList>>() {
            @Override
            public void onSuccess(List<TodoList> result) {
                mFavouriteLists = result;

                mTodoListView = (ExpandableListView) findViewById(R.id.home_expandableList);

                mDeletedListIds = new ArrayList<Long>();

                mStreakView = (TextView) findViewById(R.id.list_streak);
                mStreakView.setText(Integer.toString(user.getmStreak()));

                mListAdapter = new ExpandableListAdapter(HomeActivity.this, mFavouriteLists, mDeletedListIds, mChangedTodoLists, mTodoListView, user, mStreakView);

                mTodoListView.setAdapter(mListAdapter);

            }

            @Override
            public void onFailure(String errorString) {
                System.out.println("Erum í onFailure í homeActivity.");
            }
        });

        networkManager.getEvents(new NetworkCallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> result) {
                mEvents = result;

                for (Event event : mEvents) {
                    WeekViewEvent weekViewEvent = new WeekViewEvent();
                    weekViewEvent.setName(event.getTitle());
                    weekViewEvent.setStartTime(event.getStartDate());
                    weekViewEvent.setEndTime(event.getEndDate());
                    weekViewEvent.setId(event.getId());
                    setEventColor(event,weekViewEvent);

                    mNewEvents.add(weekViewEvent);
                }

                mWeekView.notifyDatasetChanged();
            }

            @Override
            public void onFailure(String errorString) {
                System.out.println("Failed to get events");
            }
        });

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.nav_home);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        Intent cal = new Intent(HomeActivity.this, CalendarActivity.class);
                        startActivity(cal);
                        return true;
                    case R.id.nav_todolists:
                        mListAdapter.sendChanges(networkManager);
                        try
                        {
                            Thread.sleep(150);
                        }
                        catch(InterruptedException ex)
                        {
                            Thread.currentThread().interrupt();
                        }
                        Intent todo = new Intent(HomeActivity.this, TodoListActivity.class);
                        startActivity(todo);
                        return true;
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_friendList:
                        mListAdapter.sendChanges(networkManager);
                        Intent friends = new Intent(HomeActivity.this, FriendListActivity.class);
                        startActivity(friends);
                        return true;
                }
                return false;
            }
        });

        mWeekView = (WeekView) findViewById(R.id.weekViewHome);

        mWeekView.setMonthChangeListener(this);


        //Format date and time
        setupDateTimeInterpreter(false);

        mNewEvents = new ArrayList<WeekViewEvent>();

        // Signout logic
        mSignoutButton = findViewById(R.id.signout_button);
        mSignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startup = new Intent(HomeActivity.this, StartupActivity.class);
                startActivity(startup);
            }
        });
    }

    @Override
    protected void onStop() {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        mListAdapter.sendChanges(networkManager);
        super.onStop();
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with the events that was added.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        ArrayList<WeekViewEvent> newEvents = getNewEvents(newYear, newMonth);
        events.addAll(newEvents);
        return events;
    }

    /**
     * Get events that were added by tapping on empty view.
     * @param year The year currently visible on the week view.
     * @param month The month currently visible on the week view.
     * @return The events of the given year and month.
     */
    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {

        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Changes the date format on the calendar
     * @param shortDate false if the date needs to be formatted
     * @return formatted date
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d/M", Locale.getDefault());

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            /**
             * Changes the time format on the calendar
             * @param hour the hour that is being formatted
             * @return formatted time
             */
            @Override
            public String interpretTime(int hour) {
                if (hour == 24) hour = 0;
                if (hour == 0) hour = 0;
                return hour + ":00";
            }
        });
    }

    public void setEventColor(Event event, WeekViewEvent weekViewEvent) {
        switch (event.getColor()) {
            case "yellow":
                weekViewEvent.setColor(getResources().getColor(R.color.yellow_darker));
                break;
            case "orange":
                weekViewEvent.setColor(getResources().getColor(R.color.orange_darker));
                break;
            case "red":
                weekViewEvent.setColor(getResources().getColor(R.color.red_darker));
                break;
            case "green":
                weekViewEvent.setColor(getResources().getColor(R.color.green_darker));
                break;
            case "blue":
                weekViewEvent.setColor(getResources().getColor(R.color.blue_darker));
                break;
            case "pink":
                weekViewEvent.setColor(getResources().getColor(R.color.pink_darker));
                break;
            case "purple":
                weekViewEvent.setColor(getResources().getColor(R.color.purple_darker));
                break;
        }
    }

}
