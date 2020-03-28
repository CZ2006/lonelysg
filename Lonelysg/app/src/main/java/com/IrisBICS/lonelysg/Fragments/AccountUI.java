package com.IrisBICS.lonelysg.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.IrisBICS.lonelysg.Activities.EditProfileUI;
import com.IrisBICS.lonelysg.Activities.LoginUI;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.Models.User;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountUI extends Fragment {
    TextView profileName;
    TextView profileGender;
    TextView profileAge;
    TextView profileOccupation;
    TextView profileInterest;
    TextView profileUsername;
    Spinner dropDownIcon;
    String moreSettings[] = {"Change Password", "Delete Account"};
    ArrayAdapter<String> arrayAdapter;


    Button editProfile;

    private Button logout;
//    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    User user= new User();

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_account, container, false);

        profileName = v.findViewById(R.id.accountUsername);
        profileAge = v.findViewById(R.id.accountAge);
        profileGender = v.findViewById(R.id.accountGender);
        profileOccupation = v.findViewById(R.id.accountOccupation);
        profileInterest = v.findViewById(R.id.accountInterests);
        profileUsername = v.findViewById(R.id.userName);

        // For dropdown icon (category selection)
        dropDownIcon = (Spinner) v.findViewById(R.id.moreSettingsicon);
        arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, moreSettings);
        dropDownIcon.setAdapter(arrayAdapter);
        // For edit profile pop-up
        editProfile = (Button) v.findViewById(R.id.editProfileButton);

//        String userID = FirebaseAuthHelper.getUserID();
        String userID = mAuth.getCurrentUser().getUid();
        getUserProfile(userID);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (v.getContext(), EditProfileUI.class);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        logout = (Button) getView().findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logging out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginUI.class);
                startActivity(intent);
            }
        });
    }

    private void getUserProfile(String userID) {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/getUser/"+userID;
        JsonObjectRequest getUserProfileRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("reached manager inside");
                        try {
                            user.setUsername(response.getString("username"));
                            user.setGender(response.getString("gender"));
                            user.setAge(response.getString("age"));
                            user.setOccupation(response.getString("occupation"));
                            user.setInterests(response.getString("interests"));
                            setUserProfile();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
//                        arrayAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });
        AppController.getInstance(this.getContext()).addToRequestQueue(getUserProfileRequest);
    }

    private void setUserProfile(){
        profileName.setText(user.getUsername());
        profileGender.setText(user.getGender());
        profileAge.setText(user.getAge());
        profileOccupation.setText(user.getOccupation());
        profileInterest.setText(user.getInterests());
        profileUsername.setText(user.getUsername());
    }
}