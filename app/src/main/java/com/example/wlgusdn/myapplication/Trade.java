package com.example.wlgusdn.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.Intent.ACTION_GET_CONTENT;

public class Trade extends AppCompatActivity
{
    String postnum;
    private ListView Bidderlv = null;
    TextView BidCount,Date,Condition;
    LinearLayout Sample,Already,Yet;
    LinearLayout Bid;
    ImageView[] iv;
    RadioButton rb1,rb2;
    TextView Year,Month,Day;
    TextView SampleYear,SampleMonth,SampleDay;
    EditText Count, Price, Character;
    RadioGroup rg;
    int PicCount;
    ViewPager pager,Bid_Info_pager;
    TextView Bid_Info_RadioText,Bid_Info_SampleYear,Bid_Info_SampleMonth,Bid_Info_SampleDay;
    RadioButton Bid_Info_RadioButton;
    LinearLayout Bid_Info_SampleLayout,Bid_Info_Layout;
    TextView Bid_Info_Count,Bid_Info_Price,Bid_Info_Character,Bid_Info_Year,Bid_Info_Month,Bid_Info_Day;
    LinearLayout Buyer;
    FrameLayout[] fl;
    ImageButton[] Cancel;
    LinearLayout Bottom_Bid,Bottom_buy_Yet,Bottom_buy_Already;
    int count;
    int picturenum;
    File[] fs;
    File f;
    Bitmap[] img;
    AmazonS3 s3;
    TransferUtility transferUtility;
    File[] fso;
    Bitmap[] bmo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        Bidderlv = (ListView)findViewById(R.id.bidder_ListView);
        count=0;
        BidCount = findViewById(R.id.Trade_Count);//개수
        Date= findViewById(R.id.Trade_Date);
        Condition=findViewById(R.id.Trade_Condition);
        pager=findViewById(R.id.Trade_ViewPager);
        Bid_Info_Layout=findViewById(R.id.Trade_Bid_Info_Layout);
        Already=findViewById(R.id.Trade_Step_Already);
        Yet = findViewById(R.id.Trade_Step_Yet);
        Buyer=findViewById(R.id.Trade_Buyer_Layout);
        Bid = findViewById(R.id.Trade_Bid_Layout);
        Bottom_Bid=findViewById(R.id.Trade_Bottom_Bidder_Layout);
        Intent intent = getIntent();
        postnum = intent.getStringExtra("postnum");
        picturenum=0;
        fs=new File[4];
        img = new Bitmap[4];
        fl = new FrameLayout[4];
        Cancel = new ImageButton[4];
        fl[0]=findViewById(R.id.Bid_Picture);
        fl[1]=findViewById(R.id.Bid_Picture1);
        fl[2]=findViewById(R.id.Bid_Picture2);
        fl[3]=findViewById(R.id.Bid_Picture3);

        Cancel[0] = findViewById(R.id.Bid_Picture_Delete);
        Cancel[1] = findViewById(R.id.Bid_Picture_Delete1);
        Cancel[2] = findViewById(R.id.Bid_Picture_Delete2);
        Cancel[3] = findViewById(R.id.Bid_Picture_Delete3);
        ListingAsync list = new ListingAsync();
        list.execute();
        //if(거래가 진행중일 때)
        {

            Bid_Info_pager = findViewById(R.id.Bid_Info_ViewPager);
            VpAdapter Bidadapter = new VpAdapter(getLayoutInflater(),3,1);//숫자는 글의 사진의 수
            Bid_Info_pager.setAdapter(Bidadapter);

            Bid_Info_RadioText=findViewById(R.id.Bid_Info_RadioText);
            Bid_Info_RadioButton=findViewById(R.id.Bid_Info_RadioButton1);
            Bid_Info_SampleLayout=findViewById(R.id.Sample_Bid_Info_Notice);
            Bid_Info_SampleYear=findViewById(R.id.SampleBidInfo_YearText);
            Bid_Info_SampleMonth=findViewById(R.id.SampleBidInfo_MonthText);
            Bid_Info_SampleDay=findViewById(R.id.SampleBidInfo_DayText);
            Bid_Info_Count = findViewById(R.id.BIdInfo_Countitem);
            Bid_Info_Price = findViewById(R.id.BIdInfo_Priceitem);
            Bid_Info_Character=findViewById(R.id.BId_Item_Character);
            Bid_Info_Year=findViewById(R.id.BidInfo_YearText);
            Bid_Info_Month=findViewById(R.id.BidInfo_MonthText);
            Bid_Info_Day=findViewById(R.id.BidInfo_DayText);
            // Bid_Info_Layout.setVisibility(View.VISIBLE);
            // Already.setVisibility(View.VISIBLE);

            //if(주문제작 or 기성품)
            //Bid_Info_RadioText.setText("");


        }






        iv = new ImageView[4];
        iv[0] = findViewById(R.id.Bid_Pictureiv);
        iv[1] = findViewById(R.id.Bid_Pictureiv1);
        iv[2] = findViewById(R.id.Bid_Pictureiv2);
        iv[3] = findViewById(R.id.Bid_Pictureiv3);

        //사진 올리는 4개 이미지뷰
        //사진 올리는 건 동시에 여러개를 등록하지 못해서 하나를 올리고 그 다음에 또 올리고 ...

        Sample = findViewById(R.id.Sample_Bid_Notice);

        rg = findViewById(R.id.Bid_Radio_Group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.BidRadioButton1) {
                    Sample.setVisibility(View.GONE);
                } else if (checkedId == R.id.BidRadioButton2) {
                    Sample.setVisibility(View.VISIBLE);
                }
            }
        });
        rb1 = findViewById(R.id.BidRadioButton1);//기성품
        rb2 = findViewById(R.id.BidRadioButton2);//주문제작
        //주문제작이 가능한지 라디오버튼
        Year = findViewById(R.id.BidYearText);
        Month = findViewById(R.id.BidMonthText);
        Day = findViewById(R.id.BidDayText);
        SampleYear = findViewById(R.id.SampleBidYearText);
        SampleMonth = findViewById(R.id.SampleBidMonthText);
        SampleDay = findViewById(R.id.SampleBidDayText);

        Count = findViewById(R.id.BIdCountitem);
        //물건 수량
        Price = findViewById(R.id.BIdPriceitem);
        //물건 개당 가격
        Character = findViewById(R.id.BId_Item_Character);
        Character.setHint(R.string.d);
        //기타 사항
        PicCount = 0;




        //생성자에 글의 사진 배열을 넘겨줘야함



        /*
        if(사용자가 이미 입찰한 글일 때)
        {
            입찰창에 자신이 입찰한 내용 보여주기
        }

        */
        String[] biddercompanyname = {"삼성","LG","카카오","현대"};
        String[] biddertradenum = {"10","1","99","1004"};
        String[] bidderprice = {"100,000","200,000","1,000,000","50,000"};
        String[] biddergrade = {"4.5","5.0","1.2","3.3"};


        //if(내가 올린 판매글일 경우)
        {
            ArrayList<BidderData> BidderData = new ArrayList<>();
            for (int i = 0; i < 4; i++) {

                BidderData bi = new BidderData();
                bi.BidderCompanyName = biddercompanyname[i];
                bi.BidderTradeCount = biddertradenum[i];
                bi.BidderPrice = bidderprice[i];
                bi.BidderGrade = biddergrade[i];
                BidderData.add(bi);

            }

            Bidderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                    Intent in = new Intent(Trade.this, TradeBidderYet.class);
                    startActivity(in);
                }
            });


            BidderListAdapter bidderadapter = new BidderListAdapter(BidderData);
            Bidderlv.setAdapter(bidderadapter);


            setListViewHeightBasedOnChildren(Bidderlv);
            //Yet.setVisibility(View.VISIBLE);
        }



    }
    public void UpLoadPicture(View view) {

        switch(view.getId())
        {
            case R.id.Pictureiv:
            {

                count=0;
                break;
            }
            case R.id.Pictureiv1:
            {

                count=1;
                break;
            }
            case R.id.Pictureiv2:
            {

                count=2;
                break;
            }
            case R.id.Pictureiv3:
            {

                count=3;
                break;
            }
        }

        Toast.makeText(Trade.this,"count = "+count,Toast.LENGTH_SHORT).show();
        iv[count].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

    }
    /*
    public void UpLoadPicture(View view) {
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"다중선택은 '사진'을 선택하세요."), 1);
            }
        });

    }*/

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

                    img[count]=correctBmp;
                    fs[count]=f;

                    if(count-1>=0)
                        Cancel[count-1].setVisibility(View.GONE);



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
    public void NoticeDate(View view)
    {

        if(view.getId()==R.id.button5) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Year.setText(String.valueOf(year) + "년");
                    Month.setText(String.valueOf(month + 1) + "월");
                    Day.setText(String.valueOf(dayOfMonth) + "일");

                }
            }
                    , 2019, 0, 1);


            dialog.show();
        }
        else if(view.getId()==R.id.Samplebutton5)
        {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SampleYear.setText(String.valueOf(year) + "년");
                    SampleMonth.setText(String.valueOf(month + 1) + "월");
                    SampleDay.setText(String.valueOf(dayOfMonth) + "일");

                }
            }
                    , 2019, 0, 1);


            dialog.show();
        }


    }
    public void Notice_Register(View view)
    {
        //입찰하기

        if(rb1.isChecked())
        {
            //기성품
        }
        else if(rb2.isChecked())
        {
            //주문제작 가능



        }


    }
    /***************************************************
     *
     *
     if(이 글의 주인이면)
     {
     if(거래단계 == 입찰진행중)
     R.id.Trade_Step_Already 의 SetVisible => gone

     if(거래단계 == 거래진행중)
     R.id.Trade_Step_Yet 의 SetVisible => gone
     }

     if(이 글의 주인이 아니면)
     R.id.Trade_Step_Already,R.id.Trade_Step_Yet 둘다 -> gone
     */

    //여기서 데이터 생성
    //ex)


    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
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





    public void Comment_Review(View view)
    {
        Intent in = new Intent(Trade.this, ReviewPopup.class);
        startActivity(in);
    }

    @SuppressLint("NewApi")
    public void OpenBid(View view)
    {
        //if(판매자일 때)

        Bid.setVisibility(View.VISIBLE);

        Count.requestFocus();

        //else '열수 없다' 팝업 띄우기?

    }

    public void GoAttacher(View view)
    {
        Intent in = new Intent(Trade.this,PictureAttacher.class);
        in.putExtra("PictureCount",3);
        startActivity(in);
    }

    public void GoBasket(View view)
    {
        //장바구니에 담기
    }

    public void Step(View view)
    {

        switch(view.getId())
        {

            case R.id.button2:
            {
                Yet.setVisibility(View.VISIBLE);

                break;
            }



            case R.id.button7:
            {
                Already.setVisibility(View.VISIBLE);
                Buyer.setVisibility(View.VISIBLE);
                Bid_Info_Layout.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.button11:
            {
                Bottom_Bid.setVisibility(View.VISIBLE);

                break;
            }

        }

    }

    public void DeletePicture(View view)
    {
        switch (view.getId()) {
            case R.id.Bid_Picture_Delete: {

                count = 0;
                break;
            }
            case R.id.Bid_Picture_Delete1: {

                count = 1;
                break;
            }
            case R.id.Bid_Picture_Delete2: {

                count = 2;
                break;
            }
            case R.id.Bid_Picture_Delete3: {

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

    public void GoPhoneNumber(View view)
    {


        startActivity(new Intent("android.intent.action.DIAL",Uri.parse("tel:01000000000")));


    }

    class ListingAsync extends AsyncTask<Void, String, Void> {
        String count;
        String day;
        String content;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            BidCount.setText(count);
            Date.setText(day);
            Condition.setText(content);
            Log.i("picturenumzz",picturenum+"");
            ListImageAsync listimage = new ListImageAsync();
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
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenum,1,bmo);//숫자는 글의 사진의 수
            pager.setAdapter(adapter);
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
}