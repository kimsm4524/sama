package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Search extends AppCompatActivity
{
    TextView tv,tv1;
    ScrollView scv;
    boolean lastitemVisibleFlag ;
    private ListView Noticelv = null;
    ArrayList<NoticeData> NData;
    NoticeListAdapter nadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searching);
        lastitemVisibleFlag=false;

        scv=findViewById(R.id.Scv);
        tv1 = findViewById(R.id.Searching_Category_Textview);
        tv = findViewById(R.id.SortText);
               final String[] count = {"수량 : 1","수량 : 100","수량 : 500","수량 : 203","수량 : 1","수량 : 100","수량 : 500","수량 : 203"};
        final String[] person = {"입찰자 수 : 3","입찰자 수 : 5","입찰자 수 : 2","입찰자 수 : 10","입찰자 수 : 3","입찰자 수 : 5","입찰자 수 : 2","입찰자 수 : 10"};
        final String[] date = {"희망배송일 : \n2019년 9월 1일","희망배송일 : \n2019년 1월 11일"
                ,"희망배송일 : \n2019년 10월 20일","희망배송일 : \n2019년 12월 25일","희망배송일 : \n2019년 9월 1일","희망배송일 : \n2019년 1월 11일"
                ,"희망배송일 : \n2019년 10월 20일","희망배송일 : \n2019년 12월 25일"};
        NData = new ArrayList<>();
        for(int i=0;i<8;i++)
        {
            NoticeData n = new NoticeData();
            n.Count = count[i%8];
            n.PersonNum = person[i%8];
            n.Date = date[i%8];
            NData.add(n);
        }

        Noticelv = (ListView)findViewById(R.id.NoticeList);
        nadapter = new NoticeListAdapter(NData,true) ;
        Noticelv.setAdapter(nadapter);

        scv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged()
            {
                int _scrollViewPos = scv.getScrollY();
                int _TextView_lines = scv.getChildAt(0).getBottom() - scv.getHeight();
                if (_TextView_lines == _scrollViewPos)
                {


                    for(int i=0;i<8;i++)
                    {
                        NoticeData n = new NoticeData();
                        n.Count = count[i%8];
                        n.PersonNum = person[i%8];
                        n.Date = date[i%8];
                        NData.add(n);


                    }
                    nadapter = new NoticeListAdapter(NData,true) ;
                    nadapter.notifyDataSetChanged();
                    Noticelv.setAdapter(nadapter);
                    setListViewHeightBasedOnChildren(Noticelv);

                }

            }

        });




        Noticelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(Search.this,Trade.class);
                startActivity(in);
            }
        });



        setListViewHeightBasedOnChildren(Noticelv);
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
    public void SelectCategory(View view) {
        if(view.getId()==R.id.SearchButton1) {


            Intent in = new Intent(Search.this, SortPopup.class);
            in.putExtra("type", "furniture");
            startActivityForResult(in, 2);
        }
        else if (view.getId()==R.id.SearchButton2)
            {
                Intent in = new Intent(Search.this, SortPopup.class);
                in.putExtra("type", "dish");
                startActivityForResult(in, 2);

            }
            else
            {
                Intent in = new Intent(Search.this, SortPopup.class);
                in.putExtra("type", "etc");
                startActivityForResult(in, 2);

            }


    }
    public void SelectSort(View view) {
        Intent in = new Intent(Search.this, SortPopup.class);
        in.putExtra("type", "sort");
        startActivityForResult(in, 1);
    }

    public void GoTrade(View view)
    {
        Intent intent = new Intent(Search.this,Trade.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK) {
                //데이터 받기

                String result1 = data.getStringExtra("result1");

                if(result1.equals("닫기"))
                {
                    //do nothing
                }
                else {

                    tv.setText(result1 + "▼");
                    //여기가 정렬기준을 선택했을시 SortPopup.java에서 finish()하고 돌아옴
                    //이곳에서 화면의 나오는 글들을 갱신해주어야함
                }
            }


        }
        else if(requestCode==2)
        {
            if (resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("result");
                 tv1.setText(result);
            }
        }
    }


}