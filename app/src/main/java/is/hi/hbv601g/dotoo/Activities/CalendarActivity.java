package is.hi.hbv601g.dotoo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;


import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.dotoo.Fragments.NewEventDialogFragment;

import is.hi.hbv601g.dotoo.Model.Event;
import is.hi.hbv601g.dotoo.Networking.NetworkCallback;
import is.hi.hbv601g.dotoo.Networking.NetworkManager;
import is.hi.hbv601g.dotoo.R;

public class CalendarActivity extends AppCompatActivity implements WeekView.EventLongPressListener, MonthLoader.MonthChangeListener, NewEventDialogFragment.NoticeDialogListener {

    protected BottomNavigationView navigationView;
    private WeekView mWeekView;
    ArrayList<WeekViewEvent> mNewEvents;
    List<Event> mEvents = new ArrayList<Event>();
    private static final String TAG = "CalendarActivity";
    FloatingActionButton mEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NetworkManager networkManager = NetworkManager.getInstance(this);
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
                Log.d(TAG, "Failed to get events: " + errorString);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mEventButton = (FloatingActionButton) findViewById(R.id.button_newEvent);
        mEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new NewEventDialogFragment();
                dialog.show(getSupportFragmentManager(),"newEvent");
            }
        });

        /**
         * Navigation bar logic
         */
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.nav_calendar);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_calendar:
                        return true;
                    case R.id.nav_todolists:
                        Intent todo = new Intent(CalendarActivity.this, TodoListActivity.class);
                        startActivity(todo);
                        return true;
                    case R.id.nav_home:
                        Intent home = new Intent(CalendarActivity.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.nav_friendList:
                        Intent friends = new Intent(CalendarActivity.this, FriendListActivity.class);
                        startActivity(friends);
                        return true;
                }
                return false;
            }
        });

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
      //  mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        mWeekView.setEventLongPressListener(this);

        //Format date and time
        setupDateTimeInterpreter(false);


        // Initially, there will be no events on the week view because the user has not tapped on
        // it yet.
        mNewEvents = new ArrayList<WeekViewEvent>();

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

    /**
     * Delete event if clicked on it
     * @param event, the event that was clicked on
     * @param eventRect coordinates of the event
     */
    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle(R.string.delete_event);
        alertDialogBuilder
                .setMessage(R.string.delete_event_confirm);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mNewEvents.remove(event);
                NetworkManager networkManager = NetworkManager.getInstance(CalendarActivity.this);
                try {
                    networkManager.deleteEvent(event.getId());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                mWeekView.notifyDatasetChanged();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    /**
     * Makes new event with the data that the user
     * puts in the dialog to make a new event
     * @param title the title of the new event
     * @param startDate the startdate and time of the new event
     * @param endDate the enddate and time of the new event
     */
    @Override
    public void onDialogPositiveClick(String title, Calendar startDate, Calendar endDate, String color, boolean giveNotification) {
        // Make event
        Event mainEvent = new Event();
        mainEvent.setTitle(title);
        mainEvent.setStartDate(startDate);
        mainEvent.setEndDate(endDate);
        mainEvent.setColor(color);

        mEvents.add(mainEvent);
        NetworkManager networkManager = NetworkManager.getInstance(this);
        try {
            networkManager.newEvent(mainEvent);
        } catch (Exception e) {
            System.out.println(e.toString());
        }



        WeekViewEvent event = new WeekViewEvent(5,title, startDate, endDate);
        setEventColor(mainEvent,event);

        mNewEvents.add(event);
        // Refresh the week view. onMonthChange will be called again.
        mWeekView.notifyDatasetChanged();

        if(giveNotification) {
            System.out.println("Erum að fara að setja hér á notification.");
            addNotification(this,"My Notification", startDate);

        }
    }
    private void addNotification(Context context, String message, Calendar startDate) {

        int icon = R.drawable.ic_dotoo_blue;
        long when = System.currentTimeMillis();

        System.out.println("Notificationið á að koma klukkutíma fyrir: " + startDate.toString());
        String appname = context.getResources().getString(R.string.app_name);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String NOTIFICATION_CHANNEL_ID = "4655";
            //Notification Channel
            CharSequence NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(icon)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(null)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent resultIntent = new Intent(context, CalendarActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(CalendarActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
        }


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