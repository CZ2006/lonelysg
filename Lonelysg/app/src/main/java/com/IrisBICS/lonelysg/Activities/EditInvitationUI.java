package com.IrisBICS.lonelysg.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditInvitationUI extends AppCompatActivity {

    private EditText editInvTitle;
    private EditText editInvDesc;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private StorageReference mStorage = FirebaseStorage.getInstance().getReference("invitations");

    private String userID = mAuth.getCurrentUser().getUid();
    private Invitation invitation = new Invitation();
    private String invitationID;

    // For dropdown box
    private Spinner editInvCategory;
    String categories[] = {"Choose your invitation category", "Games", "Food and Drinks", "Movies", "Sports", "Study", "Others"};
    ArrayAdapter<String >arrayAdapter;

    private Button confirmButton, cancelButton;
    private Button editInvTime, editInvDate;
    private String dateString,timeString;
    private CircleImageView editInvPic;
    private Uri imageUri;
    private Uri downloadInvPicUri;
    private Task<Uri> downloadUrl;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invitation_ui);


        editInvTitle = findViewById(R.id.editTitle);
        editInvDesc = findViewById(R.id.editDesc);
        editInvDate = findViewById(R.id.newDatePick);
        editInvTime = findViewById(R.id.newTimePick);
        editInvPic = findViewById(R.id.editInvitationPic);

        Intent receivedIntent = getIntent();
        invitationID = receivedIntent.getStringExtra("invitationID");
        setInvitationHint(invitationID);

        // For dropdown box (category selection)
        editInvCategory = findViewById(R.id.editCategoryDropBox);
        arrayAdapter = new ArrayAdapter<String>(EditInvitationUI.this, android.R.layout.simple_list_item_1, categories);
        editInvCategory.setAdapter(arrayAdapter);

        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelEditButton);

        // For invitation pic selection
        editInvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        // For date selection
        editInvDate.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick (View view){
                Calendar calender = Calendar.getInstance();
                // Current date shown when button is clicked
                int YEAR = calender.get(Calendar.YEAR);
                int MONTH = calender.get(Calendar.MONTH); // Month 0 is January
                int DATE = calender.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditInvitationUI.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dateString = year + " " + month + " " + date;
                        editInvDate.setText(dateString);

                    }
                }, YEAR, MONTH, DATE);

                datePickerDialog.show();
            }
        });

        // For time selection
        editInvTime.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick (View view) {
                Calendar calender = Calendar.getInstance();
                // Current time shown when button is clicked
                int HOUR = calender.get(Calendar.HOUR);
                int MINUTE = calender.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditInvitationUI.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeString = hour + ":" + minute;
                        editInvTime.setText(timeString);
                    }
                }, HOUR, MINUTE, true);

                timePickerDialog.show();
            }
        });
        // Pressing confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    updateInvWithPic();
                }
                else {updateInvWithoutPic();}
                Intent intent = new Intent(getApplicationContext(), IndividualUserInvitationUI.class);
                intent.putExtra("invitationID", invitation.getInvitationID());
                startActivity(intent);

            }
        });

        // Pressing cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditInvitationUI.this, "Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), IndividualUserInvitationUI.class);
                intent.putExtra("invitationID", invitation.getInvitationID());
                startActivity(intent);
            }
        });
    }

    private void updateInvWithPic() {
        System.out.println("updating invitation in progress");
        final StorageReference fileRef = mStorage.child(invitationID+ "." + getFileExtension(imageUri));

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
                        JSONObject jsonBody = new JSONObject();
                        if (editInvTitle.getText().toString().matches("")) {
                            jsonBody.put("Title", editInvTitle.getHint());
                        } else {
                            jsonBody.put("Title", editInvTitle.getText());
                        }
                        if (!editInvCategory.getSelectedItem().toString().matches("Choose your invitation category")) {

                            jsonBody.put("Category", editInvCategory.getSelectedItem().toString());
                        }
                        if (editInvDesc.getText().toString().matches("")) {
                            jsonBody.put("Description", editInvDesc.getHint());
                        } else {
                            jsonBody.put("Description", editInvDesc.getText());
                        }
                        if (editInvTime.getText().toString().matches("Select Time")) {
                            jsonBody.put("Start Time", editInvTime.getText());
                        } else {
                            jsonBody.put("Start Time", editInvTime.getText());
                        }
                        if (editInvDate.getText().toString().matches("Select Date")) {
                            jsonBody.put("Date", editInvDate.getText());
                        } else {
                            jsonBody.put("Date", editInvDate.getText());
                        }
                        jsonBody.put("Image",downloadInvPicUri.toString());
                        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/updateInvitation/" + invitationID;
                        JsonObjectRequest updateUserRequest = new JsonObjectRequest(Request.Method.PUT, URL, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("updated");
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
                            }
                        });
                        AppController.getInstance(EditInvitationUI.this).addToRequestQueue(updateUserRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateInvWithoutPic() {
        System.out.println("updating invitation in progress");
        try {
            JSONObject jsonBody = new JSONObject();
            if (editInvTitle.getText().toString().matches("")) {
                jsonBody.put("Title", editInvTitle.getHint());
            }
            else{jsonBody.put("Title", editInvTitle.getText());}
          
            if (!editInvCategory.getSelectedItem().toString().matches("Choose your invitation category")) {

                jsonBody.put("Category", editInvCategory.getSelectedItem().toString());
            }
            if (editInvDesc.getText().toString().matches("")) {
                jsonBody.put("Description", editInvDesc.getHint());
            }
            else{jsonBody.put("Description", editInvDesc.getText());}
            if (editInvTime.getText().toString().matches("Select Time")) {
                jsonBody.put("Start Time", editInvTime.getText());
            }
            else{jsonBody.put("Start Time", editInvTime.getText());}
            if (editInvDate.getText().toString().matches("Select Date")) {
                jsonBody.put("Date", editInvDate.getText());
            }
            else{jsonBody.put("Date", editInvDate.getText());}
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/updateInvitation/"+invitationID;
            JsonObjectRequest updateUserRequest = new JsonObjectRequest(Request.Method.PUT, URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("updated");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", error.toString());
                }
            });
            AppController.getInstance(EditInvitationUI.this).addToRequestQueue(updateUserRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setInvitationHint(String invitationID) {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getInvitation/"+invitationID;

        JsonObjectRequest getInvitationRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            invitation.setCategory(response.getString("Category"));
                            invitation.setTitle(response.getString("Title"));
                            invitation.setStartTime(response.getString("Start Time"));
                            invitation.setHost(response.getString("Host"));
                            invitation.setDesc(response.getString("Description"));
                            invitation.setDate(response.getString("Date"));
                            editInvTitle.setHint(invitation.getTitle());
                            editInvDesc.setHint(invitation.getDesc());
                            editInvTime.setText(invitation.getStartTime());
                            editInvDate.setText(invitation.getDate());
                            if (invitation.getInvPic()!=null) {
                                Picasso.get().load(invitation.getInvPic()).into(editInvPic);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });
        AppController.getInstance(this).addToRequestQueue(getInvitationRequest);

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
            Picasso.get().load(imageUri).into(editInvPic);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}