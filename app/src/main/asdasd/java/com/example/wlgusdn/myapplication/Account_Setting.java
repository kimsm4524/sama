package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import  android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class Account_Setting extends AppCompatActivity
{

    EditText Pass1,Pass2;
    TextView address;
    EditText Detail_Address;
    LinearLayout ll;
    ScrollView scv;
    EditText Password_Edit;
    Button Password_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_setting);

        ll=findViewById(R.id.Setting_Password_Check);
        scv = findViewById(R.id.Setting_ScrollView);

        Pass1=findViewById(R.id.setting_Password1);
        Pass2=findViewById(R.id.setting_Password2);
        address = findViewById(R.id.setting_Address);
        Detail_Address=findViewById(R.id.setting_Detail_Address);
        Password_Edit = findViewById(R.id.Setting_Password_Check_EditText);
        Password_Button = findViewById(R.id.Setting_Password_Check_Button);
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
        AddressStr = address.getText().toString()+Detail_Address.getText();

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
                    address.setText(data.getStringExtra("Adress"));

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



        if(Password_Edit.getText().toString().equals("1"))
        {
            scv.setVisibility(View.VISIBLE);
            Toast.makeText(this,Password_Edit.getText().toString(),Toast.LENGTH_SHORT).show();
            ll.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager) getSystemService(Account_Setting.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Password_Edit.getWindowToken(), 0);



        }
        else
        {
            //비밀번호가 틀렸습니다.
        }


    }
}
