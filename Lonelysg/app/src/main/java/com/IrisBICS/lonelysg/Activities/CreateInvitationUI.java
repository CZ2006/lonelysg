package com.IrisBICS.lonelysg.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CreateInvitationUI extends AppCompatActivity {

    private Spinner categoryPick;
    String categories[] = {"Choose your invitation category", "Food and Drinks", "Movies", "Sports", "Study", "Others"};
    ArrayAdapter<String >arrayAdapter;

    private EditText enterTitle, enterDesc;

    //For time and date selection
    private Button datePick, timePick, confirmButton, cancelButton;

    String dateString, timeString, category;
    String currentUser = FirebaseAuthHelper.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invitation_ui);

        enterTitle = findViewById(R.id.enterTitle);
        enterDesc = findViewById(R.id.enterDesc);

        // For category dropdown box selection
        categoryPick = findViewById(R.id.categoryDropBox);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        categoryPick.setAdapter(arrayAdapter);

        categoryPick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                // On selecting a spinner item
                category = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }});

        datePick = findViewById(R.id.datePick);
        timePick = findViewById(R.id.timePick);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateInvitationUI.this  , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dateString = year + " " + month + " " + date;
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateInvitationUI.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeString = hour + ":" + minute;
                        timePick.setText(timeString);
                    }
                }, HOUR, MINUTE, true);

                timePickerDialog.show();
            }
        });

        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Pressing confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = enterTitle.getText().toString().trim();
                String desc = enterDesc.getText().toString().trim();
                if (title == null){
                    Toast.makeText(CreateInvitationUI.this, "Enter Event Title", Toast.LENGTH_SHORT).show();
                }
                else if (category == null){
                    Toast.makeText(CreateInvitationUI.this, "Select Category", Toast.LENGTH_SHORT).show();
                }
                else if (desc == null){
                    Toast.makeText(CreateInvitationUI.this, "Enter Description", Toast.LENGTH_SHORT).show();
                }
                else if (dateString == null){
                    Toast.makeText(CreateInvitationUI.this, "Select Date", Toast.LENGTH_SHORT).show();
                }
                else if (timeString == null){
                    Toast.makeText(CreateInvitationUI.this, "Select Start Time", Toast.LENGTH_SHORT).show();
                }
                else{
                    addInvitation(title, desc);
                    Toast.makeText(CreateInvitationUI.this, "New Invitation Created", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Pressing cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateInvitationUI.this, "Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(CreateInvitationUI.this, ManageInvitationsUI.class);
                startActivity(intent);
            }
        });
    }

    private void addInvitation(String title, String desc) {
        try {
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/addInvitation";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Category", category);
            jsonBody.put("Date", dateString);
            jsonBody.put("Description", desc);
            jsonBody.put("Host", currentUser);
            jsonBody.put("Start Time", timeString);
            jsonBody.put("Title", title);

            JsonObjectRequest addInvitationRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onBackPressed();
                }
            }) {
            };
            AppController.getInstance(this).addToRequestQueue(addInvitationRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
