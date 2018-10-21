package com.shankaryadav.www.whatsappclone.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shankaryadav.www.whatsappclone.Message.MessageActivity;
import com.shankaryadav.www.whatsappclone.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private ArrayList<ChatObject> chatObjects;

    public ChatListAdapter(ArrayList<ChatObject> chatObjects) {
        this.chatObjects = chatObjects;
    }

    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat,viewGroup,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        ChatListAdapter.MyViewHolder holder = new ChatListAdapter.MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.MyViewHolder myViewHolder, final int i) {

       myViewHolder.mTitle.setText(chatObjects.get(i).getChatId());

        myViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putString("ChatId", chatObjects.get(myViewHolder.getAdapterPosition()).getChatId());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatObjects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           mTitle =(TextView) itemView.findViewById(R.id.titleName);
           mLayout =(LinearLayout) itemView.findViewById(R.id.chatLayout);
        }
    }
}
