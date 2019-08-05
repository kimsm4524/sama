package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureAttacher  extends AppCompatActivity
{
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_attacher);







        Intent i = getIntent();
        int Count = i.getIntExtra("PictureCount",0);

        vp=findViewById(R.id.Picture_ViewPager);
        VpAdapter adapter = new VpAdapter(getLayoutInflater(),Count,2);//숫자는 글의 사진의 수
        vp.setAdapter(adapter);





    }

}
