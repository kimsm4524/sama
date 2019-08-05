package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import static android.content.Intent.ACTION_GET_CONTENT;

public class Account extends AppCompatActivity {

    ImageView BusinessCardImage;
    TextView tv,Adress,CompanyAdress;
    EditText Id, Password1,Password2,BusinessNumber,CompanyName,Name,PhoneNumber,DetailAdress,DetailCompanyAdress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        Adress=findViewById(R.id.Adress);
        CompanyAdress=findViewById(R.id.CompanyAdress);
        Id=findViewById(R.id.Account_Id);
        Password1=findViewById(R.id.Account_Password);
        Password2=findViewById(R.id.Account_Password2);
        BusinessNumber=findViewById(R.id.Account_Business_Number);
        CompanyName=findViewById(R.id.Account_CompanyName);
        Name=findViewById(R.id.Account_Name);
        PhoneNumber=findViewById(R.id.Account_Phone_Number);
        DetailAdress=findViewById(R.id.DetailAdress);
        DetailCompanyAdress=findViewById(R.id.DetailCompanyAdress);


    }

    public void Register(View view)
    {

        //생성하기
        finish();

    }


    public void UpLoadPicture(View view)
    {
        BusinessCardImage = findViewById(R.id.Account_Pictureiv);
        tv = findViewById(R.id.Account_Picturetv);
        BusinessCardImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    BusinessCardImage.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode==2)
        {

            if (resultCode == RESULT_OK) {
                try {
                    Adress.setText(data.getStringExtra("Adress"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if(requestCode==3)
        {

            if (resultCode == RESULT_OK) {
                try {
                    CompanyAdress.setText(data.getStringExtra("Adress"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public void OpenMap(View view)
    {
        if(view.getId()==R.id.Adress_Check) {
            Intent in = new Intent(Account.this, DaumWebView.class);
            startActivityForResult(in, 2);
        }
        else
        {
            Intent in = new Intent(Account.this, DaumWebView.class);
            startActivityForResult(in, 3);
        }
    }
}
