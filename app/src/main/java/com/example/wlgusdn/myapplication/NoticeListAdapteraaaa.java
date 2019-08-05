package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NoticeListAdapteraaaa extends BaseAdapter
{


    private ArrayList<NoticeData> MyNoticeData = new ArrayList<NoticeData>();
    private int NoticeListCount=0;
    LayoutInflater inflater = null;
    private boolean Check;
   public static int intcheck;
    SharedPreferences sf;

    public NoticeListAdapteraaaa(ArrayList<NoticeData> myNoticeData, boolean check)
    {
        intcheck=myNoticeData.size();
        MyNoticeData = myNoticeData;
        NoticeListCount = myNoticeData.size();
        Check=check;
        //sf = getSharedPreferences(Login, 0);



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
    public View getView(final int position, View convertView, ViewGroup parent)
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


            ImageView NoticeDataImage = convertView.findViewById(R.id.Notice_imageView);
            TextView NoticeDataCount = convertView.findViewById(R.id.Notice_Count);
            TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice_BidCount);
            TextView NoticeDataDate = convertView.findViewById(R.id.Notice_Title);
            TextView NoticeDataTitle = convertView.findViewById(R.id.Notice_Title);
            TextView NoticeDataHit = convertView.findViewById(R.id.Notice_Hit_TextView);
            Log.i("zzzzzzzzzzzzzz",MyNoticeData.get(position).url);
            ToggleButton tb = convertView.findViewById(R.id.Notice_Toggle);




            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)//선택됨
                    {
                        //장바구니에 담겨야함
                    }
                    else
                    {
                        //장바구니에서 삭제해야함
                    }

                }
            });

            Picasso.get().load(MyNoticeData.get(position).url).into(NoticeDataImage);
            NoticeDataTitle.setText(MyNoticeData.get(position).Title);
            NoticeDataCount.setText("수량 : "+MyNoticeData.get(position).Count);
            NoticeDataPersonNum.setText("입찰자 수 : "+MyNoticeData.get(position).PersonNum);
            NoticeDataHit.setText("조회수 : "+MyNoticeData.get(position).Hit);
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
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("asdasdasd2", String.valueOf(position)+" "+MyNoticeData.get(position).Count);
                    Intent i = null;
                    NoticeData temp = (NoticeData)MyNoticeData.get(position);

                    if(!temp.id.equals(sf.getString("id","")))
                    {
                    //    i = new Intent(context, TradeBidderYet.class);
                    }
                    else
                    {
                      //  i = new Intent(MainActivity.this, TradeBuyerYet.class);
                    }
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",temp.postnum);

                    //startActivity(i);


                }
            });
            ImageView NoticeDataImage = convertView.findViewById(R.id.Notice1_imageView);
            TextView NoticeDataCount = convertView.findViewById(R.id.Notice1_Count);
            TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice1_BidCount);
            TextView NoticeDataDate = convertView.findViewById(R.id.Notice1_Title);
            TextView NoticeTitle = convertView.findViewById(R.id.Notice1_Title);
            Log.i("zzzzzzzzzzzzzz",MyNoticeData.get(position).url);


            Picasso.get().load(MyNoticeData.get(position).url).into(NoticeDataImage);
            NoticeDataCount.setText("수량 : "+MyNoticeData.get(position).Count);
            NoticeDataPersonNum.setText("입찰자 수 : "+MyNoticeData.get(position).PersonNum);
            NoticeTitle.setText("제목 : "+MyNoticeData.get(position).Title);

        }


        return convertView;
    }



}

