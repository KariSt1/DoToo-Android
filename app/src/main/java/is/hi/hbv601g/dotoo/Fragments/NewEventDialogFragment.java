package is.hi.hbv601g.dotoo.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import is.hi.hbv601g.dotoo.R;

public class NewEventDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(String title, Calendar startTime, Calendar endTime);
    }

    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_event_dialog, null);

        final EditText mTitle = (EditText)view.findViewById(R.id.new_event_title);
        final EditText mStartDate = (EditText) view.findViewById(R.id.new_event_startDate);
        final Button mStartTime = (Button) view.findViewById(R.id.new_event_startTime);
        final Button mEndDate = (Button) view.findViewById(R.id.new_event_endDate);
        final Button mEndTime = (Button)view.findViewById(R.id.new_event_endTime);
        Calendar sd = Calendar.getInstance();
        Calendar ed = Calendar.getInstance();

        // Show a datepicker when the dateButton is clicked
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //sd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //sd.set(Calendar.MONTH, month+1);
                        //sd.set(Calendar.YEAR, year);
                        sd.set(year,month,dayOfMonth);
                        mStartDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }
                    }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                dpd.show();
            }
        });

        // Show a timepicker when the dateButton is clicked
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar now = Calendar.getInstance();
                final Calendar cal = Calendar.getInstance();

                TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        sd.set(Calendar.HOUR_OF_DAY, hour);
                        sd.set(Calendar.MINUTE, minute);
                        mStartTime.setText(hour + ":" + minute);
                    }
                }, cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),true);
                tpd.show();
            }
        });

        // Show a datepicker when the dateButton is clicked
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar now = Calendar.getInstance();
                final Calendar cal = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //ed.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //ed.set(Calendar.MONTH, month+1);
                        //ed.set(Calendar.YEAR, year);
                        ed.set(year,month-1,dayOfMonth);
                        mEndDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }
                }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                dpd.show();
            }
        });

        // Show a timepicker when the dateButton is clicked
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar now = Calendar.getInstance();
                final Calendar cal = Calendar.getInstance();

                TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        ed.set(Calendar.HOUR_OF_DAY, hour);
                        ed.set(Calendar.MINUTE, minute);
                        mEndTime.setText(hour + ":" + minute);

                    }
                }, cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),true);
                tpd.show();
            }
        });

        builder.setTitle(R.string.dialog_newEvent)
                .setView(view)

                // Add action buttons
                .setPositiveButton(R.string.save_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String title = mTitle.getText().toString();
                        mListener.onDialogPositiveClick(title, sd, ed);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewEventDialogFragment.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }

}