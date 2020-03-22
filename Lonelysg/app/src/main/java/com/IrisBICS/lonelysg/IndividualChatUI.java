package com.IrisBICS.lonelysg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IndividualChatUI extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText typeMessage;
    private Button sendButton;
    private String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat_ui);

        Intent receivedIntent = getIntent();
        receiver = receivedIntent.getStringExtra("receiver");

        recyclerView = (RecyclerView) findViewById(R.id.chatView);
        new FirebaseDatabaseHelper().readMessage(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Message> messages, List<String> keys) {
                for (int i = 0;i<messages.size();i++){
                    if (!(messages.get(i).getReceiver().equals(receiver))){
                        messages.remove(i);
                        i--;
                    }
                }
                new ChatRecyclerView().setConfig(recyclerView,IndividualChatUI.this,messages,keys);
            }

            @Override
            public void DataIsInserted() { }

            @Override
            public void DataIsUpdated() { }

            @Override
            public void DataIsDeleted() { }
        });

        typeMessage = (EditText) findViewById(R.id.typeMessage);

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.setMessage(typeMessage.getText().toString());
                message.setSender(FirebaseDatabaseHelper.getCurrentUser());
                message.setReceiver("winniehui.99@gmail.com");
                new FirebaseDatabaseHelper().sendMessage(message, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Message> messages, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(IndividualChatUI.this,"Message sent",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

    }

}
