package com.shankaryadav.www.whatsappclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shankaryadav.www.whatsappclone.MainPageActivity;
import com.shankaryadav.www.whatsappclone.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

   private EditText phNumber, mCode;
   private  Button mVerify;

   private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

   String verId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     //   FirebaseApp.initializeApp(this);

        //Grab all the UI element

        phNumber = findViewById(R.id.logInphNumber);
        mCode = findViewById(R.id.logIncode);
        mVerify = findViewById(R.id.logInButton);

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verId != null){
                    // moving further if Code sent on other device
                  verifyPhoneNumberwithCode();
                }
                startPhoneNumberVerification();
            }
        });

        // Handling the callback after getting the verification code

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInwithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Verification failed + " + e.getClass(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verId = s;
                mVerify.setText("Code sent");
            }
        };


    }

    // Code verification on button click

    private void verifyPhoneNumberwithCode()
    {
PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verId, mCode.getText().toString());
signInwithPhoneAuthCredential(credential);
    }

    // Sign in user after Verifying phone number

    private void signInwithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 // Checking 
                    if (user != null){
                       final DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                       mUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                               if (!dataSnapshot.exists()){
                                   Map<String, Object> userMap = new HashMap<>();
                                   userMap.put("name", user.getPhoneNumber());
                                   userMap.put("phone", user.getPhoneNumber());


                                   mUserDb.updateChildren(userMap);

                               }
                           }


                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                               Toast.makeText(getApplicationContext(),"Error----- "+databaseError,Toast.LENGTH_SHORT).show();
                               Log.i("Error--------", " ------ "+ databaseError);
                           }
                       });

                        Log.i("Error--------", " "+ mUserDb);
                    }
//

                    userIsLoggedIn();

                }
            }
        });
    }

 // Moving to Main page Activity after Successfully Log in

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
            finish();
        }
    }

    // Method for sending Code

    private void startPhoneNumberVerification() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

}