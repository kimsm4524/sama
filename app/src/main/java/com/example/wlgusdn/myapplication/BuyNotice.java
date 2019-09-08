package com.example.wlgusdn.myapplication;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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
import android.widget.LinearLayout;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import static android.content.Intent.ACTION_GET_CONTENT;

public class BuyNotice extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    ImageView[] iv ;
    RadioButton r1, r2, r3, r4, r5, r6,MyAddress,NewAddress;
    RadioGroup rg1,rg2,rg3;
    String categori;
    EditText Etc;
    int PicCount;
    String Pickeditem;
    TextView Year,Month,Day;
    TextView Category;
    TextView Address;
    EditText Countitem,Title,ExtraAddress;
    LinearLayout AddressLayout;
    FrameLayout[] fl;
    ImageButton[] Cancel;
    EditText Contents;

    String Login = "login";
    String id;
    String postnum;

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
        SharedPreferences sf = getSharedPreferences(Login,0);
        id = sf.getString("id","");

        iv = new ImageView[4];
        fl=new FrameLayout[4];
        Cancel = new ImageButton[4];
        img = new Bitmap[4];
        count=0;
        iv[0] = findViewById(R.id.Pictureiv);
        iv[1] = findViewById(R.id.Pictureiv1);
        iv[2] = findViewById(R.id.Pictureiv2);
        iv[3] = findViewById(R.id.Pictureiv3);
        rg3 = findViewById(R.id.Buy_AddressGroup);
        MyAddress=findViewById(R.id.Buy_MyAddress);
        NewAddress = findViewById(R.id.Buy_NewAddress);
        ExtraAddress = findViewById(R.id.Buy_ExtraAddress);
        Year = findViewById(R.id.YearText);
        Month = findViewById(R.id.MonthText);
        Day = findViewById(R.id.DayText);
        Contents = findViewById(R.id.Item_Character);
        Address = findViewById(R.id.Buy_Address);
        Countitem = findViewById(R.id.Countitem);
        Title = findViewById(R.id.Buy_Title);
        fl[0]=findViewById(R.id.Picture);
        fl[1]=findViewById(R.id.Picture1);
        fl[2]=findViewById(R.id.Picture2);
        fl[3]=findViewById(R.id.Picture3);
        Category = findViewById(R.id.Buy_Textview_Category);

        Cancel[0] = findViewById(R.id.Picture_Delete);
        Cancel[1] = findViewById(R.id.Picture_Delete1);
        Cancel[2] = findViewById(R.id.Picture_Delete2);
        Cancel[3] = findViewById(R.id.Picture_Delete3);

        fs=new File[4];


        for(int i = 0 ; i<4;i++)
        {
            fs[i]=  new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/resize"+i);
        }
        PicCount=0;
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {

                    case R.id.Buy_MyAddress:
                    {

                        break;
                    }
                    case R.id.Buy_NewAddress:
                    {
                        Intent in = new Intent(BuyNotice.this, WebViewActivity.class);
                        startActivityForResult(in, SEARCH_ADDRESS_ACTIVITY);

                        break;
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

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

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);



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
                        fs[count] = f;

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    ExifInterface exif = new ExifInterface(fs[count].getPath());

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

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(fs[count]), null, null);
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

                    saveBitmapAsFile(img[count],Environment.getExternalStorageDirectory().getAbsolutePath() + "/resize"+count);
                    iv[count].setImageBitmap(correctBmp);


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
        else if(requestCode==2)
        {

            if (resultCode == RESULT_OK) {
                try {

                    Address.setText(data.getStringExtra("Adress"));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if(requestCode==SEARCH_ADDRESS_ACTIVITY)
        {
            if(resultCode == RESULT_OK){

                String st = data.getExtras().getString("data");
                if (st != null)
                    Address.setText(st);

            }
        }
        else if(requestCode==3)
        {
            if(resultCode == RESULT_OK){

                String st = data.getExtras().getString("result");
                if (st != null)
                {
                    Category.setText(st);
                    categori = st;
                }

            }
        }
        else if(requestCode==4)
        {

            if (resultCode == RESULT_OK) {
                try {
                    String st = data.getExtras().getString("data");
                    if (st != null)
                        Address.setText(st);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }



    public void OpenMap(View view)
    {
        if(view.getId()==R.id.Buy_NewAddress) {
            Intent in = new Intent(BuyNotice.this, WebViewActivity.class);
            startActivityForResult(in, 4);
        }

    }

    public void NoticeDate(View view)
    {
        Date today = new Date();
        String strdate= null;

        SimpleDateFormat format1  = new SimpleDateFormat();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("yyyy-MM-dd");

            strdate = format1.format(today);


        }







        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                Year.setText(String.valueOf(year)+"년");
                Month.setText(String.valueOf(month+1)+"월");
                Day.setText(String.valueOf(dayOfMonth)+"일");

            }
        }
                ,  Integer.parseInt(strdate.split("-")[0]), Integer.parseInt(strdate.split("-")[1])-1, Integer.parseInt(strdate.split("-")[2]));



        dialog.show();



    }

    public void Notice_Register(View view)
    {
        //게시글 등록

        PostAsync post = new PostAsync();
        post.execute();
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

    public void backPress(View view)
    {
        finish();
    }

    public void goCategory(View view)
    {

        Intent in = new Intent(this,Category.class);
        startActivityForResult(in,3);

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

            for(int i=0;i<count;i++)
            {
                if(fs[i]!=null) {
                    TransferObserver observer = transferUtility.upload(
                            "samaimage",
                            "post/"+postnum+"/image"+i+".png",
                            fs[i]);
                }
            }
            return null;
        }
    }
    class PostAsync extends AsyncTask<Void, String, Void>
    {
        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].equals("글쓰기 성공"))
            {
                postnum = values[1];
                DataAsync data = new DataAsync();
                data.execute();
            }
            else
            {
                Toast.makeText(BuyNotice.this, "게시글 등록이 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://52.79.255.160:8080/posting.jsp";
            //제목 -> Title.getText().toString();
            String param = "?id="+id+"&contents="+Contents.getText().toString()+"&picture="+count+"&quantity="+Countitem.getText().toString()
                    +"&address="+Address.getText().toString()+ExtraAddress.getText().toString()+"&s_date="+Year.getText().toString()+" "+Month.getText().toString()+" "+Day.getText().toString()
                    +"&categori="+categori+"&categori2="+Pickeditem+"&title="+Title.getText().toString();
            Document xml = null;
            String u = url +param;
            Log.i("zzzzzzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {
                if (e.select("result").text().equals("true"))
                    publishProgress("글쓰기 성공",e.select("postnum").text());
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
}