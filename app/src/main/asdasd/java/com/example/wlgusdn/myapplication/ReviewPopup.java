package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewPopup  extends Activity
{
    Button[] Starr;
    EditText textt;
    Button Notice;
    Button Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_review);

         Starr = new Button[6];

        Starr[0]=findViewById(R.id.review_star);
        Starr[1]=findViewById(R.id.review_star1);
        Starr[2]=findViewById(R.id.review_star2);
        Starr[3]=findViewById(R.id.review_star3);
        Starr[4]=findViewById(R.id.review_star4);

        textt=findViewById(R.id.review_text);

        Notice=findViewById(R.id.review_notice);
        Cancel=findViewById(R.id.review_cancel);



    }
    public void SetStar(View view) {

        switch (view.getId())
        {

            case R.id.review_star:
            {
                //1점
                Toast.makeText(ReviewPopup.this,"1점",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.review_star1:
            {
                //2점
                Toast.makeText(ReviewPopup.this,"2점",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.review_star2:
            {
                //3점
                Toast.makeText(ReviewPopup.this,"3점",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.review_star3:
            {
                //4점

                Toast.makeText(ReviewPopup.this,"4점",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.review_star4:
            {
                //5점

                Toast.makeText(ReviewPopup.this,"5점",Toast.LENGTH_SHORT).show();
                break;
            }

        }

    }

    public void Notice_Review(View view)
    {

    }

    public void Cancel_Review(View view)
    {
        finish();
    }


}
