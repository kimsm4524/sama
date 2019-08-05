package com.example.wlgusdn.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.Intent.ACTION_GET_CONTENT;

public class Trade extends AppCompatActivity
{
    private ListView Bidderlv = null;
    TextView BidCount,Date,Condition;
    LinearLayout Sample;
    LinearLayout Bid;
    ImageView[] iv;
    RadioButton rb1,rb2;
    TextView Year,Month,Day;
    TextView SampleYear,SampleMonth,SampleDay;
    EditText Count, Price, Character;
    RadioGroup rg;
    int PicCount;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        Bidderlv = (ListView)findViewById(R.id.bidder_ListView);

        BidCount = findViewById(R.id.Trade_Count);
        Date= findViewById(R.id.Trade_Date);
        Condition=findViewById(R.id.Trade_Condition);


        Bid = findViewById(R.id.Trade_Bid_Layout);


        iv = new ImageView[4];
        iv[0] = findViewById(R.id.BidPictureiv);
        iv[1] = findViewById(R.id.BidPictureiv1);
        iv[2] = findViewById(R.id.BidPictureiv2);
        iv[3] = findViewById(R.id.BidPictureiv3);
        //사진 올리는 4개 이미지뷰
        //사진 올리는 건 동시에 여러개를 등록하지 못해서 하나를 올리고 그 다음에 또 올리고 ...

        Sample = findViewById(R.id.Sample_Bid_Notice);

        rg = findViewById(R.id.Bid_Radio_Group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.BidRadioButton1)
                {
                    Sample.setVisibility(View.GONE);
                }
                else if(checkedId==R.id.BidRadioButton2)
                {
                    Sample.setVisibility(View.VISIBLE);
                }
            }
        });
        rb1 = findViewById(R.id.BidRadioButton1);//기성품
        rb2=findViewById(R.id.BidRadioButton2);//주문제작
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
        PicCount =0;

        pager=findViewById(R.id.Trade_ViewPager);
        VpAdapter adapter = new VpAdapter(getLayoutInflater(),3,1);//숫자는 글의 사진의 수
        pager.setAdapter(adapter);


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



        ArrayList<BidderData> BidderData = new ArrayList<>();
        for(int i=0;i<4;i++)
        {

            BidderData bi = new BidderData();
            bi.BidderCompanyName = biddercompanyname[i];
            bi.BidderTradeCount = biddertradenum[i];
            bi.BidderPrice = bidderprice[i];
            bi.BidderGrade = biddergrade[i];
            BidderData.add(bi);

        }

        Bidderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(Trade.this,TradeBid.class);
                startActivity(in);
            }
        });


        BidderListAdapter bidderadapter = new BidderListAdapter(BidderData) ;
        Bidderlv.setAdapter(bidderadapter);


        setListViewHeightBasedOnChildren(Bidderlv);





    }
    public void UpLoadPicture(View view) {

            iv[PicCount].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(ACTION_GET_CONTENT);
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

                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    iv[PicCount++].setImageBitmap(img);
                    iv[PicCount].setImageResource(R.drawable.ic_menu_gallery);

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
    public void GoOpponentPopup(View view)
    {
        Intent in = new Intent(Trade.this,OpponentInfoPopup.class);
        startActivity(in);

    }

    public void OpenBid(View view)
    {
        //if(판매자일 때)

        Bid.setVisibility(View.VISIBLE);

        //else 팝업 띄우기?

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
}
