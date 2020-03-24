package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.IrisBICS.lonelysg.Adapters.ChatRecyclerAdapter;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.Models.Message;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndividualChatUI extends AppCompatActivity {

    String currentUser = FirebaseAuthHelper.getCurrentUser();

    private RecyclerView recyclerView;
    private EditText typeMessage;
    private Button sendButton;
    private String receiver;
    private ArrayList<Message> messages;
    ChatRecyclerAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat_ui);

        Intent receivedIntent = getIntent();
        receiver = receivedIntent.getStringExtra("receiver");
        messages = new ArrayList<>();


        typeMessage = findViewById(R.id.typeMessage);
        sendButton = findViewById(R.id.sendButton);
        recyclerView = findViewById(R.id.chatView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatRecyclerAdapter(this, messages);
//        chatAdapter.setClickListener((ChatRecyclerAdapter.ItemClickListener) this);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = typeMessage.getText().toString().trim();
                sendMessage(text);
            }
        });

        getMessages();

    }

    private void getMessages() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getMessages/"+currentUser+"/"+receiver;

        final JsonArrayRequest getMessagesRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Message message = new Message();
                        message.setMessage(jsonObject.getString("message"));
                        message.setReceiver(jsonObject.getString("receiver"));
                        message.setSender(jsonObject.getString("sender"));
                        messages.add(message);
                        System.out.println(messages.get(i).getMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance(this).addToRequestQueue(getMessagesRequest);
    }

    private void sendMessage(final String text) {
        try {
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/sendMessage";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("message", text);
            jsonBody.put("receiver", receiver);
            jsonBody.put("sender", currentUser);

            JsonObjectRequest sendMessageRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Message message = new Message();
                    message.setMessage(text);
                    message.setReceiver(receiver);
                    message.setSender(currentUser);
                    messages.add(message);
                    chatAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onBackPressed();
                }
            });
            AppController.getInstance(this).addToRequestQueue(sendMessageRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



