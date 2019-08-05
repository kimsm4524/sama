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

public class CompleteListAdapter extends BaseAdapter
{


    private ArrayList<CompleteData> MyCompleteData = new ArrayList<CompleteData>();
    private int CompleteListCount=0;
    LayoutInflater inflater = null;

    public CompleteListAdapter(ArrayList<CompleteData> myCompleteData)
    {

        MyCompleteData = myCompleteData;
        CompleteListCount = myCompleteData.size();

    }



    @Override
    public int getCount()
    {

        return CompleteListCount;
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
            convertView = inflater.inflate(R.layout.trade_complete, parent, false);
        }

        //구매글들에 대한 정보들

        TextView CompleteDataCount =  convertView.findViewById(R.id.CompleteDataCount);
        TextView CompleteDataPrice =  convertView.findViewById(R.id.CompleteDataStar);
        TextView CompleteDataDate = convertView.findViewById(R.id.CompleteDataDate);


        CompleteDataCount.setText(MyCompleteData.get(position).Count);
        CompleteDataPrice.setText(MyCompleteData.get(position).Star);
        CompleteDataDate.setText(MyCompleteData.get(position).Date);
        return convertView;
    }
    public void goTrade()
    {


    }
}
