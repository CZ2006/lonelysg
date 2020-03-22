package com.IrisBICS.lonelysg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat extends Fragment {

    ListView chatList;
//    String[] chatUsers = {"User 1", "User 2", "User 3", "User 4","User 5"};

    private List<String> chatUsersList = new ArrayList<>();

    FirebaseDatabaseHelper db = new FirebaseDatabaseHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        chatList = (ListView) v.findViewById(R.id.chatList);
        db.readMessage(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Message> messages, List<String> keys) {
                for (int i = 0;i<messages.size();i++){
                        boolean exists = false;
                        if (messages.get(i).getSender().equals(FirebaseDatabaseHelper.getCurrentUser())){
                            for(int j=0;j<chatUsersList.size();j++){
                                exists = false;
                                if (messages.get(i).getReceiver().equals(chatUsersList.get(j))){
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists==false){
                                chatUsersList.add(messages.get(i).getReceiver());
                            }
                            System.out.println(messages.get(i).getSender());
                        }
                        if (messages.get(i).getReceiver().equals(FirebaseDatabaseHelper.getCurrentUser())){
                            exists = false;
                            for(int j=0;j<chatUsersList.size();j++){
                                if (messages.get(i).getSender().equals(chatUsersList.get(j))){
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists==false){
                                chatUsersList.add(messages.get(i).getSender());
                            }
                        }
                }

                System.out.println(chatUsersList + "test");

                ChatListView chatListView = new ChatListView(FragmentChat.this.getActivity(),chatUsersList);
                System.out.println("phase 1");
                chatList.setAdapter(chatListView);
                System.out.println("phase 2");
            }
            @Override
            public void DataIsInserted() {
            }
            @Override
            public void DataIsUpdated() {
            }
            @Override
            public void DataIsDeleted() {
            }
        });

        chatList.setClickable(true);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String receiver = chatUsersList.get(i);
                Intent intent;
                intent = new Intent(FragmentChat.this.getActivity(), IndividualChatUI.class);
                intent.putExtra("receiver", receiver);
                startActivity(intent);
            }
        });

        return v;
    }


}
