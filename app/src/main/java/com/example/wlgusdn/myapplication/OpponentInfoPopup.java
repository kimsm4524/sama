package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OpponentInfoPopup extends Activity
{

    TextView BusinessNumber,CompanyName,CompanyPlace,Name,Address,Star;

    String Id;
    private ListView Bidderlv = null;
    ViewPager Buyer_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character,Buyer_Address;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_opponent_info);

        Intent in = getIntent();
        Id=in.getStringExtra("Id");

        Toast.makeText(OpponentInfoPopup.this,Id,Toast.LENGTH_LONG).show();

        BusinessNumber=findViewById(R.id.Opponent_Business_Number);
        CompanyName=findViewById(R.id.Opponent_Company_Name);
        CompanyPlace=findViewById(R.id.Opponent_Company_Place);
        Name = findViewById(R.id.Opponent_Name);
        Star = findViewById(R.id.Opponent_Star);
        //상대방에 대한 정보 초기화 해줘야함


    }


    void setInfo()
    {
        //Id에 따른 정보들 불러오기


        /*
        BusinessNumber.setText();
        CompanyPlace.setText();
        CompanyName.setText();
        Name.setText();
        Address.setText();
        Star.setText();
        */

    }


}
