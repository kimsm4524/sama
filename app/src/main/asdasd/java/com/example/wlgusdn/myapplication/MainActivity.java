package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList NData;
    GridView gl;
    NoticeListAdapter nadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        final String[] count = {"수량 : 1","수량 : 100","수량 : 500","수량 : 203","수량 : 1","수량 : 100","수량 : 500","수량 : 203"};
        final String[] person = {"입찰자 수 : 3","입찰자 수 : 5","입찰자 수 : 2","입찰자 수 : 10","입찰자 수 : 3","입찰자 수 : 5","입찰자 수 : 2","입찰자 수 : 10"};
        final String[] date = {"희망배송일 : \n2019년 9월 1일","희망배송일 : \n2019년 1월 11일"
                ,"희망배송일 : \n2019년 10월 20일","희망배송일 : \n2019년 12월 25일","희망배송일 : \n2019년 9월 1일","희망배송일 : \n2019년 1월 11일"
                ,"희망배송일 : \n2019년 10월 20일","희망배송일 : \n2019년 12월 25일"};

        //최근 6개의 글 정보 불러오기

        NData = new ArrayList<>();
        for(int i=0;i<6;i++)
        {
            NoticeData n = new NoticeData();
            n.Count = count[i%6];
            n.PersonNum = person[i%6];
            n.Date = date[i%6];
            NData.add(n);
        }

       gl = (GridView) findViewById(R.id.Main_GridView);
        nadapter = new NoticeListAdapter(NData,false) ;
        gl.setAdapter(nadapter);









    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override//툴바메뉴 선택시
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_AppInformation) {
            // Handle the camera action
        } else if (id == R.id.nav_Chat) {

        } else if (id == R.id.nav_Information) {

        } else if (id == R.id.nav_MyOrder) {
            Intent intent = new Intent(MainActivity.this,MyTrade.class);
            startActivity(intent);

        } else if (id == R.id.nav_Order)
        {

            Intent intent = new Intent(MainActivity.this,BuyNotice.class);
            startActivity(intent);

        } else if (id == R.id.nav_QnA) {

        }else if(id == R.id.nav_Account_Setting)
        {
            Intent intent = new Intent(MainActivity.this,Account_Setting.class);
            startActivity(intent);
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void BuyNotice(View view)
    {
        Intent intent = new Intent(MainActivity.this,BuyNotice.class);
        startActivity(intent);
    }

    public void GoTrade(View view)
    {
        Intent intent = new Intent(MainActivity.this,Trade.class);
        startActivity(intent);
    }


    public void GoSearch(View view)
    {
        Intent intent = new Intent(MainActivity.this,Search.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 2)
        {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");

                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                //result -> 카테고리


            }


        }
    }
}
