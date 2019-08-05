package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        TextView BuyDataCount =  convertView.findViewById(R.id.BuyDataCount);
        TextView BuyDataPrice =  convertView.findViewById(R.id.BuyDataPrice);
        TextView BuyDataDate = convertView.findViewById(R.id.BuyDataDate);


        BuyDataCount.setText(MyBuyData.get(position).Count);
        BuyDataPrice.setText(MyBuyData.get(position).Price);
        BuyDataDate.setText(MyBuyData.get(position).Date);
        return convertView;
    }
    public void goTrade()
    {


    }
}
