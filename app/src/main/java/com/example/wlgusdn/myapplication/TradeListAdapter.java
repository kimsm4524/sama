package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TradeListAdapter extends BaseAdapter {


        private ArrayList<TradeData> MyTradeData = new ArrayList<TradeData>();
        private int TradeListCount=0;
        LayoutInflater inflater = null;


    public TradeListAdapter(ArrayList<TradeData> myTradeData)
        {

            MyTradeData = myTradeData;
            TradeListCount = myTradeData.size();


        }

        @Override
        public int getCount()
        {

            return TradeListCount;
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
            TextView SellDataPrice  = convertView.findViewById(R.id.SellDataPrice);
            TextView SellDataTitle = convertView.findViewById(R.id.SellDataTitle);
            ImageView SellImage = convertView.findViewById(R.id.SellImage);

            Picasso.get().load(MyTradeData.get(position).url).into(SellImage);
            SellDataTitle.setText("제목 : " + MyTradeData.get(position).Title);
            SellDataCount.setText("수랑 : "+MyTradeData.get(position).Count);
            SellDataPrice.setText("개당 가격 : "+MyTradeData.get(position).Price);
            return convertView;
        }
}
