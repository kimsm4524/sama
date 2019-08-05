package com.example.wlgusdn.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TradeBid extends AppCompatActivity
{
    TextView BidCount,Date,Condition;

    LinearLayout Sample;
    ImageView iv;
    RadioButton rb1,rb2;
    TextView Year,Month,Day;
    TextView SampleYear,SampleMonth,SampleDay;
    EditText Count, Price, Character;
    RadioGroup rg;
    int PicCount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trade_bid);

        iv = findViewById(R.id.Trade_Bid_Picture);
        Sample = findViewById(R.id.Sample_Trade_Bid_Notice);
        rg=findViewById(R.id.Trade_Bid_Radio_Group);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.Trade_Bid_RadioButton1)
                {
                    Sample.setVisibility(View.GONE);
                }
                else if(checkedId==R.id.Trade_Bid_RadioButton2)
                {
                    Sample.setVisibility(View.VISIBLE);
                }
            }
        });
        rb1 = findViewById(R.id.Trade_Bid_RadioButton1);//기성품
        rb2=findViewById(R.id.Trade_Bid_RadioButton2);//주문제작
        //주문제작이 가능한지 라디오버튼
        Year = findViewById(R.id.Trade_Bid_YearText);
        Month = findViewById(R.id.Trade_Bid_MonthText);
        Day = findViewById(R.id.Trade_Bid_DayText);
        SampleYear = findViewById(R.id.Sample_Trade_Bid_YearText);
        SampleMonth = findViewById(R.id.Sample_Trade_Bid_MonthText);
        SampleDay = findViewById(R.id.Sample_Trade_Bid_DayText);

        Count = findViewById(R.id.Trade_Bid_Countitem);
        //물건 수량
        Price = findViewById(R.id.Trade_Bid_Priceitem);
        //물건 개당 가격
        Character = findViewById(R.id.Trade_Bid_Item_Character);
        //기타 사항
        PicCount =0;

    }


    public void Start_Trade(View view)
    {
        //거래 시작하기




    }

}
