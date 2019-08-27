package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class AlramPopup extends Activity {

    ToggleButton tb1,tb2;
    private FirebaseDatabase mFirebaseDatabase;
    FirebaseAnalytics firebaseanalytics;
    UserData1 userData;
    String id;
    public SharedPreferences sf;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_popup_alram);

        sf = getSharedPreferences( "login",0);
        editor = sf.edit();
        firebaseanalytics = FirebaseAnalytics.getInstance(this);

        Intent in = getIntent();
        id = in.getStringExtra("id");

        tb1 = findViewById(R.id.toggleButton1);
        tb2 = findViewById(R.id.toggleButton2);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        Log.d("samaalarm",sf.getString("SamaAlarm","").toString());

                if(sf.getString("SamaAlarm","").equals("true"))
                {
                    tb1.setChecked(true);
                }
                else
                {
                    tb1.setChecked(false);
                }

                mFirebaseDatabase.getReference("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        userData=dataSnapshot.getValue(UserData1.class);

                        if(userData.TradeAlarm.equals("true"))
                        {
                            tb2.setChecked(true);


                        }
                        else
                        {
                            tb2.setChecked(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });











       tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               if(isChecked==true)
               {

                     editor.putString("SamaAlarm","true");
                   firebaseanalytics.setUserProperty("SamaAlarm","true");

               }
               else
               {
                     editor.putString("SamaAlarm","false");
                   firebaseanalytics.setUserProperty("SamaAlarm","false");


               }

               editor.commit();
           }
       });

        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true)
                {

                    userData.TradeAlarm="true";
                    editor.putString("TradeAlarm","true");
                    mFirebaseDatabase.getReference("users").child(id).setValue(userData);

                }
                else
                {

                    userData.TradeAlarm="false";

                    editor.putString("TradeAlarm","false");
                    mFirebaseDatabase.getReference("users").child(id).setValue(userData);

                }
                editor.commit();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
