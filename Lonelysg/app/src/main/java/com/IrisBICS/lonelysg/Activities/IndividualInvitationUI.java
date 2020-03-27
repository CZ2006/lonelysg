package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
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

import androidx.appcompat.app.AppCompatActivity;

public class IndividualInvitationUI extends AppCompatActivity implements OnMapReadyCallback {

    private Button acceptInvitation, rejectInvitation;
    private TextView activityTitle, activityDateTime,activityDesc;

    private Invitation invitation;
    private String invitationID;
    String currentUser = FirebaseAuthHelper.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_invitation_ui);

        //TRYING OUT MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent receivedIntent = getIntent();
        invitationID = receivedIntent.getStringExtra("invitationID");
        invitation = new Invitation("","","","","","",invitationID);

        activityDateTime = findViewById(R.id.activityDateTime);
        activityDesc = findViewById(R.id.activityDesc);
        activityTitle = findViewById(R.id.activityTitle);
        updateTextView();

        acceptInvitation = findViewById(R.id.acceptInvitation);
        rejectInvitation = findViewById(R.id.rejectInvitation);

        // Click request button
        acceptInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
//                sendNotif();
                Toast.makeText(IndividualInvitationUI.this, "Request Sent", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Click cancel button
        rejectInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IndividualInvitationUI.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                finish();
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
                            invitation.setHost(response.getString("Host"));
                            invitation.setDesc(response.getString("Description"));
                            invitation.setDate(response.getString("Date"));
                            updateTextView();
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

    private void sendRequest() {
        try {
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/sendRequest";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Host", invitation.getHost());
            jsonBody.put("Invitation", invitation.getTitle());
            jsonBody.put("Participant", currentUser);

            JsonObjectRequest sendRequestRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onBackPressed();
                }
            });
            AppController.getInstance(this).addToRequestQueue(sendRequestRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotif(){
        String url ="https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/sendNotif";

        // Request a string response from the provided URL.
        StringRequest sendNotifRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(ApiTest.this,"You received a request", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance(this).addToRequestQueue(sendNotifRequest);
    }

    public void updateTextView(){
        System.out.println(invitation.getInvitationID());
        activityDateTime.setText(invitation.getDate()+" "+invitation.getStartTime());
        activityTitle.setText(invitation.getTitle());
        activityDesc.setText(invitation.getDesc());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in NTU,
        // and move the map's camera to the same location.
        LatLng ntu = new LatLng(1.3483, 103.6831);
        googleMap.addMarker(new MarkerOptions().position(ntu)
                .title("Location Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ntu));
    }

}
