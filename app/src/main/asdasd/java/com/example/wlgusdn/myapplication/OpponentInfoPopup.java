package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class OpponentInfoPopup extends Activity
{

    TextView BusinessNumber,CompanyName,CompanyPlace,Name,Address,Star;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_opponent_info);

        BusinessNumber=findViewById(R.id.Opponent_Business_Number);
        CompanyName=findViewById(R.id.Opponent_Company_Name);
        CompanyPlace=findViewById(R.id.Opponent_Company_Place);
        Name = findViewById(R.id.Opponent_Name);
        Star = findViewById(R.id.Opponent_Star);
        //상대방에 대한 정보 초기화 해줘야함


    }


    void setInfo()
    {

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
