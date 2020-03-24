package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.Adapters.InvitationsListAdapter;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvitationsUI extends AppCompatActivity {

    private ListView invitationsList;
    private ArrayList<Invitation> invitations;
    private String category;
//    int userImage[] = {R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample, R.drawable.user_sample};

    InvitationsListAdapter invitationsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations_ui);

        Intent receivedIntent = getIntent();
        category = receivedIntent.getStringExtra("category");

        invitations = new ArrayList<>();

        invitationsList = findViewById(R.id.invitationsListView);
        invitationsListAdapter = new InvitationsListAdapter(this, invitations);
        invitationsList.setAdapter(invitationsListAdapter);

        invitationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Intent intent = new Intent(getApplicationContext(), IndividualInvitationUI.class);
                    intent.putExtra("invitationID", invitations.get(i).getInvitationID());
                    startActivity(intent);
            }
        });

        getInvitations();
    }

    private void getInvitations() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getInvitations/"+category;

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
                        invitation.setTitle(jsonObject.getString("Title"));
                        invitation.setInvitationID(jsonObject.getString("InvitationID"));
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

}