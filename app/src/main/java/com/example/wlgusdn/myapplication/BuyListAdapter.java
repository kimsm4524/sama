package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyListAdapter extends BaseAdapter
{


    private ArrayList<BuyData> MyBuyData = new ArrayList<BuyData>();
    private int BuyListCount=0;
    LayoutInflater inflater = null;

    public BuyListAdapter(ArrayList<BuyData> myBuyData)
    {

        MyBuyData = myBuyData;
        BuyListCount = myBuyData.size();

    }



    @Override
    public int getCount()
    {

        return BuyListCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.trade_buy_info, parent, false);
        }

        //구매글들에 대한 정보들

        ImageView BuyDataImage = convertView.findViewById(R.id.BuyImage);
        TextView BuyDataTitle = convertView.findViewById(R.id.BuyDataTitle);
        TextView BuyDataCount = convertView.findViewById(R.id.BuyDataCount);
        TextView BuyDataSeller = convertView.findViewById(R.id.BuyDataSeller);
        TextView BuyDataHit = convertView.findViewById(R.id.BuyDataHit);

        Picasso.get().load(MyBuyData.get(position).url).into(BuyDataImage);
        BuyDataTitle.setText("제목 : " +MyBuyData.get(position).Title);
        BuyDataCount.setText("수량 : "+ MyBuyData.get(position).Count);
        BuyDataSeller.setText("입찰자 수: "+ MyBuyData.get(position).seller);
        BuyDataHit.setText("조회수 : "+ MyBuyData.get(position).hit);
        return convertView;
    }
    public void goTrade()
    {


    }
}
