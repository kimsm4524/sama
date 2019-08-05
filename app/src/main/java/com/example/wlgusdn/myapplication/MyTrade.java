package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MyTrade extends AppCompatActivity
{
    private ListView Selllv = null;
    private ListView Buylv = null;
    private ListView Tradinglv = null;

    private ListView Completelv = null;
    String idd;
    String sellid;

    ArrayList<SellData> SData;
    ArrayList<SellData> SData1;

    ArrayList<SellData> TData;
    ArrayList<SellData> TData1;


    ArrayList<BuyData> BData;
    ArrayList<BuyData> BData1;

    ArrayList<CompleteData> CData;
    ArrayList<CompleteData> CData1;

    private Loading l;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrade);
        Intent intent = getIntent();
        idd = intent.getStringExtra("id");
        //여기서 데이터 생성
        //ex)
        l = new Loading(MyTrade.this);
        l.show();

        SData = new ArrayList<SellData>();
        BData = new ArrayList<BuyData>();
        CData = new ArrayList<CompleteData>();


        ListingSd sd = new ListingSd();
        sd.execute();
        ListingBd bd = new ListingBd();
        bd.execute();



        Completelv = (ListView)findViewById(R.id.BuyCompleteList);
        CompleteListAdapter cadapter = new CompleteListAdapter(CData) ;
        Completelv.setAdapter(cadapter);









       Completelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent in = new Intent(MyTrade.this,Trade.class);
               // startActivity(in);
                //완료된 거래 정보 보여주기
                Intent in = new Intent(MyTrade.this,Complete.class);
                startActivity(in);
            }
        });

        setListViewHeightBasedOnChildren(Completelv);




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

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
    class ListingBd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Buylv = (ListView)findViewById(R.id.BuyList);
            BuyListAdapter badapter = new BuyListAdapter(BData) ;
            Buylv.setAdapter(badapter);
            Buylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BuyData temp = (BuyData)BData.get(position);

                    Intent i = new Intent(MyTrade.this,TradeBuyerYet.class);
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",temp.postnum);
                    Log.d("chcheck","gogo"+temp.seller);

                    startActivity(i);
                }
            });
            setListViewHeightBasedOnChildren(Buylv);

            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/mytrade.jsp?id="+idd;
            Document xml = null;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            BData.clear();
            for (Element e : result) {
                BuyData bd = new BuyData();
                bd.Title = e.select("title").text().toString();
                bd.Count = e.select("count").text().toString();
                bd.seller = e.select("seller").text().toString();
                bd.postnum = e.select("postnum").text().toString();
                bd.hit= e.select("hit").text().toString();
                bd.url = "http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+bd.postnum+"/image0.png";
                BData.add(bd);
                Log.d("chcheck",bd.Title+bd.Count+bd.seller+bd.postnum);
            }
            publishProgress();
            return null;
        }
    }
    class ListingSd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Selllv = (ListView)findViewById(R.id.SellList);
            SellListAdapter sadapter = new SellListAdapter(SData) ;
            Selllv.setAdapter(sadapter);
            Selllv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MyTrade.this,TradeSelect.class);
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",SData.get(position).postnum);
                    Log.d("chcheck",idd);
                    // i에 postid넣어줘야함
                    i.putExtra("Postid",SData.get(position).postid);
                    i.putExtra("Sellid",MainActivity.Myname);
                    startActivity(i);
                }
            });

            setListViewHeightBasedOnChildren(Selllv);
            Handler hd = new Handler();
            hd.postDelayed(new MyTrade.splashhandler(), 1000);
            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/myauction.jsp?id="+idd;
            Document xml = null;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            SData.clear();
            for (Element e : result) {
                SellData sd = new SellData();
                sd.Count = e.select("count").text().toString();
                sd.Date = e.select("s_date").text().toString();
                sd.Price = e.select("price").text().toString();
                sd.postnum = e.select("postnum").text().toString();
                sd.postid = e.select("postid").text().toString();
                sd.url = "http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/auction/"+sd.postnum+"/"+idd+"/image0.png";
                SData.add(sd);

            }
            publishProgress();
            return null;
        }
    }
    private class splashhandler implements Runnable{
        public void run(){

            l.dismiss();
        }
    }
}
