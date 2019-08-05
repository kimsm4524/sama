package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;


public class MyTrade extends AppCompatActivity
{
    private ListView Selllv = null;
    private ListView Buylv = null;

    private ListView Completelv = null;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrade);

        //여기서 데이터 생성
        //ex)



        String[] count = {"수량 : 1","수량 : 100","수량 : 500","수량 : 203"};
        String[] person = {"입찰자 수 : 3","입찰자 수 : 5","입찰자 수 : 2","입찰자 수 : 10"};
        String[] date = {"희망배송일 : \n2019년 9월 1일","희망배송일 : \n2019년 1월 11일"
                ,"희망배송일 : \n2019년 10월 20일","희망배송일 : \n2019년 12월 25일"};
        String[] price = {"개당 : 100,000원","개당 : 200,000원","개당 : 95,000원","개당 : 110,000원"};
        String[] star = {"4.0 / 5.0","4.2 / 5.0","3.0 / 5.0","4.5 / 5.0"};

        ArrayList<SellData> SData = new ArrayList<>();
        for(int i=0;i<4;i++)
        {

            SellData s = new SellData();
            s.Count = count[i];
            s.PersonNum = person[i];
            s.Date = date[i];
            SData.add(s);

        }
        ArrayList<BuyData> BData = new ArrayList<>();
        for(int i=0;i<4;i++)
        {

            BuyData b = new BuyData();
            b.Count = count[i];
            b.Price = price[i];
            b.Date = date[i];
            BData.add(b);

        }
        ArrayList<CompleteData> CData = new ArrayList<>();
        for(int i=0;i<4;i++)
        {

            CompleteData c = new CompleteData();
            c.Count = count[i];
            c.Star = star[i];
            c.Date = date[i];
            CData.add(c);

        }
        Selllv = (ListView)findViewById(R.id.SellList);
        SellListAdapter sadapter = new SellListAdapter(SData) ;
        Selllv.setAdapter(sadapter);

        Buylv = (ListView)findViewById(R.id.BuyList);
        BuyListAdapter badapter = new BuyListAdapter(BData) ;
        Buylv.setAdapter(badapter);

        Completelv = (ListView)findViewById(R.id.CompleteList);
        CompleteListAdapter cadapter = new CompleteListAdapter(CData) ;
        Completelv.setAdapter(cadapter);

        Buylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(MyTrade.this,Trade.class);
                startActivity(in);
            }
        });
        Selllv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(MyTrade.this,Trade.class);
                startActivity(in);
            }
        });
       Completelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent in = new Intent(MyTrade.this,Trade.class);
               // startActivity(in);
                //완료된 거래 정보 보여주기
            }
        });

        setListViewHeightBasedOnChildren(Selllv);
        setListViewHeightBasedOnChildren(Buylv);
        setListViewHeightBasedOnChildren(Completelv);




    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }





}
