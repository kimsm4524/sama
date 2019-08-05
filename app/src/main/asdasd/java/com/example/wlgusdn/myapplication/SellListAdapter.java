package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SellListAdapter extends BaseAdapter
{


    private ArrayList<SellData> MySellData = new ArrayList<SellData>();
    private int SellListCount=0;
    LayoutInflater inflater = null;


    public SellListAdapter(ArrayList<SellData> mySellData)
    {

        MySellData = mySellData;
        SellListCount = mySellData.size();


    }

    @Override
    public int getCount()
    {

        return SellListCount;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.trade_sell_info, parent, false);
        }

        //

        TextView SellDataCount =  convertView.findViewById(R.id.SellDataCount);
        TextView SellDataPersonNum =  convertView.findViewById(R.id.SellDataPersonNum);
        TextView SellDataDate = convertView.findViewById(R.id.SellDataDate);


        SellDataCount.setText(MySellData.get(position).Count);
        SellDataPersonNum.setText(MySellData.get(position).PersonNum);
        SellDataDate.setText(MySellData.get(position).Date);
        return convertView;
    }
}
