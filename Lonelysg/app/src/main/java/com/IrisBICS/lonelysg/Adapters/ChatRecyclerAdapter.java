package com.IrisBICS.lonelysg.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.IrisBICS.lonelysg.FirebaseAuthHelper;
import com.IrisBICS.lonelysg.Models.Message;
import com.IrisBICS.lonelysg.R;

import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MessageViewHolder>{

    private List<Message> messageList;
    private ItemClickListener mClickListener;
    private LayoutInflater mInflater;

    public static final int SENDER_MESSAGE = 1;
    public static final int RECEIVER_MESSAGE = 0;
    private String currentUser;

    public ChatRecyclerAdapter(Context context, List<Message> messageList) {
        this.mInflater = LayoutInflater.from(context);
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SENDER_MESSAGE){
            View view = mInflater.inflate(R.layout.chat_view_sender, parent, false);
            return new MessageViewHolder(view);
        }
        else {
            View view = mInflater.inflate(R.layout.chat_view_receiver, parent, false);
            return new MessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public int getItemViewType(int position){
        currentUser = FirebaseAuthHelper.getCurrentUser();
        if (messageList.get(position).getSender().equals(currentUser)){
            return SENDER_MESSAGE;
        }
        else return RECEIVER_MESSAGE;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView user;
        TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            message = itemView.findViewById(R.id.message);
            itemView.setOnClickListener(this);
        }

        public void bind(Message m){
            message.setText(m.getMessage());
            user.setText(m.getSender());
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting Message at click position
    Message getMessage(int id) {
        return messageList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

}
