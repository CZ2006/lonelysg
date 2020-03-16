package com.IrisBICS.lonelysg;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;

public class FragmentInvitation extends Fragment {
    // For dropdown box
    Spinner dropdownbox;
    String categories[] = {"Choose your invitation category", "Food and Drinks", "Movies", "Sports", "Study", "Others"};
    ArrayAdapter<String>arrayAdapter;

    //For time and date selection
    Button datePick, timePick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_invitation, container, false);

        // For dropdown box (category selection)
        dropdownbox = (Spinner) v.findViewById(R.id.categoryDropBox);
        arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, categories);
        dropdownbox.setAdapter(arrayAdapter);

        datePick = v.findViewById(R.id.datePick);
        timePick = v.findViewById(R.id.timePick);

        // For date selection
        datePick.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick (View view){
                Calendar calender = Calendar.getInstance();
                // Current date shown when button is clicked
                int YEAR = calender.get(Calendar.YEAR);
                int MONTH = calender.get(Calendar.MONTH); // Month 0 is January
                int DATE = calender.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext()  , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String dateString = year + " " + month + " " + date;
                        datePick.setText(dateString);

                        // For date formatting
                        /*
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, date);

                        CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                        datePick.setText(dateCharSequence);
                         */
                    }
                }, YEAR, MONTH, DATE);

                datePickerDialog.show();
            }
        });

        // For time selection
        timePick.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick (View view) {
                Calendar calender = Calendar.getInstance();
                // Current time shown when button is clicked
                int HOUR = calender.get(Calendar.HOUR);
                int MINUTE = calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String timeString = "hour: " + hour + "minute: " + minute;
                        datePick.setText(timeString);
                    }
                }, HOUR, MINUTE, true);

                timePickerDialog.show();
            }
        });

        return v;
    }

}
