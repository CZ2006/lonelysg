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
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.Models.Invitation;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInvitationsUI extends AppCompatActivity {

    private ListView userInvitationsList;

    private ArrayList<Invitation> userInvitations;
    InvitationsListAdapter invitationsListAdapter;
    String currentUser = FirebaseAuthHelper.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_invitations_ui);

        userInvitations = new ArrayList<>();

        userInvitationsList = findViewById(R.id.userInvitationsListView);
        invitationsListAdapter = new InvitationsListAdapter(this, userInvitations);
        userInvitationsList.setAdapter(invitationsListAdapter);

        userInvitationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), IndividualUserInvitationUI.class);
                intent.putExtra("invitationID", userInvitations.get(i).getInvitationID());
                startActivity(intent);
            }
        });

        getUserInvitations();
    }

    private void getUserInvitations() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getUserInvitations/"+currentUser;

        final JsonArrayRequest getUserInvitationsRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Invitation invitation = new Invitation();
                        invitation.setTitle(jsonObject.getString("Title"));
                        invitation.setStartTime(jsonObject.getString("Start Time"));
                        invitation.setHost(jsonObject.getString("Host"));
                        invitation.setDesc(jsonObject.getString("Description"));
                        invitation.setDate(jsonObject.getString("Date"));
                        invitation.setCategory(jsonObject.getString("Category"));
                        invitation.setInvitationID(jsonObject.getString("InvitationID"));
                        userInvitations.add(invitation);
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
        AppController.getInstance(this).addToRequestQueue(getUserInvitationsRequest);
    }
}
