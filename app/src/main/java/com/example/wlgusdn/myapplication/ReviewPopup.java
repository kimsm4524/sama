package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class ReviewPopup  extends Activity
{
    Button[] Starr;
    EditText textt;
    Button Notice;
    Button Cancel;
    String postnum,Sellid;
    int star;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_review);
        Intent i = getIntent();
        postnum = i.getStringExtra("postnum");
        Sellid = i.getStringExtra("seller");

         Starr = new Button[6];

        Starr[0]=findViewById(R.id.review_star);
        Starr[1]=findViewById(R.id.review_star1);
        Starr[2]=findViewById(R.id.review_star2);
        Starr[3]=findViewById(R.id.review_star3);
        Starr[4]=findViewById(R.id.review_star4);

        textt=findViewById(R.id.review_text);

        Notice=findViewById(R.id.review_notice);
        Cancel=findViewById(R.id.review_cancel);
        star=3;


    }
    public void SetStar(View view) {

        switch (view.getId())
        {

            case R.id.review_star:
            {
                //1점
                Toast.makeText(ReviewPopup.this,"1점",Toast.LENGTH_SHORT).show();
                star = 1;
                break;
            }
            case R.id.review_star1:
            {
                //2점
                Toast.makeText(ReviewPopup.this,"2점",Toast.LENGTH_SHORT).show();
                star = 2;
                break;
            }
            case R.id.review_star2:
            {
                //3점
                Toast.makeText(ReviewPopup.this,"3점",Toast.LENGTH_SHORT).show();
                star = 3;
                break;
            }
            case R.id.review_star3:
            {
                //4점

                Toast.makeText(ReviewPopup.this,"4점",Toast.LENGTH_SHORT).show();
                star = 4;
                break;
            }
            case R.id.review_star4:
            {
                //5점

                Toast.makeText(ReviewPopup.this,"5점",Toast.LENGTH_SHORT).show();
                star = 5;
                break;
            }

        }

    }

    public void Notice_Review(View view)
    {
        new decisionAsync().execute();
    }

    public void Cancel_Review(View view)
    {
        finish();
    }

    class decisionAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            finish();
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url;
            String param;
            url = "http://52.79.255.160:8080/review.jsp";
            param = "?postnum=" + postnum + "&sellerid=" + Sellid+"&condition=3"+"&content="+textt.getText()+"&grade="+star;

            Document xml = null;
            String u = url + param;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }
            publishProgress();
            return null;
        }
    }
}
