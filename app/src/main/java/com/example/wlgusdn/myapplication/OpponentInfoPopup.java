package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class OpponentInfoPopup extends Activity
{

    TextView BusinessNumber,CompanyName,CompanyPlace,Name,Address,Star;

    String Id;
    private ListView Bidderlv = null;
    ViewPager Buyer_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character,Buyer_Address;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_opponent_info);

        Intent in = getIntent();
        Id=in.getStringExtra("Id");
        Toast.makeText(OpponentInfoPopup.this,Id,Toast.LENGTH_LONG).show();

        BusinessNumber=findViewById(R.id.Opponent_Business_Number);
        CompanyName=findViewById(R.id.Opponent_Company_Name);
        CompanyPlace=findViewById(R.id.Opponent_Company_Place);
        Name = findViewById(R.id.Opponent_Name);
        Star = findViewById(R.id.Opponent_Star);
        //상대방에 대한 정보 초기화 해줘야함ㅜ
        new UserAsync().execute();


    }


    class UserAsync extends AsyncTask<Void, String, Void> {
        String Bus_num,ComName,Comadd,name,star;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            BusinessNumber.setText("사업자 등록번호 : "+Bus_num);
            CompanyName.setText("사업명 : "+ComName);
            CompanyPlace.setText("사업자 등록지 : "+Comadd);
            Name.setText("이름 : "+name);
            Star.setText("평점 : "+star);
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/userinfo.jsp";
            String param = "?id="+Id;
            Document xml = null;
            String u = url + param;
            Log.i("zzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {
                name = e.select("name").text().toString();
                Bus_num = e.select("business_num").text().toString();
                ComName = e.select("business_name").text().toString();
                Comadd = e.select("business_add").text().toString();
                star = e.select("grade").text().toString();
            }
            Log.i("zzzzzzzzzzzzz",name);
            publishProgress();
            return null;
        }
    }


}
