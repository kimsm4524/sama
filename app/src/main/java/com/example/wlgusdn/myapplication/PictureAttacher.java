package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureAttacher  extends AppCompatActivity
{
    CustomViewPager vp;
    Bitmap[] bmo;
    int Count;
    String postnum;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i("pictureattazzz","access");

        setContentView(R.layout.activity_picture_attacher);
        Intent in = getIntent();
        Count = in.getIntExtra("PictureCount",0);
        postnum = in.getStringExtra("postnum");
        vp=findViewById(R.id.Picture_ViewPager);
        ListImageAsync list = new ListImageAsync();
        list.execute();



    }
    class ListImageAsync extends AsyncTask<File, Void, Void> {
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),Count,2,bmo);//숫자는 글의 사진의 수
            vp.setAdapter(adapter);
        }

        protected Void doInBackground(File... values) {//user thread
            bmo = new Bitmap[Count];
            Log.i("backgroundzzz","zzzzzzzzzzzzz");
            for(int i=0;i<Count;i++)
            {
                Log.i("urlzzzzz","http://samaimage.s3.ap-northeast-2.amazonaws.com/post/" + postnum + "/" + "image"+i+".png");
                bmo[i]=loadBitmap("http://samaimage.s3.ap-northeast-2.amazonaws.com/post/" + postnum + "/" + "image"+i+".png");
            }
            publishProgress();
            return null;
        }
    }
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
