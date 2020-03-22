package com.IrisBICS.lonelysg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EditProfileUI extends Activity {

    // For dropdown box
    Spinner dropdownbox;
    String categories[] = {"Choose your Gender", "Male", "Female", "Non-binary", "Transgender", "Intersex", "Gender Non-Conforming", "Others"};
    ArrayAdapter<String> arrayAdapter;

    Button confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_pop_up_page);

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
            }
        });

        // Pressing cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileUI.this, "Process Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
