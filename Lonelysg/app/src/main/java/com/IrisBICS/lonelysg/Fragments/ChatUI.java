package com.IrisBICS.lonelysg.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.IrisBICS.lonelysg.Activities.IndividualChatUI;
import com.IrisBICS.lonelysg.Adapters.ChatListAdapter;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ChatUI extends Fragment {

    private ListView chatList;
    private ArrayList<String> chatUsersList;
    ChatListAdapter chatListAdapter;
    String currentUser = FirebaseAuthHelper.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        chatUsersList = new ArrayList<>();
        chatList = v.findViewById(R.id.chatList);
        chatListAdapter = new ChatListAdapter(this.getActivity(),chatUsersList);
        chatList.setAdapter(chatListAdapter);
        chatList.setClickable(true);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String receiver = chatUsersList.get(i);
                Intent intent;
                intent = new Intent(ChatUI.this.getActivity(), IndividualChatUI.class);
                intent.putExtra("receiver", receiver);
                startActivity(intent);
            }
        });
        getChatUsersList();
        return v;
    }

    private void getChatUsersList() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getChatUsersList/"+currentUser;

        final JsonArrayRequest getChatUsersListRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String chatUser = response.getString(i);
                        chatUsersList.add(chatUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                chatListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance(this.getContext()).addToRequestQueue(getChatUsersListRequest);
    }

}
