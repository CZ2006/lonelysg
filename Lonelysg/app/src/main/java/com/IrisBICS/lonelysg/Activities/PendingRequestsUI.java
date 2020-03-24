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
import com.IrisBICS.lonelysg.RequestCancelDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingRequestsUI extends AppCompatActivity implements RequestCancelDialog.DialogListener{

    private ArrayList<Request> requests;
    private ListView pendingRequestsList;
    private int clickedPos = -1;

    String currentUser = FirebaseAuthHelper.getCurrentUser();
    RequestListAdapter requestListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests_ui);

        requests = new ArrayList<>();

        pendingRequestsList = findViewById(R.id.pendingRequestsListView);
        requestListAdapter = new RequestListAdapter(this, requests,"pending");
        pendingRequestsList.setAdapter(requestListAdapter);

        pendingRequestsList.setClickable(true);
        pendingRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedPos = i;
                openDialog();
            }
        });

        getPendingRequests();
    }

    private void getPendingRequests() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getPendingRequests/"+currentUser;

        final JsonArrayRequest getPendingRequestsRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
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
        AppController.getInstance(this).addToRequestQueue(getPendingRequestsRequest);
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
                        Toast.makeText(PendingRequestsUI.this, response, Toast.LENGTH_LONG).show();
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

    public void openDialog(){
        RequestCancelDialog requestCancelDialog = new RequestCancelDialog();
        requestCancelDialog.show(getSupportFragmentManager(), "Request Cancel Dialog");
    }

    @Override
    public void cancelRequest() {
        if (clickedPos!=-1){
            deleteRequest(requests.get(clickedPos).getRequestID());
        }
    }
}
