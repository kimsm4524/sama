package com.example.wlgusdn.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.io.InputStream;

import static android.content.Intent.ACTION_GET_CONTENT;

public class BidNotice extends AppCompatActivity
{
    LinearLayout Sample;
    ImageView[] iv;
    RadioButton rb1,rb2;
    TextView Year,Month,Day;
    TextView SampleYear,SampleMonth,SampleDay;
    EditText Count, Price, Character;
    RadioGroup rg;
    int PicCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bid_notice);

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
        //기타 사항
        PicCount =0;



    }
    public void UpLoadPicture(View view) {
        iv[PicCount].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
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
}
