package com.example.wlgusdn.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Complete  extends AppCompatActivity {

    ViewPager Complete_ViewPager;
    TextView Complete_Radio_Text,Complete_Count,Complete_Character;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complete_trade);

        Complete_ViewPager=findViewById(R.id.Complete_ViewPager);
        VpAdapter adapter = new VpAdapter(getLayoutInflater(),3,1);//숫자는 글의 사진의 수
        Complete_ViewPager.setAdapter(adapter);
        Complete_Radio_Text=findViewById(R.id.Complete_RadioText);
        Complete_Count = findViewById(R.id.Complete_Count);
        Complete_Character=findViewById(R.id.Complete_Condition);

        //if(기성품 or 주문제작)
        // Complete_Radio_Text.setText("");




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }



}
