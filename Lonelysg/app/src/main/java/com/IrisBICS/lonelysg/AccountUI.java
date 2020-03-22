package com.IrisBICS.lonelysg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AccountUI extends Fragment {
    Spinner dropdownicon;
    String moreSettings[] = {"Change Password", "Delete Account", "Log Out"};
    ArrayAdapter<String> arrayAdapter;

    Button editProfile;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_account, container, false);

        // For dropdown icon (category selection)
        dropdownicon = (Spinner) v.findViewById(R.id.moreSettingsicon);
        arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, moreSettings);
        dropdownicon.setAdapter(arrayAdapter);

        // For edit profile pop-up
        editProfile = (Button) v.findViewById(R.id.editProfileButton);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (v.getContext(), EditProfileUI.class);
                startActivity(i);
            }
        });

        return v;
    }
}
