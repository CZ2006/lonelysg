package com.IrisBICS.lonelysg.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.IrisBICS.lonelysg.Activities.ActivityChangePassword;
import com.IrisBICS.lonelysg.Activities.ActivityEditProfile;
import com.IrisBICS.lonelysg.Activities.ActivityLogin;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.Models.User;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentAccount extends Fragment {
    private TextView profileName, profileGender, profileAge, profileOccupation, profileInterest, profileUsername;
    private Uri imageUri;
    private Spinner settingsIcon;
    private CircleImageView profilePic;
    private String settings[] = {"Change Password", "Delete Account", "Log Out"};
    private ArrayAdapter<String> arrayAdapter;

    private Button editProfile;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
        profilePic = v.findViewById(R.id.accountProfilePic);

        // For dropdown settings icon
        settingsIcon = (Spinner) v.findViewById(R.id.moreSettingsicon);
        arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, settings);
        settingsIcon.setAdapter(arrayAdapter);

        settingsIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView parent, View view, int position, long id) {
                   // On selecting a spinner item
                   String next = parent.getItemAtPosition(position).toString();
                   switch (next) {
                       case "Change Password":
                           Intent intent = new Intent(getActivity(), ActivityChangePassword.class);
                           startActivity(intent);
                           break;
                       case "Delete Account":
                           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                           user.delete()
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               Log.d("FragmentAccount", "User account deleted.");
                                           }
                                       }
                                   });
                           break;
                       case "Log Out":
                           FirebaseAuth.getInstance().signOut();
                           Toast.makeText(getActivity(), "Logging out!", Toast.LENGTH_SHORT).show();
                           Intent i = new Intent(getActivity(), ActivityLogin.class);
                           startActivity(i);
                           break;
                       default:
                           break;
                   }
               }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
           });

        //settingsIcon.setOnItemSelectedListener(this);

        editProfile = v.findViewById(R.id.editProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (v.getContext(), ActivityEditProfile.class);
                startActivity(i);
            }
        });

        String userID = mAuth.getCurrentUser().getUid();
        getUserProfile(userID);

        return v;
    }

    private void getUserProfile(String userID) {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/getUser/"+userID;
        JsonObjectRequest getUserProfileRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user.setUsername(response.getString("username"));
                            user.setGender(response.getString("gender"));
                            user.setAge(response.getString("age"));
                            user.setOccupation(response.getString("occupation"));
                            user.setInterests(response.getString("interests"));
                            if (response.has("image")!=false) {
                                String profilePicUri = response.getString("image");
                                imageUri = Uri.parse(profilePicUri);
                                user.setProfilePic(imageUri);
                            }
                            setUserProfile();
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
        AppController.getInstance(this.getContext()).addToRequestQueue(getUserProfileRequest);
    }

    private void setUserProfile(){
        profileName.setText(user.getUsername());
        profileGender.setText(user.getGender());
        profileAge.setText(user.getAge());
        profileOccupation.setText(user.getOccupation());
        profileInterest.setText(user.getInterests());
        profileUsername.setText(user.getUsername());
        if (user.getProfilePic()!=null) {
            Picasso.get().load(user.getProfilePic()).into(profilePic);
        }
    }
}