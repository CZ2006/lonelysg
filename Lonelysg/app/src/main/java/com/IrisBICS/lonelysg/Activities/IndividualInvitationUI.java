package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.Models.User;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class IndividualInvitationUI extends AppCompatActivity implements OnMapReadyCallback {

    private Button acceptInvitation, backButton;
    private TextView activityTitle, activityDateTime,activityDesc,hostInfo,hostInterests;
    private Uri imageUri;
    private Invitation invitation;
    private String invitationID;
    private ImageView indInvImage;
    String currentUserID = FirebaseAuthHelper.getCurrentUserID();
    User host, participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_invitation_ui);

        host = new User();
        participant = new User();

//        //MAP
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        Intent receivedIntent = getIntent();
        invitationID = receivedIntent.getStringExtra("invitationID");
        invitation = new Invitation("","","","","","","",invitationID,"","","",imageUri);

        activityDateTime = findViewById(R.id.activityDateTime);
        activityDesc = findViewById(R.id.activityDesc);
        activityTitle = findViewById(R.id.activityTitle);
        hostInfo = findViewById(R.id.hostInfo);
        hostInterests = findViewById(R.id.hostInterests);
        indInvImage = findViewById(R.id.invImage);

        acceptInvitation = findViewById(R.id.acceptInvitation);
        backButton = findViewById(R.id.backButton);

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            invitation.setEndTime(response.getString("End Time"));
                            invitation.setHost(response.getString("Host"));
                            invitation.setDesc(response.getString("Description"));
                            invitation.setDate(response.getString("Date"));
                            invitation.setLatitude(response.getString("Latitude"));
                            invitation.setLongitude(response.getString("Longitude"));
                            invitation.setLocationName(response.getString("Location"));
                            if (response.has("Image")!=false) {
                                String InvPicUri = response.getString("Image");
                                imageUri = Uri.parse(InvPicUri);
                                invitation.setInvPic(imageUri);
                            }
                            updateTextView();
                            //MAP
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(IndividualInvitationUI.this);
                            getHost(invitation.getHost());
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
            jsonBody.put("Participant", currentUserID);

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

    public void updateTextView(){
        activityDateTime.setText(invitation.getDate()+" "+invitation.getStartTime()+" - " +invitation.getEndTime());
        activityTitle.setText(invitation.getTitle());
        activityDesc.setText(invitation.getDesc());
        if (invitation.getInvPic()!=null) {
            Picasso.get().load(invitation.getInvPic()).into(indInvImage);
        }
    }

    public void updateUserTextView(){
        hostInfo.setText(host.getUsername()+", "+host.getGender()+", "+host.getOccupation());
        hostInterests.setText(host.getInterests());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(Double.parseDouble(invitation.getLatitude()),Double.parseDouble(invitation.getLongitude()));
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(invitation.getLocationName()));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
    }

    private void getHost(String userID) {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/getUser/"+userID;
        JsonObjectRequest getUserProfileRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("reached manager inside");
                        try {
                            host.setUsername(response.getString("username"));
                            host.setGender(response.getString("gender"));
                            host.setAge(response.getString("age"));
                            host.setOccupation(response.getString("occupation"));
                            host.setInterests(response.getString("interests"));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                       updateUserTextView();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });
        AppController.getInstance(this).addToRequestQueue(getUserProfileRequest);
    }

//    private void getParticipant(String userID) {
//        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/getUser/"+userID;
//        JsonObjectRequest getUserProfileRequest = new JsonObjectRequest
//                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println("reached manager inside");
//                        try {
//                            participant.setUsername(response.getString("username"));
////                            participant.setGender(response.getString("gender"));
////                            participant.setAge(response.getString("age"));
////                            participant.setOccupation(response.getString("occupation"));
////                            participant.setInterests(response.getString("interests"));
//                        } catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
////                        arrayAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley", error.toString());
//                    }
//                });
//        AppController.getInstance(this).addToRequestQueue(getUserProfileRequest);
//    }

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


}
