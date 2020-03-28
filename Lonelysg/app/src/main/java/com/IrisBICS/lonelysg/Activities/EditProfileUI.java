package com.IrisBICS.lonelysg.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileUI extends Activity {

    private CircleImageView profilePic;
    private Uri imageUri;
    private EditText name;
    private EditText age;
    private EditText occupation;
    private EditText interest;
    private static final int PICK_IMAGE = 1;

    String currentUserID = FirebaseAuthHelper.getCurrentUserID();

    // For dropdown box
    Spinner dropdownbox;
    String categories[] = {"Choose your Gender", "Male", "Female", "Non-binary", "Transgender", "Intersex", "Gender Non-Conforming", "Others"};
    ArrayAdapter<String> arrayAdapter;

    Button confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        profilePic = findViewById(R.id.editProfilePic);
        name = findViewById(R.id.editProfileName);
        age = findViewById(R.id.editProfileAge);
        occupation = findViewById(R.id.editProfileOccupation);
        interest = findViewById(R.id.editProfileInterests);

        // For dropdown box (category selection)
        dropdownbox = (Spinner) findViewById(R.id.genderCategoryDropBox);
        arrayAdapter = new ArrayAdapter<String>(EditProfileUI.this, android.R.layout.simple_list_item_1, categories);
        dropdownbox.setAdapter(arrayAdapter);

        confirmButton = (Button)findViewById(R.id.editProfileConfirmButton);
        cancelButton = (Button)findViewById(R.id.editProfileCancelButton);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });

        // Pressing confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                Intent i = new Intent (EditProfileUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });

        // Pressing cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileUI.this, "Process Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_IMAGE){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
        }
    }

    private void updateProfile() {

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", name.getText());
            jsonBody.put("gender", dropdownbox.getSelectedItem().toString());
            jsonBody.put("age", age.getText());
            jsonBody.put("occupation",occupation.getText());
            jsonBody.put("interests",interest.getText());
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/updateUser/"+currentUserID;
            JsonObjectRequest updateUserRequest = new JsonObjectRequest(Request.Method.PUT, URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", error.toString());
                }
            });
            AppController.getInstance(this).addToRequestQueue(updateUserRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

