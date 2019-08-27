package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class     Complete  extends AppCompatActivity {

    ViewPager Complete_ViewPager,Complete_ViewPager1;
    TextView Complete_Radio_Text,Complete_Count,Complete_Character,Complete_Address,Complete_Date;
    TextView Complete_Radio_Text1,Complete_Count1,Complete_Character1,Complete_Address1,Complete_Date1;
    String postnum,seller;
    int picturenum,picturenuma;
    Bitmap[] bmo,bmoa;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        postnum = in.getStringExtra("postnum");
        seller = in.getStringExtra("seller");
        setContentView(R.layout.activity_complete_trade);

        Complete_ViewPager=findViewById(R.id.Complete_ViewPager);
        Complete_ViewPager1= findViewById(R.id.Complete_ViewPager1);

        VpAdapter adapter = new VpAdapter(getLayoutInflater(),3,1);//숫자는 글의 사진의 수
        VpAdapter adapter1 = new VpAdapter(getLayoutInflater(),3,1);

        Complete_ViewPager.setAdapter(adapter);
        Complete_ViewPager1.setAdapter(adapter1);



        Complete_Radio_Text=findViewById(R.id.Complete_RadioText);
        Complete_Count = findViewById(R.id.Complete_Count);
        Complete_Character=findViewById(R.id.Complete_Condition);
        Complete_Address = findViewById(R.id.Complete_Address);
        Complete_Date = findViewById(R.id.Complete_Date);


        Complete_Radio_Text1=findViewById(R.id.Complete_RadioText1);
        Complete_Count1 = findViewById(R.id.Complete_Count1);
        Complete_Character1=findViewById(R.id.Complete_Condition1);
        Complete_Address1 = findViewById(R.id.Complete_Address1);
        Complete_Date1 = findViewById(R.id.Complete_Date1);
        new ListingAsync().execute();
        new ListauctionAsync().execute();

        //if(기성품 or 주문제작)
        // Complete_Radio_Text.setText("");




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    class ListingAsync extends AsyncTask<Void, String, Void> {
        String count;
        String date;
        String address;
        String content;

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Complete_Address.setText("주소 : "+address);
            Complete_Count.setText("수량 : "+count);
            Complete_Character.setText("내용 : " +content);
            Complete_Date.setText("날짜 : "+date);
            ListImageAsync list = new ListImageAsync();
            list.execute();
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/getpost.jsp";
            Document xml = null;
            String param = "?postnum="+postnum;
            String u = url + param;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {


                count = e.select("quantity").text().toString();
                date = e.select("shipping_date").text().toString();
                content = e.select("contents").text().toString();
                address = e.select("address").text().toString();
                picturenum = Integer.parseInt(e.select("picturenum").text().toString());

            }
            publishProgress();
            return null;
        }
    }
    class ListImageAsync extends AsyncTask<File, Void, Void>
    {
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenum,1,bmo);//숫자는 글의 사진의 수
            Complete_ViewPager.setAdapter(adapter);
        }



        protected Void doInBackground(File... values) {//user thread
            bmo = new Bitmap[picturenum];
            for(int i=0;i<picturenum;i++)
            {
                bmo[i]=loadBitmap("http://samaimage.s3.ap-northeast-2.amazonaws.com/post/" + postnum + "/" + "image"+i+".png");
            }
            publishProgress();
            return null;
        }
    }
    class ListauctionAsync extends AsyncTask<Void, String, Void> {
        String count;
        String date;
        String address;
        String content;

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Complete_Address1.setText("가격 : "+address);
            Complete_Count1.setText("수량 : "+count);
            Complete_Character1.setText("내용 : "+content);
            Complete_Date1.setText("날짜 : "+date);
            Log.i("picturenumzz",picturenum+"");
            ListaucImageAsync image = new ListaucImageAsync();
            image.execute();
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/getauction.jsp";
            Document xml = null;
            String param = "?postnum="+postnum+"&sellerid="+seller;
            String u = url + param;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {


                count = e.select("quantity").text().toString();
                date = e.select("sample_shipping_date").text().toString();
                content = e.select("contents").text().toString();
                address = e.select("price").text().toString();
                picturenuma = Integer.parseInt(e.select("picturenum").text().toString());

            }
            publishProgress();
            return null;
        }
    }
    class ListaucImageAsync extends AsyncTask<File, Void, Void>
    {
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenuma,3,bmoa);//숫자는 글의 사진의 수
            Complete_ViewPager1.setAdapter(adapter);
        }



        protected Void doInBackground(File... values) {//user thread
//            fso = new File[picturenum];
            bmoa = new Bitmap[picturenuma];
            for(int i=0;i<picturenuma;i++)
            {
                bmoa[i]=loadBitmap("http://samaimage.s3.ap-northeast-2.amazonaws.com/auction/" + postnum + "/"+seller+"/" + "image"+i+".png");
            }
            publishProgress();
            return null;
        }
    }
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }
}

