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

import com.IrisBICS.lonelysg.Activities.ActivityIndividualChat;
import com.IrisBICS.lonelysg.Adapters.ChatListAdapter;
import com.IrisBICS.lonelysg.AppController;
import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentChat extends Fragment {

    private ListView chatList;
    private ArrayList<String> chatUsersList, chatUsersIDList;
    ChatListAdapter chatListAdapter;
    String currentUserID = FirebaseAuthHelper.getCurrentUserID();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        chatUsersList = new ArrayList<>();
        chatUsersIDList = new ArrayList<>();
        chatList = v.findViewById(R.id.chatList);
        chatListAdapter = new ChatListAdapter(this.getActivity(),chatUsersList);
        chatList.setAdapter(chatListAdapter);
        chatList.setClickable(true);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                intent = new Intent(FragmentChat.this.getActivity(), ActivityIndividualChat.class);
                Bundle extras = new Bundle();
                extras.putString("receiver_name", chatUsersList.get(i));
                extras.putString("receiver_id", chatUsersIDList.get(i));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        getChatUsersList();

        return v;
    }

    private void getChatUsersList() {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/MinHui/getChatUsersList/"+currentUserID;

        final JsonArrayRequest getChatUsersListRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String chatUser = response.getString(i);
                        chatUsersIDList.add(chatUser);
                        getChatUsers(chatUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance(this.getContext()).addToRequestQueue(getChatUsersListRequest);
    }

    private void getChatUsers(String userID) {
        String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/getUser/"+userID;
        JsonObjectRequest getChatUserRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            host.setGender(response.getString("gender"));
//                            host.setAge(response.getString("age"));
//                            host.setOccupation(response.getString("occupation"));
//                            host.setInterests(response.getString("interests"));
                            chatUsersList.add(response.getString("username"));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        chatListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });
        AppController.getInstance(this.getContext()).addToRequestQueue(getChatUserRequest);
    }

}