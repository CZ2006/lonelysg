package com.IrisBICS.lonelysg.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.Adapters.RequestListAdapter;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.Models.Request;
import com.IrisBICS.lonelysg.R;
import com.IrisBICS.lonelysg.RequestActionDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.pusher.pushnotifications.PushNotifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceivedRequestsUI extends AppCompatActivity implements RequestActionDialog.DialogListener{

    private ArrayList<Request> requests;
    private ListView receivedRequestsList;
    private int clickedPos = -1;

    String currentUser = FirebaseAuthHelper.getCurrentUser();
    RequestListAdapter requestListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_requests_ui);

        requests = new ArrayList<>();

        receivedRequestsList = findViewById(R.id.receivedRequestsListView);
        requestListAdapter = new RequestListAdapter(this, requests,"received");
        receivedRequestsList.setAdapter(requestListAdapter);

        receivedRequestsList.setClickable(true);
        receivedRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedPos = i;
                openDialog();
            }
        });

        getReceivedRequests();
    }

    private void getReceivedRequests() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getReceivedRequests/"+currentUser;

        final JsonArrayRequest getReceivedRequestsRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Request request = new Request();
                        request.setHost(jsonObject.getString("Host"));
                        request.setInvitation(jsonObject.getString("Invitation"));
                        request.setParticipant(jsonObject.getString("Participant"));
                        request.setRequestID(jsonObject.getString("RequestID"));
                        requests.add(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                requestListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance(this).addToRequestQueue(getReceivedRequestsRequest);
    }

    private void deleteRequest(final String reqID) {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/deleteRequest/"+reqID;
        StringRequest deleteRequestRequest = new StringRequest(com.android.volley.Request.Method.DELETE,URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                // response
                requests.remove(requests.get(clickedPos));
                requestListAdapter.notifyDataSetChanged();
                Toast.makeText(ReceivedRequestsUI.this, "Done!", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                }
        );
        AppController.getInstance(this).addToRequestQueue(deleteRequestRequest);
    }

    private void sendAcceptRequestMessage() {
        try {
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/sendMessage";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("message", "Hello! I have accepted your request!");
            jsonBody.put("receiver", requests.get(clickedPos).getParticipant());
            jsonBody.put("sender", currentUser);

            JsonObjectRequest sendAcceptRequestMessageRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onBackPressed();
                }
            });
            AppController.getInstance(this).addToRequestQueue(sendAcceptRequestMessageRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openDialog(){
        RequestActionDialog reqActionDialog = new RequestActionDialog();
        reqActionDialog.show(getSupportFragmentManager(), "Request Action Dialog");
    }

    public void approveRequest(){
        if (clickedPos!=-1){
            deleteRequest(requests.get(clickedPos).getRequestID());
            sendAcceptRequestMessage();
            //insert xq send notif
        }

    }

    public void rejectRequest(){
        if (clickedPos!=-1){
            deleteRequest(requests.get(clickedPos).getRequestID());
            //insert xq send notif
        }
    }

}
