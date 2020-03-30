package com.IrisBICS.lonelysg.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pusher.pushnotifications.PushNotifications;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateInvitationUI extends AppCompatActivity {

    private Spinner categoryPick;
    String categories[] = {"Choose your invitation category", "Games", "Food and Drinks", "Movies", "Sports", "Study", "Others"};
    ArrayAdapter<String >arrayAdapter;

    private EditText enterTitle, enterDesc;
    private CircleImageView invPic;
    private Uri imageUri;
    private Uri downloadInvPicUri;
    private Task<Uri> downloadUrl;
    private static final int PICK_IMAGE = 1;
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference("invitations");

    //For time and date selection
    private Button datePick, startTimePick, endTimePick, confirmButton, cancelButton;

    String dateString, startTimeString, endTimeString, category, title, desc, latitude, longitude, locationName;
    Place location;
    String currentUserID = FirebaseAuthHelper.getCurrentUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invitation_ui);

        enterTitle = findViewById(R.id.enterTitle);
        enterDesc = findViewById(R.id.enterDesc);
        invPic = findViewById(R.id.newInvPic);

        invPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

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
        startTimePick = findViewById(R.id.startTimePick);
        endTimePick = findViewById(R.id.endTimePick);

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
                        dateString = year + "/" + month + "/" + date;
                        datePick.setText(dateString);

                        // For date formatting

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, date);

                        CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                        datePick.setText(dateCharSequence);

                    }
                }, YEAR, MONTH, DATE);

                datePickerDialog.show();
            }
        });

        // For start time selection
        startTimePick.setOnClickListener(new View.OnClickListener() {
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
                        startTimeString = hour + ":" + minute;
                        startTimePick.setText(startTimeString);
                    }
                }, HOUR, MINUTE, true);

                timePickerDialog.show();
            }
        });

        // For end time selection
        endTimePick.setOnClickListener(new View.OnClickListener() {
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
                        endTimeString = hour + ":" + minute;
                        endTimePick.setText(endTimeString);
                    }
                }, HOUR, MINUTE, true);

                timePickerDialog.show();
            }
        });

        //Places API
        Places.initialize(getApplicationContext(), "AIzaSyDf5AJqzMTUa6kYEqyl19TAOyAeS_v5Y3c");
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location = place;
                locationName = location.getName();
                LatLng loc = location.getLatLng();
                latitude = String.valueOf(loc.latitude);
                longitude = String.valueOf(loc.longitude);
                Toast.makeText(CreateInvitationUI.this, "Location is: " + place.getName(), Toast.LENGTH_SHORT).show();
                Log.i("Create Invitation UI", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Create Invitation UI", "An error occurred: " + status);
            }
        });


        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Pressing confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = enterTitle.getText().toString().trim();
                desc = enterDesc.getText().toString().trim();
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
                else if (startTimeString == null){
                    Toast.makeText(CreateInvitationUI.this, "Select Start Time", Toast.LENGTH_SHORT).show();
                }
                else if (endTimeString == null){
                    Toast.makeText(CreateInvitationUI.this, "Select End Time", Toast.LENGTH_SHORT).show();
                }
                else if (location == null){
                    Toast.makeText(CreateInvitationUI.this, "Select Location", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (imageUri != null) {
                        addInvWithPic();
                    }
                    else {addInvWithoutPic();}
//                    addInvitation();
                    Toast.makeText(CreateInvitationUI.this, "New Invitation Created", Toast.LENGTH_SHORT).show();
                }
                String requestInterest = currentUserID+"request";
                PushNotifications.addDeviceInterest(requestInterest);
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

    private void addInvWithPic() {
        final StorageReference fileRef = mStorage.child(currentUserID+ "." + getFileExtension(imageUri));

        UploadTask uploadTask = fileRef.putFile(imageUri);
        downloadUrl = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadInvPicUri = task.getResult();
                    try {
                        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/addInvitation";
                        JSONObject jsonBody = new JSONObject();

                        jsonBody.put("Category", category);
                        jsonBody.put("Date", dateString);
                        jsonBody.put("Description", desc);
                        jsonBody.put("Host", currentUserID);
                        jsonBody.put("Start Time", startTimeString);
                        jsonBody.put("End Time", endTimeString);
                        jsonBody.put("Title", title);
                        jsonBody.put("Latitude", latitude);
                        jsonBody.put("Longitude", longitude);
                        jsonBody.put("Location",locationName);
                        jsonBody.put("Image",downloadInvPicUri.toString());
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
                        AppController.getInstance(CreateInvitationUI.this).addToRequestQueue(addInvitationRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addInvWithoutPic() {
        try {
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/addInvitation";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Category", category);
            jsonBody.put("Date", dateString);
            jsonBody.put("Description", desc);
            jsonBody.put("Host", currentUserID);
            jsonBody.put("Start Time", startTimeString);
            jsonBody.put("End Time", endTimeString);
            jsonBody.put("Title", title);
            jsonBody.put("Latitude", latitude);
            jsonBody.put("Longitude", longitude);
            jsonBody.put("Location",locationName);

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

    private void openImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(invPic);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
