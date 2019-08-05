package com.example.wlgusdn.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.Month;
import java.time.Year;


public class TradeBidderYet extends AppCompatActivity
{

    static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAA2j7nEs4:APA91bEVEE2yEeR8aiaZ8CQcErx0EGlJeFSiOejjBjtgZGYAJ8k80_r_iEP0_QYCSLoPtoAG1w4cOInqx3bbpthWMIi-PvYEpYEGOvFGENhoz_7D5LqivNwk2QKQh1b8J-xrcpn9_2WK";

    CustomViewPager Buyer_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character;

    FrameLayout[] fl;
    ImageButton[] Cancel;
    ImageView[] iv;
    RadioGroup rg;
    RadioButton rb1,rb2;
    File temp;

    EditText Bidder_Count,Bidder_Price,Bidder_Character;
    LinearLayout Sample,Bid;
    TextView Sample_Year,Sample_Month,Sample_Day,Bidder_Year,Bidder_Month,Bidder_Day,title,hit;
    Button basket;
    String basketbool;

    int count;

    int picturenum;
    String postnum;
    String postid;
    String postToken;
    String postMessageOnOff;
    String postManagerOnOff;
    File[] fs;
    String Login = "login";
    String id;
    File f;
    Bitmap[] img;
    TransferUtility transferUtility;
    File[] fso;
    Bitmap[] bmo;
    AmazonS3 s3;
    int custom;

    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trade_bidder_yet);
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        count=0;
        picturenum=0;

        title = findViewById(R.id.Yet_Bidder_Buy_Title);
        hit = findViewById(R.id.Yet_Bidder_Buy_Hit);

        Buyer_Vp=findViewById(R.id.Yet_Bidder_Buy_ViewPager);
     //   VpAdapter Buyadapter = new VpAdapter(getLayoutInflater(),3,1);//숫자는 글의 사진의 수
      //  Buyer_Vp.setAdapter(Buyadapter);
        Buyer_Count=findViewById(R.id.Yet_Bidder_Buy_Count);
        Buyer_Date=findViewById(R.id.Yet_Bidder_Buy_Date);
        Buyer_Character=findViewById(R.id.Yet_Bidder_Buy_Character);

        basket = findViewById(R.id.Yet_Bidder_Basket);

        iv = new ImageView[4];
        iv[0] = findViewById(R.id.Yet_Bidder_Buy_Pictureiv);
        iv[1] = findViewById(R.id.Yet_Bidder_Buy_Pictureiv1);
        iv[2] = findViewById(R.id.Yet_Bidder_Buy_Pictureiv2);
        iv[3] = findViewById(R.id.Yet_Bidder_Buy_Pictureiv3);

        fl = new FrameLayout[4];
        fl[0]=findViewById(R.id.Yet_Bidder_Buy_Picture);
        fl[1]=findViewById(R.id.Yet_Bidder_Buy_Picture1);
        fl[2]=findViewById(R.id.Yet_Bidder_Buy_Picture2);
        fl[3]=findViewById(R.id.Yet_Bidder_Buy_Picture3);

        Cancel = new ImageButton[4];
        Cancel[0] = findViewById(R.id.Yet_Bidder_Buy_Picture_Delete);
        Cancel[1] = findViewById(R.id.Yet_Bidder_Buy_Picture_Delete1);
        Cancel[2] = findViewById(R.id.Yet_Bidder_Buy_Picture_Delete2);
        Cancel[3] = findViewById(R.id.Yet_Bidder_Buy_Picture_Delete3);
        SharedPreferences sf = getSharedPreferences(Login, 0);
        id = sf.getString("id", "");

        rg = findViewById(R.id.Yet_Bidder_Radio_Group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Yet_Bidder_RadioButton1) {
                    Sample.setVisibility(View.GONE);
                } else if (checkedId == R.id.Yet_Bidder_RadioButton2) {
                    Sample.setVisibility(View.VISIBLE);
                }
            }
        });
        rb1 = findViewById(R.id.Yet_Bidder_RadioButton1);
        rb2 = findViewById(R.id.Yet_Bidder_RadioButton2);

        Sample = findViewById(R.id.Yet_Bidder_Sample_Notice);
        Sample_Year=findViewById(R.id.Yet_Bidder_SampleYearText);
        Sample_Month=findViewById(R.id.Yet_Bidder_SampleMonthText);
        Sample_Day=findViewById(R.id.Yet_Bidder_SampleDayText);


        Bid = findViewById(R.id.Trade_Bid_Layout);
        Bidder_Count = findViewById(R.id.Yet_Bidder_Count);
        Bidder_Price = findViewById(R.id.Yet_Bidder_Price);
        Bidder_Character = findViewById(R.id.Yet_Bidder_Character);

        Bidder_Year = findViewById(R.id.Yet_Bidder_YearText);
        Bidder_Month = findViewById(R.id.Yet_Bidder_MonthText);
        Bidder_Day = findViewById(R.id.Yet_Bidder_DayText);
        img = new Bitmap[4];
        fs = new File[4];
        for(int i = 0 ; i<4;i++)
        {
            fs[i]=  new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/resize"+i);
        }
        Intent intent = getIntent();
        postnum = intent.getStringExtra("postnum");
        ListingAsync list = new TradeBidderYet.ListingAsync();
        list.execute();


    }
    public void GoAttacher(View view)
    {
        Intent in = new Intent(TradeBidderYet.this,PictureAttacher.class);
        in.putExtra("postnum",postnum);
        in.putExtra("PictureCount",picturenum);
        startActivity(in);
    }
    public void DeletePicture(View view) {
        switch (view.getId()) {
            case R.id.Yet_Bidder_Buy_Picture_Delete: {

                count = 0;
                break;
            }
            case R.id.Yet_Bidder_Buy_Picture_Delete1: {

                count = 1;
                break;
            }
            case R.id.Yet_Bidder_Buy_Picture_Delete2: {

                count = 2;
                break;
            }
            case R.id.Yet_Bidder_Buy_Picture_Delete3: {

                count = 3;
                break;
            }
        }
        if(count+1<4)
            fl[count+1].setVisibility(View.GONE);

        iv[count].setImageResource(R.drawable.ic_menu_gallery);
        Cancel[count].setVisibility(View.GONE);
        if(count-1>=0)
            Cancel[count-1].setVisibility(View.VISIBLE);
    }

    public void UpLoadPicture(View view) {
        switch(view.getId())
        {
            case R.id.Yet_Bidder_Buy_Pictureiv:
            {

                count=0;
                break;
            }
            case R.id.Yet_Bidder_Buy_Pictureiv1:
            {

                count=1;
                break;
            }
            case R.id.Yet_Bidder_Buy_Pictureiv2:
            {

                count=2;
                break;
            }
            case R.id.Yet_Bidder_Buy_Pictureiv3:
            {

                count=3;
                break;
            }
        }




                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri photoUri = data.getData();
                    Bitmap correctBmp = null;
                    Cursor cursor = null;

                    try {

                        /*
                         *  Uri 스키마를
                         *  content:/// 에서 file:/// 로  변경한다.
                         */
                        String[] proj = { MediaStore.Images.Media.DATA };

                        assert photoUri != null;
                        cursor = getContentResolver().query(photoUri, proj, null, null, null);

                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        cursor.moveToFirst();

                        f = new File(cursor.getString(column_index));

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    ExifInterface exif = new ExifInterface(f.getPath());

                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;

                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }

                    Matrix mat = new Matrix();
                    mat.postRotate(angle);

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
                    correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                    int height = correctBmp.getHeight();
                    int width = correctBmp.getWidth();

                    Bitmap resized = null;

                    while (height > 1280) {

                        resized = Bitmap.createScaledBitmap(correctBmp, (width * 1280) / height, 1280, true);

                        height = resized.getHeight();

                        width = resized.getWidth();
                    }
                    img[count]=resized;
                    if(count-1>=0)
                        Cancel[count-1].setVisibility(View.GONE);

                    saveBitmapAsFile(resized,Environment.getExternalStorageDirectory().getAbsolutePath() + "/resize"+count);
                    iv[count].setImageBitmap(img[count]);


                    Cancel[count].setVisibility(View.VISIBLE);
                    if(count<4) {
                        fl[++count].setVisibility(View.VISIBLE);
                        iv[count].setImageResource(R.drawable.ic_menu_gallery);

                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void NoticeDate(View view) {
        if (view.getId() == R.id.Yet_Bidder_Date_Button) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Bidder_Year.setText(String.valueOf(year) + "년");
                    Bidder_Month.setText(String.valueOf(month + 1) + "월");
                    Bidder_Day.setText(String.valueOf(dayOfMonth) + "일");

                }
            }
                    , 2019, 0, 1);


            dialog.show();
        } else if (view.getId() == R.id.Yet_Bidder_Sample_Date_Button) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Sample_Year.setText(String.valueOf(year) + "년");
                    Sample_Month.setText(String.valueOf(month + 1) + "월");
                    Sample_Day.setText(String.valueOf(dayOfMonth) + "일");

                }
            }
                    , 2019, 0, 1);


            dialog.show();
        }
    }

    public void Notice_Register(View view)
    {

        //등록
        if(rb1.isChecked())
        {
            custom = 0;

            //custom = 0일때 기성품
            // 1일때 주문제작가능
        }
        else if(rb2.isChecked())
        {            //주문제작 가능
            custom = 1;
        }

        if(postMessageOnOff.equals("On"))
        {
            ChatData chatData = new ChatData();
            chatData.userName = "SAMA";
            chatData.message = "입찰이 등록되었습니다. \n메세지를 주고받아 거래를 진행할 수 있습니다.";
            chatData.time = System.currentTimeMillis();

            mFirebaseDatabase.getReference(postnum).push().setValue(chatData);

            sendPostToFCM();


        }

        PostAsync post = new PostAsync();
        post.execute();








    }
    private void sendPostToFCM() {
        mFirebaseDatabase.getReference("users")
                .child(postid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d("checkkk",dataSnapshot.toString());

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {

                                    // FMC 메시지 생성 start
                                    JSONObject root = new JSONObject();
                                    JSONObject notification = new JSONObject();
                                    notification.put("body", "구매신청한 글에 새로운 입찰이 들어왔습니다.");
                                    notification.put("title", getString(R.string.app_name));
                                    root.put("notification", notification);
                                    root.put("to", postToken);   // FMC 메시지 생성 end

                                    URL Url = new URL(FCM_MESSAGE_URL);
                                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    conn.setDoInput(true);
                                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setRequestProperty("Content-type", "application/json");
                                    OutputStream os = conn.getOutputStream();
                                    os.write(root.toString().getBytes("utf-8"));
                                    os.flush();
                                    conn.getResponseCode();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    public void GoBasket(View view) {
        //장바구니 담기]

        if(MainActivity.sf.contains("id")) {
            if(basket.getText().toString().equals("찜하기")) {
                basket.setText("찜취소");
                new addbasket().execute();
                basketbool="false";
            }else
            {
                basket.setText("찜하기");
                new addbasket().execute();
                basketbool="true";
            }

        }
        else
        {
            Toast.makeText(TradeBidderYet.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
        }


    }

    public void OpenBid(View view)
    {
        if(MainActivity.sf.contains("id")) {
            Bid.setVisibility(View.VISIBLE);

            Bidder_Count.requestFocus();

        }
        else
        {
            Toast.makeText(TradeBidderYet.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
        }
    }
    class ListingAsync extends AsyncTask<Void, String, Void> {
        String count;
        String day;
        String content;
        String tit;
        String hits;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Buyer_Count.setText("수량 : "+count);
            Buyer_Date.setText("희망배송일 : " +day);
            Buyer_Character.setText("세부사항 : "+content);
            title.setText("제목 : "+tit);
            hit.setText("조회수 : "+hits);
            if(basketbool.equals("true"))
                basket.setText("찜하기");
            else
                basket.setText("찜취소");
            Log.i("picturenumzz",picturenum+"");
            mFirebaseDatabase.getReference("users").child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d("checkkk",dataSnapshot.toString());
                    postToken = dataSnapshot.child("fcmToken").getValue(true).toString();
                    postManagerOnOff = dataSnapshot.child("ManagerOnOff").getValue(true).toString();
                    postMessageOnOff = dataSnapshot.child("MessageOnOff").getValue(true).toString();

                    Log.d("checkkkkk",postManagerOnOff + postMessageOnOff);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            TradeBidderYet.ListImageAsync listimage = new TradeBidderYet.ListImageAsync();
            listimage.execute();
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/getpost.jsp";
            Document xml = null;
            String param = "?postnum="+postnum+"&id="+id;
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
                postid = e.select("id").text().toString();
                tit=e.select("title").text().toString();
                hits =e.select("hit").text().toString();
                if(e.select("preference").text().toString().equals("0"))
                    basketbool="true";
                else
                    basketbool="false";

            }
            publishProgress();
            return null;
        }
    }
    class ListImageAsync extends AsyncTask<File, Void, Void> {
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenum,1,bmo);//숫자는 글의 사진의 수
            Buyer_Vp.setAdapter(adapter);
        }



        protected Void doInBackground(File... values) {//user thread
//            fso = new File[picturenum];
            bmo = new Bitmap[picturenum];
            for(int i=0;i<picturenum;i++)
            {
                bmo[i]=loadBitmap("http://samaimage.s3.ap-northeast-2.amazonaws.com/post/" + postnum + "/" + "image"+i+".png");
            }
            publishProgress();
            return null;
        }
    }
    class BidImageAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("ccccc","온ㄷ아");

            Bid.setVisibility(View.GONE);
            Bidder_Count.setText("");
            Bidder_Price.setText("");
            Bidder_Character.setText("");
            Bidder_Year.setText("");
            Bidder_Day.setText("");
            Bidder_Month.setText("");
        }

        protected Void doInBackground(Void... voids) {//user thread
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:6fb92d56-fccc-4470-af83-af13c271a5b1", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
            s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");


            transferUtility = new TransferUtility(s3, getApplicationContext());

            for(int i=0;i<count;i++)
            {
                if(fs[i]!=null) {
                    TransferObserver observer = transferUtility.upload(
                            "samaimage",
                            "auction/"+postnum+"/"+id+"/image"+i+".png",
                            fs[i]);
                }
            }

            publishProgress();
            return null;

        }
    }
    class PostAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            if (values[0].equals("글쓰기 성공")) {

                BidImageAsync data = new BidImageAsync();
                data.execute();
            } else {
                Toast.makeText(TradeBidderYet.this, "게시글 등록이 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://52.79.255.160:8080/auctionpost.jsp";
            String param = "?postid=" + postid + "&postnum=" + postnum + "&sellerid=" + id + "&price=" + Bidder_Price.getText().toString()
                    + "&contents=" + Bidder_Character.getText().toString() + "&quantity=" + Bidder_Count.getText().toString() + "&s_date=" +Bidder_Year.getText().toString()+" "+ Bidder_Month.getText().toString() + " " + Bidder_Day.getText().toString()
                    + "&custom=" + custom+"&picture="+count+"&ss_date="+Sample_Year.getText().toString()+" "+Sample_Month.getText().toString()+" "+Sample_Day.getText().toString();
            Document xml = null;



            String u = url + param;
            Log.i("zzzzzzzzzzzz", u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {
                if (e.select("result").text().equals("true"))
                    publishProgress("글쓰기 성공");
                else
                    publishProgress("글쓰기 실패");
            }
            return null;
        }
    }
    private void saveBitmapAsFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);
        OutputStream os = null;

        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class addbasket extends AsyncTask<Void,String,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://52.79.255.160:8080/addbasket.jsp";
            String params = "?id="+id+"&postnum="+postnum+"&bool="+basketbool;
            String u = url+params;
            Log.i("addbasketadd",u);
            Document xml = null;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements result = xml.select("data");
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
