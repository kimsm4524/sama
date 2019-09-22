package com.example.wlgusdn.myapplication;
import android.content.Context;
import android.content.Intent;
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

        TextView CompleteDataTitle =  convertView.findViewById(R.id.CompleteDataTitle);
        TextView CompleteDataCount =  convertView.findViewById(R.id.CompleteDataCount);
        TextView CompleteDataStar = convertView.findViewById(R.id.CompleteDataStar);
        ImageView CompleteImage = convertView.findViewById(R.id.CompleteImage);
        TextView CompletePrice = convertView.findViewById(R.id.CompletePrice);

        CompleteDataCount.setText(MyCompleteData.get(position).Count);
        CompleteDataTitle.setText(MyCompleteData.get(position).Title);
        CompleteDataStar.setText(MyCompleteData.get(position).Star);
        CompletePrice.setText(MyCompleteData.get(position).price+"원");
        Picasso.get().load(MyCompleteData.get(position).url).into(CompleteImage);


        return convertView;
    }

}
