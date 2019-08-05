package com.example.wlgusdn.myapplication;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.content.Intent.ACTION_GET_CONTENT;

public class BuyNotice extends AppCompatActivity {

    ImageView[] iv ;
    RadioButton r1, r2, r3, r4, r5, r6;
    RadioGroup rg1,rg2;
    EditText Etc;
    int PicCount;
    String Pickeditem;
    int Countitem; //등록버튼 눌렀을 때 값을 넣어줘야함
    String Characteritem;
    TextView Year,Month,Day;

    FrameLayout[] fl;
    ImageButton[] Cancel;

    //s3
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;
    File[] fs;
    File f;
    Bitmap[] img;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buy_notice);

        iv = new ImageView[4];
        fl=new FrameLayout[4];
        Cancel = new ImageButton[4];
        img = new Bitmap[4];
        count=0;
        iv[0] = findViewById(R.id.Pictureiv);
        iv[1] = findViewById(R.id.Pictureiv1);
        iv[2] = findViewById(R.id.Pictureiv2);
        iv[3] = findViewById(R.id.Pictureiv3);
        rg1 = findViewById(R.id.Buy_Notice_RadioGroup1);
        rg2 = findViewById(R.id.Buy_Notice_RadioGroup2);
        r1 = findViewById(R.id.radioButton1);
        r2 = findViewById(R.id.radioButton2);
        r3 = findViewById(R.id.radioButton3);
        r4 = findViewById(R.id.radioButton4);
        r5 = findViewById(R.id.radioButton5);
        r6 = findViewById(R.id.radioButton6);
        Etc = findViewById(R.id.Etc);
        Year = findViewById(R.id.YearText);
        Month = findViewById(R.id.MonthText);
        Day = findViewById(R.id.DayText);

        fl[0]=findViewById(R.id.Picture);
        fl[1]=findViewById(R.id.Picture1);
        fl[2]=findViewById(R.id.Picture2);
        fl[3]=findViewById(R.id.Picture3);

        Cancel[0] = findViewById(R.id.Picture_Delete);
        Cancel[1] = findViewById(R.id.Picture_Delete1);
        Cancel[2] = findViewById(R.id.Picture_Delete2);
        Cancel[3] = findViewById(R.id.Picture_Delete3);

        fs=new File[4];

        PicCount=0;

    }

    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try
        {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) { return cursor.getString(columnIndex);
            }
        }
        finally
        {
            cursor.close();
        }
        return null;
    }


    //    public String getPath(Uri uri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor c = getContentResolver().query(uri, proj, null, null, null);
//        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        c.moveToFirst();
//        String path = c.getString(index);
//        Log.i("zzzzzzzzzzzzzz",path+"");
//        return path;
//    }
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

        Toast.makeText(BuyNotice.this,"count = "+count,Toast.LENGTH_SHORT).show();
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

    public void SelectCategory(View view) {
        int id = view.getId();

        rg1.setVisibility(View.VISIBLE);
        rg2.setVisibility(View.VISIBLE);
        switch (id) {
            case R.id.Furniture: {
                r1.setText("의자");
                r2.setText("테이블");
                r3.setText("소파");
                r4.setText("침대");
                r5.setText("옷장");
                r6.setText("기타");
                break;
            }
            case R.id.Tableware: {
                r1.setText("접시");
                r2.setText("그릇");
                r3.setText("찻잔");
                r4.setText("컵/텀블러");
                r5.setText("수저/포크");
                r6.setText("기타");
                break;
            }
            default: {
                rg1.setVisibility(View.GONE);
                rg2.setVisibility(View.GONE);
                break;
            }


        }
        rg1.clearCheck();
        rg2.clearCheck();
        Etc.setVisibility(View.GONE);

    }

    public void Select(View view)
    {
        int id = view.getId();
        RadioButton rb=findViewById(id);

        Etc.setVisibility(View.GONE);

        switch(id)
        {
            case R.id.radioButton1:
            {
                rg1.clearCheck();
                rg2.clearCheck();
                rg1.check(id);
                break;
            }
            case R.id.radioButton2:
            {
                rg1.clearCheck();
                rg2.clearCheck();
                rg1.check(id);
                break;
            }
            case R.id.radioButton3:
            {
                rg1.clearCheck();
                rg2.clearCheck();
                rg1.check(id);
                break;
            }
            case R.id.radioButton4:
            {
                rg1.clearCheck();
                rg2.clearCheck();
                rg2.check(id);
                break;
            }
            case R.id.radioButton5:
            {
                rg1.clearCheck();
                rg2.clearCheck();
                rg2.check(id);
                break;
            }
            case R.id.radioButton6:
            {
                rg1.clearCheck();
                rg2.clearCheck();
                rg2.check(id);
                Etc.setVisibility(View.VISIBLE);
                //**************************
                // 적어놓고 등록버튼을 누를때 Pickeditem에 넣어줘야함.*
                // ***************/
                break;
            }

        }


        Pickeditem = rb.getText().toString();
        Toast.makeText(this,Pickeditem,Toast.LENGTH_SHORT).show();
    }

    public void NoticeDate(View view)
    {


        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                Year.setText(String.valueOf(year)+"년");
                Month.setText(String.valueOf(month+1)+"월");
                Day.setText(String.valueOf(dayOfMonth)+"일");

            }
        }
                , 2019, 0, 1);



        dialog.show();



    }

    public void Notice_Register(View view)
    {
        //게시글 등록

        DataAsync dataa = new DataAsync();
        dataa.execute();

        finish();


    }

    public void DeletePicture(View view) {
        switch (view.getId()) {
            case R.id.Picture_Delete: {

                count = 0;
                break;
            }
            case R.id.Picture_Delete1: {

                count = 1;
                break;
            }
            case R.id.Picture_Delete2: {

                count = 2;
                break;
            }
            case R.id.Picture_Delete3: {

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

    class DataAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
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

            for(int i=0;i<4;i++)
            {
                if(fs[i]!=null) {
                    TransferObserver observer = transferUtility.upload(
                            "samaimage",
                            "/abcc/" + "image"+i,
                            fs[i]);
                }
            }
            return null;
        }
    }
}