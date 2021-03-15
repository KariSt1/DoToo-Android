package is.hi.hbv601g.dotoo.Fragments;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import is.hi.hbv601g.dotoo.R;


public class NewTodoListDialogFragment extends DialogFragment {


    public interface NoticeDialogListener {
        public void onDialogPositiveClick(String name, String color);
    }

    NewTodoListDialogFragment.NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewTodoListDialogFragment.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_todo_list_dialog, null);

       final EditText mTitle = (EditText)view.findViewById(R.id.new_todo_list_title);
       System.out.println("Titill á nýja todolistanum er:" + mTitle);

        //get the spinner from the xml.
        Spinner dropdown = (Spinner)view.findViewById(R.id.color_list);
        //create a list of items for the spinner.
        String[] items = new String[]{"yellow", "red", "green", "blue", "pink", "purple"};
        //create an adapter to describe how the items are displayed
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        //dropdown.setAdapter(adapter);

        builder.setTitle(R.string.dialog_newTodolist)
                .setView(view)

                // Add action buttons
                .setPositiveButton(R.string.save_todo_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String title = mTitle.getText().toString();
                        String color = "purple_darker";
                        boolean favorite = false;
                        mListener.onDialogPositiveClick(title, color);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTodoListDialogFragment.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }
}