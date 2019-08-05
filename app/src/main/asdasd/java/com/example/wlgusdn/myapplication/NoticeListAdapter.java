package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeListAdapter extends BaseAdapter
{


    private ArrayList<NoticeData> MyNoticeData = new ArrayList<NoticeData>();
    private int NoticeListCount=0;
    LayoutInflater inflater = null;
    private boolean Check;

    public NoticeListAdapter(ArrayList<NoticeData> myNoticeData,boolean check)
    {

        MyNoticeData = myNoticeData;
        NoticeListCount = myNoticeData.size();
        Check=check;
    }

    @Override
    public int getCount()
    {

        return NoticeListCount;
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

        if(Check==true) {

            if (convertView == null) {
                final Context context = parent.getContext();
                if (inflater == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView = inflater.inflate(R.layout.notice_info, parent, false);
            }

            //

            TextView NoticeDataCount = convertView.findViewById(R.id.Notice_Count);
            TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice_BidCount);
            TextView NoticeDataDate = convertView.findViewById(R.id.Notice_Day);


            NoticeDataCount.setText(MyNoticeData.get(position).Count);
            NoticeDataPersonNum.setText(MyNoticeData.get(position).PersonNum);
            NoticeDataDate.setText(MyNoticeData.get(position).Date);
        }
        else
        {
            if (convertView == null) {
                final Context context = parent.getContext();
                if (inflater == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView = inflater.inflate(R.layout.notice, parent, false);
            }

            //

            TextView NoticeDataCount = convertView.findViewById(R.id.Notice1_Count);
            TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice1_BidCount);
            TextView NoticeDataDate = convertView.findViewById(R.id.Notice1_Day);


            NoticeDataCount.setText(MyNoticeData.get(position).Count);
            NoticeDataPersonNum.setText(MyNoticeData.get(position).PersonNum);
            NoticeDataDate.setText(MyNoticeData.get(position).Date);
        }
        return convertView;
        }



    }


