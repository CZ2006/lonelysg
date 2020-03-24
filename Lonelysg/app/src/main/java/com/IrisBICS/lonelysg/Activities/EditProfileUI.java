package com.IrisBICS.lonelysg.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.IrisBICS.lonelysg.R;

public class EditProfileUI extends Activity {

    // For dropdown box
    Spinner dropdownbox;
    String categories[] = {"Choose your Gender", "Male", "Female", "Non-binary", "Transgender", "Intersex", "Gender Non-Conforming", "Others"};
    ArrayAdapter<String> arrayAdapter;

    Button confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        // For dropdown box (category selection)
        dropdownbox = (Spinner) findViewById(R.id.genderCategoryDropBox);
        arrayAdapter = new ArrayAdapter<String>(EditProfileUI.this, android.R.layout.simple_list_item_1, categories);
        dropdownbox.setAdapter(arrayAdapter);

        confirmButton = (Button)findViewById(R.id.editProfileConfirmButton);
        cancelButton = (Button)findViewById(R.id.editProfileCancelButton);

        // Pressing confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileUI.this, "Profile Information Updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (EditProfileUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });

        // Pressing cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileUI.this, "Process Cancelled", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (EditProfileUI.this, NavigationBarUI.class);
                startActivity(i);
            }
        });
    }
}
