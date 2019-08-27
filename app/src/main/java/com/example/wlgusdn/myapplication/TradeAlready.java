package com.example.wlgusdn.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3;
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
import java.util.ArrayList;
import java.util.List;

public class TradeAlready  extends Trade
{
    ViewPager Buyer_Vp,Bidder_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character,Buyer_Address;
    TextView Bidder_Count,Bidder_Price,Bidder_Date,Bidder_Character;
    RadioButton Bidder_RadioButon;
    ArrayList<UserData> BuyerData;
    ArrayList<UserData> BidderData;

    UserData Buyer,Bidder;
    Button complete;
    ListView Buyerlv,Bidderlv;
    //거래 상태 변수
    String condition;

    public ImageView imgAndroid,back;
    public Animation anim;

    int picturenum,picturenuma;
    String postnum,Sellid,Postid;

    Bitmap[] bmo,bmoa;
    AmazonS3 s3;
    private Loading l;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_already);


        l = new Loading(TradeAlready.this);

        Intent intent = getIntent();
        postnum = intent.getStringExtra("postnum");
        Sellid = intent.getStringExtra("Sellid");
        Postid = intent.getStringExtra("Postid");
        Log.i("idzzzzzzzzzzzzzz",Sellid+Postid);

        picturenum=0;
        picturenuma=0;

        complete = findViewById(R.id.Already_Button_Complete);



        Buyer_Vp = findViewById(R.id.Already_Buyer_ViewPager);
        VpAdapter Buyadapter = new VpAdapter(getLayoutInflater(), 3, 1);//숫자는 글의 사진의 수
        Buyer_Vp.setAdapter(Buyadapter);

        Buyer_Count = findViewById(R.id.Already_Buyer_Count);
        Buyer_Address = findViewById(R.id.already_Buyer_Address);
        Buyer_Date = findViewById(R.id.Already_Buyer_Date);
        Buyer_Character = findViewById(R.id.Already_Buyer_Character);

        Bidder_Vp = findViewById(R.id.Already_Bidder_ViewPager);
        VpAdapter Bidadapter = new VpAdapter(getLayoutInflater(), 3, 1);//숫자는 글의 사진의 수
        Bidder_Vp.setAdapter(Bidadapter);
        Bidder_RadioButon = findViewById(R.id.Already_Bidder_RadioButton);
        Bidder_Count = findViewById(R.id.already_Bidder_Count);
        Bidder_Price = findViewById(R.id.already_Bidder_Price);
        Bidder_Date = findViewById(R.id.already_Bidder_Date);
        Bidder_Character = findViewById(R.id.already_Bidder_Character);









        new UserpostAsync().execute();
        new UsersellAsync().execute();

        ListingAsync list = new ListingAsync();
        list.execute();
        ListauctionAsync lista = new ListauctionAsync();
        lista.execute();





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

    public void GoAttacher(View view) {
        Intent in = new Intent(TradeAlready.this,PictureAttacher.class);
        in.putExtra("PictureCount",picturenum);
        startActivity(in);
    }



    public void Declaration(View view) {

        Toast.makeText(TradeAlready.this, "신고하기 Declaration()", Toast.LENGTH_SHORT).show();

        // 신고 하기 -> 신고사유, 신고내용
        Intent in = new Intent(TradeAlready.this,Declaration.class);
        startActivity(in);

    }




    class ListingAsync extends AsyncTask<Void, String, Void> {
        String count;
        String day;
        String content;
        String address;

        @Override
        protected void onPreExecute() {
            l.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Buyer_Count.setText(count);
            Buyer_Date.setText(day);
            Buyer_Character.setText(content);
            Buyer_Address.setText(address);
            Log.i("picturenumzz",picturenum+"");
            Log.i("conditionzzz",condition);
            TradeAlready.ListImageAsync listimage = new TradeAlready.ListImageAsync();
            listimage.execute();
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
                day = e.select("shipping_date").text().toString();
                content = e.select("contents").text().toString();
                picturenum = Integer.parseInt(e.select("picturenum").text().toString());
                condition = e.select("condition").text().toString();
                address = e.select("address").text().toString();
            }
            publishProgress();
            return null;
        }
    }
    class ListImageAsync extends AsyncTask<File, Void, Void> {

        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenum,1,bmo);//숫자는 글의 사진의 수
            Buyer_Vp.setAdapter(adapter);
            if(Postid.equals(MainActivity.Myname))
            {
                if(condition.equals("1"))
                {
                    complete.setText("배송준비중");
                    complete.setEnabled(false);
                }
                else
                    complete.setText("인수확인");
                //condition 3으로
            }
            else
            {
                complete.setText("인계확인");
                //condition 2으로
            }

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (complete.getText().toString().equals("인계확인")) {
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(TradeAlready.this);
                        // 다이얼로그 메세지
                        alertdialog.setMessage("인계 확인처리 하시겠습니까?");

                        // 확인버튼
                        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new decisionAsync().execute();
                                Toast.makeText(TradeAlready.this, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                                //구매글, 글에 대한 입찰글 삭제

                            }
                        });

                        // 취소버튼
                        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Do Nothing

                            }
                        });
                        // 메인 다이얼로그 생성
                        AlertDialog alert = alertdialog.create();
                        // 아이콘 설정
                        alert.setIcon(R.drawable.logo);
                        // 타이틀
                        // alert.setTitle("");
                        // 다이얼로그 보기
                        alert.show();
                        Toast.makeText(TradeAlready.this, complete.getText(), Toast.LENGTH_LONG).show();

                    }else
                    {
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(TradeAlready.this);
                        // 다이얼로그 메세지
                        alertdialog.setMessage("인수확인 처리 하시겠습니까?");

                        // 확인버튼
                        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new decisionAsync().execute();
                                Toast.makeText(TradeAlready.this, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                                //구매글, 글에 대한 입찰글 삭제

                            }
                        });

                        // 취소버튼
                        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Do Nothing

                            }
                        });
                        // 메인 다이얼로그 생성
                        AlertDialog alert = alertdialog.create();
                        // 아이콘 설정
                        alert.setIcon(R.drawable.logo);
                        // 타이틀
                        // alert.setTitle("");
                        // 다이얼로그 보기
                        alert.show();
                        Toast.makeText(TradeAlready.this, complete.getText(), Toast.LENGTH_LONG).show();

                    }
                }
            });
            l.dismiss();
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
        String day;
        String content;
        String price;

        @Override
        protected void onPreExecute() {
            l.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Bidder_Count.setText(count);
            Bidder_Date.setText(day);
            Bidder_Character.setText(content);
            Bidder_Price.setText(price);

            Log.d("rkwkwha","Main.Myname"+MainActivity.Myname);
            Log.d("rkwkwha","Postid"+Postid);
            Log.d("rkwkwha","Sellid"+Sellid);


            Log.i("picturenumzz",picturenum+"");
            ListaucImageAsync image = new ListaucImageAsync();
            image.execute();
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/getauction.jsp";
            Document xml = null;
            String param = "?postnum="+postnum+"&sellerid="+Sellid;
            String u = url + param;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {


                count = e.select("quantity").text().toString();
                day = e.select("sample_shipping_date").text().toString();
                content = e.select("contents").text().toString();
                picturenuma = Integer.parseInt(e.select("picturenum").text().toString());
                price = e.select("price").text().toString();

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
            Bidder_Vp.setAdapter(adapter);
        }



        protected Void doInBackground(File... values) {//user thread
            bmoa = new Bitmap[picturenuma];
            for(int i=0;i<picturenuma;i++)
            {
                bmoa[i]=loadBitmap("http://samaimage.s3.ap-northeast-2.amazonaws.com/auction/" + postnum + "/"+Sellid+"/" + "image"+i+".png");
            }
            publishProgress();
            return null;
        }
    }
    class decisionAsync extends AsyncTask<Void, String, Void> {
        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url;
            String param;
            url = "http://52.79.255.160:8080/decision.jsp";
            if (complete.getText().equals("인계확인"))
                param = "?postnum=" + postnum + "&sellerid=" + Sellid+"&condition=2";
            else
                param = "?postnum=" + postnum + "&sellerid=" + Sellid+"&condition=3";
            Document xml = null;
            String u = url + param;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
    private class splashhandler implements Runnable{
        public void run(){

          l.dismiss();
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
    class UserpostAsync extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Buyerlv = (ListView) findViewById(R.id.Already_Buyer_Listview);
            UserAdapter buyeradapter = new UserAdapter(BuyerData);
            Buyerlv.setAdapter(buyeradapter);
            Buyerlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(TradeAlready.this, OpponentInfoPopup.class);
                    in.putExtra("Id", Postid);

                    startActivity(in);
                }
            });
            setListViewHeightBasedOnChildren(Buyerlv);

        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/userinfo.jsp";
            String param = "?id="+Postid;
            Document xml = null;
            String u = url + param;
            Log.i("zzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }
            BuyerData = new ArrayList<>();

            Buyer = new UserData();
            Buyer.Id = Postid;

            Elements result = xml.select("data");

            for (Element e : result) {
                Buyer.Name = e.select("name").text().toString();
                Buyer.Phone = e.select("phonenumber").text().toString();
                point = e.select("point").text().toString();
            }
            BuyerData.add(Buyer);
            publishProgress();
            return null;
        }
    }
    class UsersellAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Bidderlv = (ListView) findViewById(R.id.Already_Bidder_Listview);
            UserAdapter buyeradapter = new UserAdapter(BidderData);
            Bidderlv.setAdapter(buyeradapter);
            Bidderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(TradeAlready.this, OpponentInfoPopup.class);
                    in.putExtra("Id", Sellid);
                    startActivity(in);
                }
            });
            setListViewHeightBasedOnChildren(Bidderlv);
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/userinfo.jsp";
            String param = "?id="+Sellid;
            Document xml = null;
            String u = url + param;
            Log.i("zzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }
            BidderData = new ArrayList<>();

            Bidder = new UserData();
            Bidder.Id = Sellid;

            Elements result = xml.select("data");

            for (Element e : result) {
                Bidder.Name = e.select("name").text().toString();
                Bidder.Phone = e.select("phonenumber").text().toString();
            }
            BidderData.add(Bidder);
            publishProgress();
            return null;
        }
    }

}
