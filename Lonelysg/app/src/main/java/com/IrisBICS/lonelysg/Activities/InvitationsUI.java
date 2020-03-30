package com.IrisBICS.lonelysg.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.IrisBICS.lonelysg.Adapters.InvitationsListAdapter;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InvitationsUI extends AppCompatActivity implements SearchView.OnQueryTextListener {

    int PERMISSION_ID = 1;

    private ListView invitationsList;
    private SearchView searchView;
    private Spinner sortBy;
    private Button back;
    String sortChoices[] = {"Recent", "Title", "Date", "Distance from current location"};
    private ArrayList<Invitation> invitations;
    private String category, sort;
//    int userImage[] = {R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample};
    ArrayAdapter<String >arrayAdapter;
    InvitationsListAdapter invitationsListAdapter;
    String currentUserID = FirebaseAuthHelper.getCurrentUserID();

    FusedLocationProviderClient mFusedLocationClient;
    private double userLat, userLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations_ui);

        Intent receivedIntent = getIntent();
        category = receivedIntent.getStringExtra("category");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        invitations = new ArrayList<>();

        sortBy = findViewById(R.id.sortDropBox);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sortChoices);
        sortBy.setAdapter(arrayAdapter);

        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                // On selecting a spinner item
                sort = parent.getItemAtPosition(position).toString();
                switch(sort){
                    case "Recent":
                        sortByRecent();
                        break;
                    case "Title":
                        sortByTitle();
                        break;
                    case "Date":
                        sortByDate();
                    case "Distance from current location":
                        sortByDistance();
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }});

        searchView = findViewById(R.id.searchView);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NavigationBarUI.class);
                intent.putExtra("next", "Discovery Page");
                startActivity(intent);
            }
        });

        invitationsList = findViewById(R.id.invitationsListView);
        invitationsListAdapter = new InvitationsListAdapter(this, invitations);
        invitationsList.setAdapter(invitationsListAdapter);
        invitationsList.setTextFilterEnabled(true);

        invitationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Intent intent = new Intent(getApplicationContext(), IndividualInvitationUI.class);
                    intent.putExtra("invitationID", invitations.get(i).getInvitationID());
                    startActivity(intent);
            }
        });

        setupSearchView();
        getInvitations();
        getLastLocation();
    }

    private void getInvitations() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getInvitations/"+category+"/"+currentUserID;

        final JsonArrayRequest getInvitationsRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Invitation invitation = new Invitation();
                        invitation.setDate(jsonObject.getString("Date"));
                        invitation.setDesc(jsonObject.getString("Description"));
                        invitation.setHost(jsonObject.getString("Host"));
                        invitation.setStartTime(jsonObject.getString("Start Time"));
                        invitation.setEndTime(jsonObject.getString("End Time"));
                        invitation.setTitle(jsonObject.getString("Title"));
                        invitation.setInvitationID(jsonObject.getString("InvitationID"));
                        invitation.setLatitude(jsonObject.getString("Latitude"));
                        invitation.setLongitude(jsonObject.getString("Longitude"));
                        invitation.setLocationName(jsonObject.getString("Location"));
                        invitation.setCategory(category);
                        invitations.add(invitation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                invitationsListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance(this).addToRequestQueue(getInvitationsRequest);
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("What do you want?");
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            invitationsList.clearTextFilter();
        } else {
            invitationsList.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void sortByRecent(){
        Collections.sort(invitations, new Comparator<Invitation>(){
            @Override
            public int compare(Invitation a, Invitation b){
                return a.getInvitationID().compareTo(b.getInvitationID());
            }
        });
        invitationsListAdapter.notifyDataSetChanged();
    }

    private void sortByTitle(){
        Collections.sort(invitations, new Comparator<Invitation>(){
            @Override
            public int compare(Invitation a, Invitation b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        invitationsListAdapter.notifyDataSetChanged();
    }

    private void sortByDistance(){
        Collections.sort(invitations, new Comparator<Invitation>(){
            @Override
            public int compare(Invitation a, Invitation b){
                double distA = Math.sqrt(Math.pow(Double.valueOf(a.getLatitude())-userLat, 2) + Math.pow(Double.valueOf(a.getLongitude())-userLong, 2));
                double distB = Math.sqrt(Math.pow(Double.valueOf(b.getLatitude())-userLat, 2) + Math.pow(Double.valueOf(b.getLongitude())-userLong, 2));
                return Double.compare(distA,distB);
            }
        });
        invitationsListAdapter.notifyDataSetChanged();
    }

    private void sortByDate(){
        Collections.sort(invitations, new Comparator<Invitation>(){
            @Override
            public int compare(Invitation a, Invitation b){
                return a.getDate().compareTo(b.getDate());
            }
        });
        invitationsListAdapter.notifyDataSetChanged();
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    userLat = location.getLatitude();
                                    userLong = location.getLongitude();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            userLat = mLastLocation.getLatitude();
            userLong = mLastLocation.getLongitude();
        }
    };

}