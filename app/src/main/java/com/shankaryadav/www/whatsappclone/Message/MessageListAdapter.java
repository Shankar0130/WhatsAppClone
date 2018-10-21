package com.shankaryadav.www.whatsappclone.Message;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shankaryadav.www.whatsappclone.R;

import java.util.ArrayList;

class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    private ArrayList<MessageObject> messageObjects;

    public MessageListAdapter(ArrayList<MessageObject> messageObjects) {
        this.messageObjects = messageObjects;
    }

    @NonNull
    @Override
    public MessageListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item,viewGroup,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        MessageListAdapter.MyViewHolder holder = new MessageListAdapter.MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.mMessage.setText(messageObjects.get(i).getmText());

        myViewHolder.sender.setText(messageObjects.get(i).getSenderId());


    }

    @Override
    public int getItemCount() {
        return messageObjects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mMessage, sender;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mMessage =(TextView) itemView.findViewById(R.id.message);
            sender =(TextView) itemView.findViewById(R.id.sender);
        }
    }
}
