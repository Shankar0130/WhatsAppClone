package com.shankaryadav.www.whatsappclone.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shankaryadav.www.whatsappclone.FindUserActivity;
import com.shankaryadav.www.whatsappclone.MainPageActivity;
import com.shankaryadav.www.whatsappclone.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {


    private ArrayList<UserObject> userObjects;

    public UserListAdapter(ArrayList<UserObject> userObjects) {
        this.userObjects = userObjects;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user,viewGroup,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        MyViewHolder holder = new MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder myViewHolder, final int i) {
     myViewHolder.mName.setText(userObjects.get(i).getName());
     myViewHolder.mPhone.setText(userObjects.get(i).getPhone());
     myViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

             FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue("true");
             FirebaseDatabase.getInstance().getReference().child("user").child(userObjects.get(i).getUid()).child("chat").child(key).setValue("true");

             Intent intent = new Intent(view.getContext(),MainPageActivity.class);
             view.getContext().startActivity(intent);



         }
     });
    }

    @Override
    public int getItemCount() {
        return userObjects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
         public TextView mName, mPhone;
         public LinearLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.name);
            mPhone = (TextView) itemView.findViewById(R.id.phone);
            mLayout = (LinearLayout) itemView.findViewById(R.id.mLayout);
        }
    }
}
