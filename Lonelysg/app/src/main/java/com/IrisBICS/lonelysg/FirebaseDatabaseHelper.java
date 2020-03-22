package com.IrisBICS.lonelysg;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceMessages;
    private List<Message> messages = new ArrayList<>();
    private List<String> chatUsersList = new ArrayList<>();

    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public FirebaseDatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceMessages = mDatabase.getReference("Messages");
    }

    public interface DataStatus{
        void DataIsLoaded(List<Message> messages, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readMessage(final DataStatus dataStatus){
        mReferenceMessages.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messages.clear();
                    List<String> keys = new ArrayList<>();
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                        keys.add(keyNode.getKey());
                        Message message = keyNode.getValue(Message.class);
                        messages.add(message);
                    }
                    dataStatus.DataIsLoaded(messages, keys);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
        });
    }

    public void sendMessage(Message message, final DataStatus dataStatus){
        String key = mReferenceMessages.push().getKey();
        mReferenceMessages.child(key).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();

            }
        });
    }

    public static String getCurrentUser(){
        return currentUser.getEmail();
    }


}
