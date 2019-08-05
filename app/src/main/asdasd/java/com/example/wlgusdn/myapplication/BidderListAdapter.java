package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BidderListAdapter extends BaseAdapter
{
    private ArrayList<BidderData> BidderData = new ArrayList<BidderData>();
    private int BidderListCount=0;
    LayoutInflater inflater = null;


    public BidderListAdapter(ArrayList<BidderData> bidderData)
    {

        BidderData = bidderData;
        BidderListCount = BidderData.size();


    }

    @Override
    public int getCount()
    {

        return BidderListCount;
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
            convertView = inflater.inflate(R.layout.trade_bidder, parent, false);
        }

        //MyTrade(내 주문 보기) 액티비티에서의 내가 등록한 구매글을 선택했을 때만 나오는
        //입찰자들의 정보

        TextView BidderDataCompanyName =  convertView.findViewById(R.id.BidderDataCompanyName);
        TextView BidderDataTradeNum =  convertView.findViewById(R.id.BidderDataTradeNum);
        TextView BidderDataPrice = convertView.findViewById(R.id.BidderDataPrice);
        TextView BidderDataGrade = convertView.findViewById(R.id.BidderDataGrade);


        BidderDataCompanyName.setText("기업명 : "+BidderData.get(position).BidderCompanyName);
        BidderDataTradeNum.setText("거래횟수 : "+BidderData.get(position).BidderPrice+"회");
        BidderDataPrice.setText("개당 가격 : \n"+BidderData.get(position).BidderPrice+"원");
        BidderDataGrade.setText(BidderData.get(position).BidderGrade);
        return convertView;
    }
}
