package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
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

public class TradeAlready  extends Trade
{
    ViewPager Buyer_Vp,Bidder_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character,Buyer_Address;
    TextView Bidder_Count,Bidder_Price,Bidder_Date,Bidder_Character;
    RadioButton Bidder_RadioButon;

    UserData Buyer,Bidder;
    Button complete;
    ListView Buyerlv,Bidderlv;

    public ImageView imgAndroid,back;
    public Animation anim;

    int picturenum;
    String postnum,Sellid,Postid;

    File f;
    TransferUtility transferUtility;
    File[] fso;
    Bitmap[] bmo;
    AmazonS3 s3;
    private Loading l;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_already);


        l = new Loading(TradeAlready.this);

        Intent intent = getIntent();
        postnum = intent.getStringExtra("Postnum");
        Sellid = intent.getStringExtra("Sellid");
        Postid = intent.getStringExtra("Postid");

        picturenum=0;

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


        ArrayList<UserData> BuyerData;
        ArrayList<UserData> BidderData;

            BuyerData = new ArrayList<>();

            Buyer = new UserData();
            Buyer.Id = Postid;
            Buyer.Name = Postid;
            Buyer.Phone = "구매자 번호";
            Buyer.Profile = null;
            BuyerData.add(Buyer);

            BidderData = new ArrayList<>();

            Bidder = new UserData();
            Bidder.Id = Sellid;
            Bidder.Name = Sellid;
            Bidder.Phone = "판매자 번호";
            Bidder.Profile = null;
            BidderData.add(Bidder);


        Buyerlv = (ListView) findViewById(R.id.Already_Buyer_Listview);
        UserAdapter buyeradapter = new UserAdapter(BuyerData);
        Buyerlv.setAdapter(buyeradapter);

        Bidderlv = (ListView) findViewById(R.id.Already_Bidder_Listview);
        UserAdapter bidderadapter = new UserAdapter(BidderData);
        Bidderlv.setAdapter(bidderadapter);


        Buyerlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(TradeAlready.this, OpponentInfoPopup.class);
                in.putExtra("Id", Buyer.Id);

                startActivity(in);
            }
        });
        Bidderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(TradeAlready.this, OpponentInfoPopup.class);
                in.putExtra("Id", Bidder.Id);
                startActivity(in);
            }
        });

        setListViewHeightBasedOnChildren(Buyerlv);
        setListViewHeightBasedOnChildren(Bidderlv);


        TradeAlready.ListingAsync list = new TradeAlready.ListingAsync();
        list.execute();

        if(Postid.equals(MainActivity.Myname))
        {
            complete.setText("인수확인");
        }
        else
        {
            complete.setText("인계확인");
        }

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(TradeAlready.this,complete.getText(),Toast.LENGTH_LONG).show();

            }
        });





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
            Log.i("picturenumzz",picturenum+"");
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
            }
            publishProgress();
            return null;
        }
    }
    class ListImageAsync extends AsyncTask<File, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenum,1,bmo);//숫자는 글의 사진의 수
            Buyer_Vp.setAdapter(adapter);

           // Loading.Up();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Handler hd = new Handler();
            hd.postDelayed(new TradeAlready.splashhandler(), 5000);



        }

        protected Void doInBackground(File... values) {//user thread


            fso = new File[picturenum];
            bmo = new Bitmap[picturenum];
            for (int i = 0; i < picturenum; i++) {
                fso[i] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/trade" + i);
            }
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:6fb92d56-fccc-4470-af83-af13c271a5b1", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
            s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
            for(int i=0;i<picturenum;i++) {
                transferUtility = new TransferUtility(s3, getApplicationContext());
                TransferObserver observer = transferUtility.download(
                        "samaimage",
                        "post/" + postnum + "/" + "image"+i+".png",
                        fso[i]);
                observer.setTransferListener(new TransferListener() {
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        // update progress bar
                        //progressBar.setProgress(bytesCurrent);
                        Log.i("zzzzzzzzzzzz", "progress changed");

                    }

                    public void onStateChanged(int id, TransferState state) {
                        Bitmap myBitmap[] = new Bitmap[picturenum];
                        for(int i=0;i<picturenum;i++)
                        {
                            myBitmap[i]= BitmapFactory.decodeFile(fso[i].getAbsolutePath());
                        }
                        //Bitmap fin = rotateBitmap(myBitmap, 90);
                        //portraitView.setImageBitmap(myBitmap);
                        bmo = myBitmap;
                        publishProgress();
                    }

                    public void onError(int id, Exception ex) {
                        Log.e("ERROR", ex.getMessage(), ex);
                        Log.i("ERROR", "189");
                        Log.i("ERROR", "image is:");
                        Log.i("ERROR", "iFile is:");
                    }
                });
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
