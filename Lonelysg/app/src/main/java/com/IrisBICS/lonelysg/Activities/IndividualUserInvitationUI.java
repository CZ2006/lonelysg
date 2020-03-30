package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class IndividualUserInvitationUI extends AppCompatActivity implements OnMapReadyCallback {

    private Button editInvitation, deleteInvitation, back;
    private TextView activityTitle, activityDateTime,activityDesc, activityLocation;

    private Invitation invitation;
    private String invitationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_user_invitation_ui);

        Intent receivedIntent = getIntent();
        invitationID = receivedIntent.getStringExtra("invitationID");
        invitation = new Invitation("","","","","","","",invitationID,"","","");

        back = findViewById(R.id.backButton);
        activityDateTime = findViewById(R.id.activityDateTime);
        activityDesc = findViewById(R.id.activityDesc);
        activityTitle = findViewById(R.id.activityTitle);
        activityLocation = findViewById(R.id.activityLocation);
        updateTextView();

        editInvitation = findViewById(R.id.editInvitation);
        deleteInvitation = findViewById(R.id.deleteInvitation);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInvitationsUI.class);
                startActivity(intent);
            }
        });

        // Click edit button
        editInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditInvitationUI.class);
                intent.putExtra("invitationID", invitationID);
                startActivity(intent);
            }
        });

        // Click delete button
        deleteInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteInvitation();
            }
        });

        getInvitation();

    }

    private void getInvitation() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getInvitation/"+invitationID;

        JsonObjectRequest getInvitationRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            invitation.setCategory(response.getString("Category"));
                            invitation.setTitle(response.getString("Title"));
                            invitation.setStartTime(response.getString("Start Time"));
                            invitation.setEndTime(response.getString("End Time"));
                            invitation.setHost(response.getString("Host"));
                            invitation.setDesc(response.getString("Description"));
                            invitation.setDate(response.getString("Date"));
                            invitation.setLocationName(response.getString("Location"));
                            invitation.setInvitationID(invitationID);
                            updateTextView();
                            //MAP
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(IndividualUserInvitationUI.this);
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

    private void deleteInvitation() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/deleteInvitation/"+invitationID;
        StringRequest deleteInvitationRequest = new StringRequest(com.android.volley.Request.Method.DELETE,URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                // response
                Toast.makeText(IndividualUserInvitationUI.this, "Invitation deleted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserInvitationsUI.class);
                startActivity(intent);
            }
        },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) { }
            }
        );
        AppController.getInstance(this).addToRequestQueue(deleteInvitationRequest);
    }

    public void updateTextView(){
        activityDateTime.setText(invitation.getDate()+" "+invitation.getStartTime()+" - " +invitation.getEndTime());
        activityTitle.setText(invitation.getTitle());
        activityDesc.setText(invitation.getDesc());
        activityLocation.setText(invitation.getLocationName());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(Double.parseDouble(invitation.getLatitude()),Double.parseDouble(invitation.getLongitude()));
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(invitation.getLocationName()));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
    }
}
