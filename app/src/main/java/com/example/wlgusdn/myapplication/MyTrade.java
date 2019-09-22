package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MyTrade extends AppCompatActivity {
    SharedPreferences sf;
    String Login = "login";
    String idd;
    private ViewPager mViewPager;
    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
    Fragment fragsell,fragbuy,fragfinish,fragtrade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrade);
        sf = getSharedPreferences(Login,0);
        idd = sf.getString("id","");
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    public void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle.putString("id", idd);
        fragbuy=new fragbuy();
        fragbuy.setArguments(bundle);
        fragsell=new fragsell();
        fragsell.setArguments(bundle);
        fragtrade=new fragtrade();
        fragtrade.setArguments(bundle);
        fragfinish=new fragfinish();
        fragfinish.setArguments(bundle);
        adapter.addFragment(fragbuy, "등록한 구매글");
        adapter.addFragment(fragsell, "입찰한 판매글");
        adapter.addFragment(fragtrade, "거래 진행중인 글");
        adapter.addFragment(fragfinish, "거래 완료된 글");
        viewPager.setAdapter(adapter);
    }
}
