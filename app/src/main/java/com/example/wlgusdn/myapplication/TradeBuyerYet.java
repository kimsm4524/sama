package com.example.wlgusdn.myapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TradeBuyerYet extends AppCompatActivity
{
    ViewPager Buyer_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character,Buyer_Address,Buyer_title,Buyer_hit,Buyer_Address1;
    TextView MoYear,MoMonth,MoDay;
    ListView Bidderlv = null;
    String Buyer_Date1;

    EditText Buyer_Count1,Buyer_Character1,Buyer_title1;
    Button button;
    LinearLayout modify;
    int picturenum;
    String postnum;
    String postid;
    File f;
    TransferUtility transferUtility;
    Bitmap[] bmo;
    AmazonS3 s3;
    int sellercount=0;
    File[] fseller;
    Bitmap[] bseller;
    ArrayList<BidderData> BData;
    ArrayList<BidderData> BData1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_buyer_yet);



        picturenum=0;

        Bidderlv = findViewById(R.id.Yet_Bidder_ListView);

        Buyer_Vp = findViewById(R.id.Yet_Buyer_ViewPager);
       // VpAdapter Buyadapter = new VpAdapter(getLayoutInflater(),3,1);//숫자는 글의 사진의 수
       // Buyer_Vp.setAdapter(Buyadapter);

        Buyer_hit = findViewById(R.id.Yet_Buyer_Hit);

        Buyer_Count = findViewById(R.id.Yet_Buyer_Count);
        Buyer_Address = findViewById(R.id.Yet_Buyer_Address);
        Buyer_Date = findViewById(R.id.Yet_Buyer_Date);
        Buyer_title = findViewById(R.id.Yet_Buyer_Title);
        Buyer_Character = findViewById(R.id.Yet_Buyer_Character);

        Buyer_Count1 = findViewById(R.id.Yet_Buyer_Count1);
        Buyer_Address1 = findViewById(R.id.Yet_Buyer_Address1);
        Buyer_title1 = findViewById(R.id.Yet_Buyer_Title1);
        Buyer_Character1 = findViewById(R.id.Yet_Buyer_Character1);

        modify = findViewById(R.id.Buyer_Linear_Modify);
        modify.setVisibility(View.GONE);

        MoDay = findViewById(R.id.ModifyDayText);
        MoMonth = findViewById(R.id.ModifyMonthText);
        MoYear = findViewById(R.id.ModifyYearText);

        button = findViewById(R.id.Buyer_Button_Modify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenMap();

            }
        });

        BData = new ArrayList<BidderData>();
        BData1 = new ArrayList<BidderData>();
        Intent intent = getIntent();

        postnum = intent.getStringExtra("postnum");

        ListingAsync list = new ListingAsync();
        list.execute();
        ListingSeller listsell = new ListingSeller();
        listsell.execute();


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
    public void NoticeDate(View view)
    {


        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                MoYear.setText(String.valueOf(year)+"년");
                MoMonth.setText(String.valueOf(month+1)+"월");
                MoDay.setText(String.valueOf(dayOfMonth)+"일");
            }
        }
                , 2019, 0, 1);



        dialog.show();


    }
        public void Modify(View view)
        {

            modify.setVisibility(View.VISIBLE);





        }


    public void Delete(View view)
    {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(TradeBuyerYet.this);
        // 다이얼로그 메세지
        alertdialog.setMessage("구매글을 삭제하시겠습니까?");

        // 확인버튼
        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                Toast.makeText(TradeBuyerYet.this, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                //구매글, 글에 대한 입찰글 삭제

                finish();

            }
        });

        // 취소버튼
        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

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



        Toast.makeText(TradeBuyerYet.this,"구매글 삭제하기 Delete()",Toast.LENGTH_SHORT).show();
    }

    public void GoAttacher(View view) {
        Intent in = new Intent(TradeBuyerYet.this,PictureAttacher.class);
        in.putExtra("postnum",postnum);
        in.putExtra("PictureCount",picturenum);
        startActivity(in);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {

                try {
                   Buyer_Address1.setText(data.getStringExtra("Adress"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void OpenMap()
    {
            Intent in = new Intent(TradeBuyerYet.this, DaumWebView.class);
            startActivityForResult(in, 4);



    }

    public void Modify_Complete(View view)
    {

        if(Buyer_title.getText().equals(Buyer_title1.getText()))
        {
            Buyer_title.getText();
        }
        else
        {

            Buyer_title1.getText();
        }

        if(Buyer_Count.getText().equals(Buyer_Count1.getText()))
        {
            Buyer_Count.getText();
        }
        else
        {

            Buyer_Count1.getText();
        }


        if(Buyer_Address.getText().equals(Buyer_Address1.getText()))
        {
            Buyer_Address.getText();
        }
        else
        {

            Buyer_Address1.getText();
        }

        if(Buyer_Character.getText().equals(Buyer_Character1.getText()))
        {
            Buyer_Character.getText();
        }
        else
        {

            Buyer_Character1.getText();
        }

        MoYear.getText();
        MoMonth.getText();
        MoDay.getText();

        Toast.makeText(TradeBuyerYet.this,"수정완료",Toast.LENGTH_SHORT).show();

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
            Buyer_title.setText("제목 : "+tit);
            Buyer_hit.setText("조회수 : "+hits);
            Log.i("picturenumzz",picturenum+"");
            TradeBuyerYet.ListImageAsync listimage = new TradeBuyerYet.ListImageAsync();
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
                tit=e.select("title").text().toString();
                hits =e.select("hit").text().toString();;
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
            bmo = new Bitmap[picturenum];
            for(int i=0;i<picturenum;i++)
            {
                bmo[i]=loadBitmap("http://samaimage.s3.ap-northeast-2.amazonaws.com/post/" + postnum + "/" + "image"+i+".png");
            }
            publishProgress();
            return null;
        }
    }
    class ListingSeller extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            new ListSellerImage().execute();

            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/sellerlist.jsp?postnum="+postnum;
            Document xml = null;
            sellercount=0;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            BData.clear();
            for (Element e : result) {
                BidderData bd = new BidderData();
                bd.BidderCompanyName = e.select("name").text().toString();
                bd.BidderTradeCount = e.select("tradecount").text().toString();
                bd.BidderPrice = e.select("price").text().toString();
                bd.BidderGrade = e.select("grade").text().toString();
                bd.id = e.select("id").text().toString();
                postid = bd.id;
                BData.add(bd);
            }
            sellercount=result.size();
            publishProgress();
            return null;
        }
    }
    class ListSellerImage extends AsyncTask<File, Void, Void> {
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            BData1.clear();
            Log.i("datanumzzz", BData1.size() + "");
            for (int i = 0; i < sellercount; i++) {
                BidderData temp = (BidderData) BData.get(i);
                temp.bmp = bseller[i];
                BData1.add(temp);
            }
            Bidderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent in = new Intent(TradeBuyerYet.this, TradeSelect.class);
                    in.putExtra("Postid",MainActivity.Myname);
                    Log.d("chcheck11",BData1.get(position).id);
                    in.putExtra("Sellid",BData1.get(position).id);
                    in.putExtra("postnum",postnum);
                    startActivity(in);
                }
            });


            BidderListAdapter bidderadapter = new BidderListAdapter(BData1);
            Bidderlv.setAdapter(bidderadapter);


            setListViewHeightBasedOnChildren(Bidderlv);
            //Yet.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("checkzzz", "post");
        }

        protected Void doInBackground(File... values) {//user thread

            fseller = new File[sellercount];
            bseller = new Bitmap[sellercount];
            for (int i = 0; i < sellercount; i++) {
                fseller[i] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sellerimage" + i);
            }
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:6fb92d56-fccc-4470-af83-af13c271a5b1", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );


            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
            s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
            for (int i = 0; i < sellercount; i++) {
                BidderData temp = (BidderData) BData.get(i);
                transferUtility = new TransferUtility(s3, getApplicationContext());
                TransferObserver observer = transferUtility.download(
                        "samaimage",
                        "user/"+temp.id+"/"+"image.png",
                        fseller[i]);
                observer.setTransferListener(new TransferListener() {
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        // update progress bar
                        //progressBar.setProgress(bytesCurrent);
                        Log.i("zzzzzzzzzzzz", "progress changed");

                    }


                    public void onStateChanged(int id, TransferState state) {
                        Bitmap myBitmap[] = new Bitmap[sellercount];
                        for (int i = 0; i < sellercount; i++) {
                            myBitmap[i] = BitmapFactory.decodeFile(fseller[i].getAbsolutePath());
                        }
                        //Bitmap fin = rotateBitmap(myBitmap, 90);
                        //portraitView.setImageBitmap(myBitmap);
                        bseller = myBitmap;
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
