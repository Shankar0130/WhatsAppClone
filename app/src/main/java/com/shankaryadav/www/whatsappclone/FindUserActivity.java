package com.shankaryadav.www.whatsappclone;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shankaryadav.www.whatsappclone.User.UserListAdapter;
import com.shankaryadav.www.whatsappclone.User.UserObject;
import com.shankaryadav.www.whatsappclone.Utils.CountryToPhonePrefix;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userObjectArrayList, contactObjectArraList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        userObjectArrayList = new ArrayList<>();
        contactObjectArraList = new ArrayList<>();

        initializeRecyclerView();

        getContactList();

    }

    private void initializeRecyclerView() {
        mUserList =  findViewById(R.id.userList);

        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        mUserList.setLayoutManager(mUserListLayoutManager);

        mUserListAdapter = new UserListAdapter(userObjectArrayList);

        mUserList.setAdapter(mUserListAdapter);

    }

    private String getCountryIso(){
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        if (telephonyManager.getNetworkCountryIso() != null && !telephonyManager.getNetworkCountryIso().equals("")){
            iso = telephonyManager.getNetworkCountryIso();
        }


        return CountryToPhonePrefix.getPhone(iso);
    }

    private  void getContactList(){

        String ISOPrefix = getCountryIso();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));



            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if (!String.valueOf(phone.charAt(0)).equals("+")){
                phone = ISOPrefix + phone;
            }
            Log.i("Error--------", " " + phone + " ----- " + name + "----" + phones);

            UserObject mContacts = new UserObject(name, phone, "");

            contactObjectArraList.add(mContacts);
           getUserDetails(mContacts);
        }
        phones.close();
    }

    private void getUserDetails(UserObject mContact){
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String phone = "", name = "";

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if (childSnapshot.child("phone").getValue()  != null){
                            phone = childSnapshot.child("phone").getValue().toString();
                        }
                        if (childSnapshot.child("name").getValue() != null){
                            name = childSnapshot.child("name").getValue().toString();
                        }

                        Log.i("Error--------", " " + phone + " ----- " + name + "----" + childSnapshot);

                        UserObject mUser = new UserObject(name,phone,childSnapshot.getKey());

                        for (UserObject objectIterator : contactObjectArraList){
                            if (objectIterator.getPhone().equals(phone)){
                                mUser.setName(objectIterator.getName());
                            }
                        }



                        userObjectArrayList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindUserActivity.this, "Error------------- " + databaseError, Toast.LENGTH_SHORT).show();
                Log.i("Error--------", " ------ "+ databaseError);
            }
        });


    }
}
