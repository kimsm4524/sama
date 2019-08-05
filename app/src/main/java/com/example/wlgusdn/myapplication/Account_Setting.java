package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import  android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account_Setting extends AppCompatActivity
{

    EditText Pass1,Pass2;
    TextView address;
    LinearLayout ll;
    ScrollView scv;
    EditText Password_Edit;
    Button Password_Button;
    String Login = "login";
    SharedPreferences sf;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_setting);

        ll=findViewById(R.id.Setting_Password_Check);
        scv = findViewById(R.id.Setting_ScrollView);
        sf = getSharedPreferences(Login,0);


        Pass1=findViewById(R.id.setting_Password1);
        Pass2=findViewById(R.id.setting_Password2);
        address = findViewById(R.id.setting_Address);
        Password_Edit = findViewById(R.id.Setting_Password_Check_EditText);
        Password_Button = findViewById(R.id.Setting_Password_Check_Button);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    public void ChangePassword(View view)
    {

        if(Pass1==Pass2)
        {
            //DB에 비밀번호 변경

            Toast.makeText(Account_Setting.this,"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show();

        }
        else
        {
            //비밀번호를 다시 확인해주세요.

            Toast.makeText(Account_Setting.this,"비밀번호가 일치하지 않습니다..",Toast.LENGTH_SHORT).show();
        }



    }

    public void ChangeAddress(View view)
    {
        String AddressStr;
        AddressStr = address.getText().toString();

        //DB에 AddressStr 저장하기


        Toast.makeText(Account_Setting.this,"주소가 변경되었습니다.",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==4)
        {

            if (resultCode == RESULT_OK)
            {
                try {
                    address.setText(data.getStringExtra("Adress"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if(requestCode==5)
        {

            if (resultCode == RESULT_OK)
            {
                try {
                    address.setText(data.getStringExtra("Adress")+"\n"+data.getStringExtra("Detail"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }


    public void OpenMap(View view)
    {

        Intent in = new Intent(Account_Setting.this, DaumWebView.class);
        startActivityForResult(in, 4);

    }

    public void OpenSettingView(View view)
    {
        new LoginAsync().execute();
    }
    class LoginAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(Account_Setting.this, values[0], Toast.LENGTH_SHORT).show();
            if(values[0]=="로그인 실패"){

            } else if(values[0]=="로그인 성공") {

                scv.setVisibility(View.VISIBLE);
                Toast.makeText(Account_Setting.this,Password_Edit.getText().toString(),Toast.LENGTH_SHORT).show();
                ll.setVisibility(View.GONE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Account_Setting.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Password_Edit.getWindowToken(), 0);
            }
        }


        protected Void doInBackground(Void... voids) {//user thread
            String url = "http://52.79.255.160:8080/login.jsp";
            String param = "?id=" + sf.getString("id","") + "&pass=" + SHA256(Password_Edit.getText().toString());
            Document xml = null;
            String u = url + param;
            Log.i("zzzzzzzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {
                if (e.select("result").text().equals("true"))
                    publishProgress("로그인 성공");
                else
                    publishProgress("로그인 실패");
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
}
