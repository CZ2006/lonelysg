package com.IrisBICS.lonelysg;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiTest extends AppCompatActivity {

    private ArrayList<Message> messageList = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

//        final TextView textView = (TextView) findViewById(R.id.textView2);
//// ...
//
//// Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getMessage";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,15));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
//            }
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);

        getMessage();
        System.out.println(messageList+"outside");
//        textView.setText(messageList.get(0).getSender());
    }


    private void getMessage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getMessage", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("phase1");
                for (int i = 0; i < response.length(); i++) {
                    System.out.println("phase2");
                    try {
                        System.out.println("phase3");
                        JSONObject jsonObject = response.getJSONObject(i);
                        Message text = new Message();
                        text.setMessage(jsonObject.getString("message"));
                        text.setReceiver(jsonObject.getString("receiver"));
                        text.setSender(jsonObject.getString("sender"));
                        messageList.add(text);
                        System.out.println(messageList.get(i).getMessage()+"inside1");
                    } catch (JSONException e) {
                        System.out.println("phase4");
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                System.out.println(messageList);
//                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        System.out.println(messageList+"inside2");
    }

}
