package com.shankaryadav.www.whatsappclone.Message;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shankaryadav.www.whatsappclone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MessageActivity extends AppCompatActivity {

    private RecyclerView mMessageList;
    private RecyclerView.Adapter mMessageListAdapter;
    private RecyclerView.LayoutManager mMessageListLayoutManager;

    ArrayList<MessageObject> messageObjectArrayList;

    DatabaseReference chatDatabase;

    private EditText mMessage;
    private Button mSend;

    String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        chatId = getIntent().getExtras().getString("ChatId");

        mMessage = findViewById(R.id.myMessage);
        mSend = findViewById(R.id.send);

        chatDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);

        initializeRecyclerView();
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        getMessage();

    }

    private void getMessage() {

        chatDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    String text = "", creatorId = "";

                    if (dataSnapshot.child("Text").getValue() != null){
                        text  = dataSnapshot.child("Text").getValue().toString();
                    }

                    if (dataSnapshot.child("creator").getValue() != null){
                        creatorId  = dataSnapshot.child("creator").getValue().toString();
                    }

                    MessageObject messageObject = new MessageObject(dataSnapshot.getKey(), text, creatorId);
                    messageObjectArrayList.add(messageObject);
                    mMessageListLayoutManager.scrollToPosition(messageObjectArrayList.size()-1);
                    mMessageListAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {
        String message = mMessage.getText().toString();

        if (!TextUtils.isEmpty(message)){
            DatabaseReference newDatabaseRef = chatDatabase.push();

            Map chatDetails = new HashMap<>();

            chatDetails.put("Text", message);
            chatDetails.put("creator", FirebaseAuth.getInstance().getUid());

            newDatabaseRef.updateChildren(chatDetails);
        }

        mMessage.setText(null);

    }


    private void initializeRecyclerView() {
        messageObjectArrayList = new ArrayList<>();
        mMessageList =  findViewById(R.id.recyclerView);

        mMessageList.setNestedScrollingEnabled(false);
        mMessageList.setHasFixedSize(false);
        mMessageListLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        mMessageList.setLayoutManager(mMessageListLayoutManager);

        mMessageListAdapter = new MessageListAdapter(messageObjectArrayList);

        mMessageList.setAdapter(mMessageListAdapter);

    }
}
