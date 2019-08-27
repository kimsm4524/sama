package com.example.wlgusdn.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Intent.ACTION_GET_CONTENT;

public class Account extends AppCompatActivity {

    ImageView AccountImage,BusinessCardImage;
    TextView tv,Adress,CompanyAdress,PhoneNumber;
    EditText Id, Password1,Password2,BusinessNumber,CompanyName,Name,ExtraAdress,CompanyExtraAddress;

    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private ImageView imgAndroid,back;
    private Animation anim;
    //s3용 변수
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;
    //사업자등록증 이미지
    File businessimage;
    Bitmap businessbm;
    File myimage;
    Bitmap mybm;

    public SharedPreferences sf;
    public SharedPreferences.Editor editor;

    TelephonyManager telManager;
    String PhoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);


        sf = getSharedPreferences( "login",0);

        editor = sf.edit();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        BusinessCardImage = findViewById(R.id.Account_Company_Pictureiv);
        AccountImage = findViewById(R.id.Account_Pictureiv);
        Adress=findViewById(R.id.Adress);
        CompanyAdress=findViewById(R.id.CompanyAdress);
        Id=findViewById(R.id.Account_Id);
        Password1=findViewById(R.id.Account_Password);
        Password2=findViewById(R.id.Account_Password2);
        BusinessNumber=findViewById(R.id.Account_Business_Number);
        CompanyName=findViewById(R.id.Account_CompanyName);
        Name=findViewById(R.id.Account_Name);
        PhoneNumber=findViewById(R.id.Account_Phone_Number);
        ExtraAdress = findViewById(R.id.ExtraAddress);
        CompanyExtraAddress = findViewById(R.id.CompanyExtraAddress);

        imgAndroid = findViewById(R.id.Account_img_android);
        anim = AnimationUtils.loadAnimation(this, R.anim.loading);
        back = findViewById(R.id.Account_back);

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** * 사용자 단말기의 권한 중 "전화걸기" 권한이 허용되어 있는지 확인한다.
             *
             *
             *
             Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다. */
            int permissionResult = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            /** * 패키지는 안드로이드 어플리케이션의 아이디이다.
             *

             * 현재 어플리케이션이 CALL_PHONE에 대해 거부되어있는지 확인한다.
             */
            if (permissionResult == PackageManager.PERMISSION_DENIED) { /** * 사용자가 WRITE_EXTERNAL_STORAGE 권한을 거부한 적이 있는지 확인한다.
             * 거부한적이 있으면 True를 리턴하고 * 거부한적이 없으면 False를 리턴한다. */
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Account.this);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { /** * 새로운 인스턴스(onClickListener)를 생성했기 때문에 * 버전체크를 다시 해준다. */
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        // WRITE_EXTERNAL_STORAGE 권한을 Android OS에 요청한다.
                                        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Account.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create().show();
                } // 최초로 권한을 요청할 때
                else {
                    // WRITE_EXTERNAL_STORAGE 권한을 Android OS에 요청한다.
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                }
            }
            // CALL_PHONE의 권한이 있을 때
            else {
                // 즉시 실행
                PhoneNum = telManager.getLine1Number();
                if(PhoneNum.startsWith("+82"))
                {
                    PhoneNum = PhoneNum.replace("+82", "0");
                    PhoneNumber.setText(
                            PhoneNum.substring(0,3)+"-"+
                                    PhoneNum.substring(3,7)+"-"+
                                    PhoneNum.substring(7,11));
                }

            }
        } // 마시멜로우 미만의 버전일 때
        else
        {
            // 즉시 실행
            PhoneNum = telManager.getLine1Number();
            if(PhoneNum.startsWith("+82"))
            {
                PhoneNum = PhoneNum.replace("+82", "0");
                PhoneNumber.setText(PhoneNum);
            }
        }











    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 1000)
        {
            // 요청한 권한을 사용자가 "허용" 했다면...
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                // Add Check Permission
                if (ActivityCompat.checkSelfPermission(Account.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(Account.this,"다이얼 접근권한을 동의했습니다.",Toast.LENGTH_SHORT).show();
                }







                PhoneNumber.setText(PhoneNum);
            }
            else
            {
                Toast.makeText(Account.this, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void check(View view)
    {
        idcheck id = new idcheck();
        id.execute();
    }

    public void Register(View view)
    {
        if(Password1.getText().toString().equals(Password2.getText().toString()))
        {
            back.setVisibility(View.VISIBLE);
            imgAndroid.setVisibility(View.VISIBLE);
            imgAndroid.setAnimation(anim);
            SignupAsync sign = new SignupAsync();
            sign.execute();
            imgAndroid.clearAnimation();
            imgAndroid.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            finish();
        }
        else
        {
            Password1.setText("");
            Password2.setText("");

            //여기서 비밀번호 틀렸다고 팝업창 필요
            //그리고 elseif문으로 필요정보들 비었을때 체크 필요

        }
        //생성하기

    }


    public void UpLoadPicture(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 1);
    }
    public void UpLoadComPicture(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4) {
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
                        String[] proj = {MediaStore.Images.Media.DATA};

                        assert photoUri != null;
                        cursor = getContentResolver().query(photoUri, proj, null, null, null);

                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        cursor.moveToFirst();

                        businessimage = new File(cursor.getString(column_index));

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    ExifInterface exif = new ExifInterface(businessimage.getPath());

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

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(businessimage), null, null);
                    correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);






                    businessbm = correctBmp;
                    BusinessCardImage.setImageBitmap(businessbm);
                    Log.i("zzzzzzzzzzz", businessimage.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode==2)
        {

            if (resultCode == RESULT_OK) {
                try {
                    String st = data.getExtras().getString("data");
                    if (st != null)
                        Adress.setText(st);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if(requestCode==3)
        {

            if (resultCode == RESULT_OK) {
                try {
                    String st = data.getExtras().getString("data");
                    if (st != null)
                        CompanyAdress.setText(st);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode==1)
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
                        String[] proj = {MediaStore.Images.Media.DATA};

                        assert photoUri != null;
                        cursor = getContentResolver().query(photoUri, proj, null, null, null);

                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        cursor.moveToFirst();

                        myimage = new File(cursor.getString(column_index));

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    ExifInterface exif = new ExifInterface(myimage.getPath());

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

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(myimage), null, null);
                    correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);

                    mybm = correctBmp;
                    AccountImage.setImageBitmap(mybm);
                    Log.i("zzzzzzzzzzz", myimage.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }




    public void OpenMap(View view)
    {
        if(view.getId()==R.id.Adress_Check) {
            Intent in = new Intent(Account.this, WebViewActivity.class);
            startActivityForResult(in, 2);
        }
        else
        {
            Intent in = new Intent(Account.this, WebViewActivity.class);
            startActivityForResult(in, 3);
        }
    }
    class SignupAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(values[0].equals("회원가입 성공"))
            {

                Log.d("Accounttt","ddd");
                new CardImageAsync().execute();

                Log.d("Accounttt","ddd");

                String id = Id.getText().toString();
                UserData1 userData = new UserData1();
                userData.userEmailID = id;
                userData.fcmToken = FirebaseInstanceId.getInstance().getToken();
                userData.TradeAlarm = "true";

                editor.putString("SamaAlarm","true");
                editor.putString("TradeAlarm","true");
                editor.commit();

                mFirebaseDatabase.getInstance().getReference("users").child(userData.userEmailID).setValue(userData);







                mFirebaseDatabase.getReference("users").child(userData.userEmailID).setValue(userData);



            }else{


            }

        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/signup.jsp";
            String param = "?id=" + Id.getText().toString() + "&name=" + Name.getText().toString() +
                    "&phonenumber=" + PhoneNumber.getText().toString() + "&address=" + Adress.getText().toString()+ExtraAdress.getText().toString()
                    + "&business_number=" + BusinessNumber.getText().toString() + "&passwd=" + SHA256(Password1.getText().toString())
                    + "&com_name=" + CompanyName.getText().toString() + "&com_add=" + CompanyAdress.getText().toString()+CompanyExtraAddress.getText().toString();
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
                if (e.select("result").text().equals("true")) {
                    Log.d("Accountt","성공");
                    publishProgress("회원가입 성공");
                } else
                {
                    Log.d("Accountt","실패");
                    publishProgress("회원가입 실패");
                }
            }

            return null;
        }
    }
    public String SHA256(String st){

        String SHA = "";
        String str = "nidfmkasof"+st;
        try{

            MessageDigest sh = MessageDigest.getInstance("SHA-256");

            sh.update(str.getBytes());

            byte byteData[] = sh.digest();

            StringBuffer sb = new StringBuffer();

            for(int i = 0 ; i < byteData.length ; i++){

                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

            }

            SHA = sb.toString();



        }catch(NoSuchAlgorithmException e){

            e.printStackTrace();

            SHA = null;

        }

        return SHA;

    }
    class CardImageAsync extends AsyncTask<Void, String, Void> {
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

            Log.i("zzzzzzzzzzzzzzz","error here");
            transferUtility = new TransferUtility(s3, getApplicationContext());

            TransferObserver observer = transferUtility.upload(
                    "samaimage",
                    "user/"+Id.getText().toString()+"/"+"businesscard.png",
                    businessimage);
            TransferObserver observer1 = transferUtility.upload(
                    "samaimage",
                    "user/"+Id.getText().toString()+"/"+"image.png",
                    myimage);
            return null;
        }
    }
    class idcheck extends AsyncTask<Void, String, Void>
    {
        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].equals("회원가입 가능"))
            {
                Toast.makeText(Account.this,values[0],Toast.LENGTH_SHORT);
                Password1.requestFocus();
            }
            else
            {
                Toast.makeText(Account.this,values[0],Toast.LENGTH_SHORT);
                Id.setText("");
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://52.79.255.160:8080/idcheck.jsp";
            String param = "?id="+Id.getText().toString();
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
                    publishProgress("아이디 중복");
                else
                    publishProgress("회원가입 가능");
            }
            return null;
        }
    }

}