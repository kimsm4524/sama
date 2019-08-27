package com.example.wlgusdn.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {

    ScrollView nscv;
    HorizontalScrollView scv;
    public static String Myname;
    GridView gl;
    NoticeListAdapteraaa nadapter;
    String Login = "login";
    String id,token;
    NoticeData temp;
    int posts=0;
    SwipeRefreshLayout ReFresh;
    public static Boolean check;
    ArrayList NData;
    ImageView Loginiv,Logoutiv;
   public static SharedPreferences sf;
    AmazonS3 s3;
    TransferUtility transferUtility;
    File f;
    Bitmap bm;
    Bitmap[] bmp;
    private FirebaseDatabase mFirebaseDatabase;
    private BackPressCloseHandler backPressCloseHandler;

    private DatabaseReference mDatabaseReference;
    private Loading l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                int temp = Integer.parseInt(dataSnapshot.child("Visit").getValue(true).toString());
                temp++;
                mDatabaseReference.child("Visit").setValue(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Loginiv = findViewById(R.id.Main_Login);

        Logoutiv = findViewById(R.id.Main_Logout);

        nscv = findViewById(R.id.Main_Nest);
        scv = findViewById(R.id.Main_Scv);





        check=false;
        sf = getSharedPreferences(Login, 0);
        id = sf.getString("id", "");
        Myname = id;
        token = sf.getString("Token","");
        if(!sf.contains("id"))
        {
            Loginiv.setVisibility(View.VISIBLE);
            Logoutiv.setVisibility(View.GONE);

        }
        else
        {
            Loginiv.setVisibility(View.GONE);
            Logoutiv.setVisibility(View.VISIBLE);
        }

       l = new Loading(MainActivity.this);
       l.show();

        Loginiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent it = new Intent(MainActivity.this,Login.class);
                startActivity(it);
                finish();

            }
        });
        Logoutiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserData1 userData = new UserData1();
                userData.userEmailID = sf.getString("id","");
                userData.fcmToken = "null";

                Boolean bl;

                if(sf.getString("SamaAlarm","").equals("true"))
                {
                    bl=true;
                }
                else
                {
                    bl=false;
                }


                if(sf.getString("TradeAlarm","").equals("true"))
                {
                    userData.TradeAlarm="true";
                }
                else
                {
                    userData.TradeAlarm="false";
                }

                mDatabaseReference.child("users").child(userData.userEmailID).setValue(userData);

                final SharedPreferences.Editor editor = sf.edit();
                editor.clear();

                if(bl==true)
                {
                    editor.putString("SamaAlarm","true");
                }
                else
                {
                    editor.putString("SamaAlarm","false");

                }


                editor.commit();



                Intent intent = getIntent();
                finish();
                startActivity(intent);
                if(!sf.contains("id"))
                {
                    Myname = null;


                    Log.d("cclleeaarr","삭제성공");
                }
                Loginiv.setVisibility(View.VISIBLE);
                Logoutiv.setVisibility(View.GONE);

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ReFresh=(SwipeRefreshLayout)findViewById(R.id.Main_ReFresh);
        ReFresh.setOnRefreshListener(MainActivity.this);
        NData = new ArrayList<NoticeData>();
        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/userimage");

        //아이디 받아오는 부분 추가




        new UserAsync().execute();

        new ImageAsync().execute();

        new ListingAsync().execute();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        gl = (GridView) findViewById(R.id.Main_GridView);

        scv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                nscv.requestDisallowInterceptTouchEvent(true);


                return  false;

            }
        });


    }


    protected void onResume() {

        super.onResume();

        //Activity가 비로소 화면에 보여지는 단계, 사용자에게 Focus를 잡은 상태

    }

    public static void MainCloseLoad()
    {



    }

    @Override
    public void onBackPressed() {

        backPressCloseHandler.onBackPressed();
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
        int eid = item.getItemId();


        if (eid == R.id.nav_AppInformation) {
            // Handle the camera action
        } else if (eid == R.id.nav_Chat) {

        } else if (eid == R.id.nav_Information) {

        } else if (eid == R.id.nav_MyOrder) {
            if(sf.contains("id"))
            {
                Intent intent = new Intent(MainActivity.this, MyTrade.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
            }
        } else if (eid == R.id.nav_Alarm_Setting) {
            if(sf.contains("id"))
            {
            Intent intent = new Intent(MainActivity.this, AlramPopup.class);
            intent.putExtra("id",id);
            startActivity(intent);
            }
            else
            {
                Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
            }
        } else if (eid == R.id.nav_Order) {
                if(sf.contains("id"))
                {
            Intent intent = new Intent(MainActivity.this, BuyNotice.class);
            startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
                }
        } else if (eid == R.id.nav_QnA) {

        } else if (eid == R.id.nav_Account_Setting) {
                    if(sf.contains("id"))
                    {

            Intent intent = new Intent(MainActivity.this, Account_Setting.class);
            startActivity(intent); }
        else
        {
            Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
        }
        }
        else if (eid == R.id.nav_Basket)
        {
            if(sf.contains("id"))
                {
            Intent intent = new Intent(MainActivity.this, Basket.class);
            intent.putExtra("id",id);
            startActivity(intent);
                }
            else
            {
                Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void BuyNotice(View view) {
        if(sf.contains("id"))
        {
            Intent intent = new Intent(MainActivity.this, BuyNotice.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
        }
    }


    public void GoSearch(View view) {
        Intent intent = new Intent(MainActivity.this, Search.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)//로그인에서 다시 오기
        {
            if(resultCode==RESULT_OK)
            {
                sf = getSharedPreferences(Login, 0);
                id = sf.getString("id", "");
                token = sf.getString("Token","");
                if(!sf.contains("id"))
                {
                    Loginiv.setVisibility(View.VISIBLE);
                    Logoutiv.setVisibility(View.GONE);
                }
                else
                {
                    Loginiv.setVisibility(View.GONE);
                    Logoutiv.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this,"로그인 되었습니다.", Toast.LENGTH_SHORT).show();

            }
        }

        else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");

                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                //result -> 카테고리


            }


        }

    }



    public void ChargePoint(View view) {

        if(sf.contains("id"))
        {

        }
        else
        {
            Toast.makeText(MainActivity.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRefresh() {
        // 새로고침시 변경해야할 사항 변경

        Toast.makeText(MainActivity.this,"새로고침 완료",Toast.LENGTH_SHORT).show();
        new ListingAsync().execute();
        NoticeData nd = new NoticeData();
        nd.Title = "asdfasfd";



        ReFresh.setRefreshing(false);
    }



    class UserAsync extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        SharedPreferences.Editor editor;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header_view = navigationView.getHeaderView(0);

            Log.i("zzzzzzz",point+"/"+name);
            TextView namet = (TextView)header_view.findViewById(R.id.Nav_id);
            namet.setText(name);



        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            editor = sf.edit();
            String url = "http://52.79.255.160:8080/userinfo.jsp";
            String param = "?id="+id;
            Document xml = null;
            String u = url + param;
            Log.i("zzzzzzzz",u);
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {
                name = e.select("name").text().toString();
                point = e.select("point").text().toString();
                editor.putString("name",e.select("name").text().toString());
                editor.putString("phonenumber",e.select("phonenumber").text().toString());
                editor.putString("address",e.select("address").text().toString());
                editor.putString("business_num",e.select("business_num").text().toString());
                editor.putString("business_add",e.select("business_add").text().toString());
            }
            editor.commit();
            Log.i("zzzzzzzzzzzzz",name);
            publishProgress();
            return null;
        }
    }


    public void update()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_view = navigationView.getHeaderView(0);
        ImageView imagev = (ImageView)header_view.findViewById(R.id.Nav_Account_Picture);
        imagev.setImageBitmap(bm);

    }

    class ImageAsync extends AsyncTask<Void, String, Void> {
        ImageView imagev;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            try{   bm =  BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagev.setImageBitmap(bm);
            update();

            Log.d("checkzzz","Image");
        }

        protected Void doInBackground(Void... voids) {//user thread
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header_view = navigationView.getHeaderView(0);
            imagev = (ImageView)header_view.findViewById(R.id.Nav_Account_Picture);
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:6fb92d56-fccc-4470-af83-af13c271a5b1", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
            s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");


            transferUtility = new TransferUtility(s3, getApplicationContext());
            TransferObserver observer = transferUtility.download(
                    "samaimage",
                    "user/"+id+"/"+"image.png",
                    f
            );
            observer.setTransferListener(new TransferListener() {
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    // update progress bar
                    //progressBar.setProgress(bytesCurrent);
                    Log.i("zzzzzzzzzzzz", "progress changed");
                }

                public void onStateChanged(int id, TransferState state) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    //Bitmap fin = rotateBitmap(myBitmap, 90);
                    //portraitView.setImageBitmap(myBitmap);
                    bm = myBitmap;
                    update();
                }

                public void onError(int id, Exception ex) {
                    Log.e("ERROR", ex.getMessage(), ex);
                    Log.i("ERROR", "189");
                    Log.i("ERROR", "image is:");
                    Log.i("ERROR", "iFile is:");
                    update();
                }
            });
            Log.i("zzzzzzzzzzz",observer.getState().toString());
            Log.i("zzzzzzzzzzzzzzzzzzzzz",f.getName());
            return null;
        }
    }

    class ListingAsync extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            nadapter = new NoticeListAdapteraaa(NData, false);
            gl.setAdapter(nadapter);
            gl.setOnItemClickListener(new OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NoticeData temp = (NoticeData)NData.get(position);
                    Intent i = null;
                    if(!temp.id.equals(sf.getString("id","")))
                    {
                        i = new Intent(MainActivity.this, TradeBidderYet.class);
                    }

                    else
                    {
                        i = new Intent(MainActivity.this, TradeBuyerYet.class);
                    }
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",temp.postnum);

                    startActivity(i);
                }
            });
            Handler hd = new Handler();
            hd.postDelayed(new MainActivity.splashhandler(), 1000);
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/main.jsp";
            Document xml = null;
            posts=0;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            NData.clear();
            for (Element e : result) {
                NoticeData nd = new NoticeData();
                nd.Count = e.select("quantity").text().toString();
                nd.PersonNum = e.select("seller").text().toString();
                nd.Title = e.select("title").text().toString();
                nd.id = e.select("postid").text();
                nd.postnum = e.select("postnum").text().toString();
                nd.url="http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+nd.postnum+"/image0.png";
                NData.add(nd);
            }
            posts=result.size();
            publishProgress();
            return null;
        }
    }
    private class splashhandler implements Runnable{
        public void run(){

            l.dismiss();
        }
    }
    class NoticeListAdapteraaa extends BaseAdapter
    {


        private ArrayList<NoticeData> MyNoticeData = new ArrayList<NoticeData>();
        private int NoticeListCount=0;
        LayoutInflater inflater = null;
        private boolean Check;
        public int intcheck;
        SharedPreferences sf;

        public NoticeListAdapteraaa(ArrayList<NoticeData> myNoticeData, boolean check)
        {
            intcheck=myNoticeData.size();
            MyNoticeData = myNoticeData;
            NoticeListCount = myNoticeData.size();
            Check=check;
            //sf = getSharedPreferences(Login, 0);



        }

        @Override
        public int getCount()
        {

            return NoticeListCount;
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            if(Check==true) {

                if (convertView == null) {
                    final Context context = parent.getContext();
                    if (inflater == null) {
                        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    }
                    convertView = inflater.inflate(R.layout.notice_info, parent, false);
                }

                //


                ImageView NoticeDataImage = convertView.findViewById(R.id.Notice_imageView);
                TextView NoticeDataCount = convertView.findViewById(R.id.Notice_Count);
                TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice_BidCount);
                TextView NoticeDataDate = convertView.findViewById(R.id.Notice_Title);
                TextView NoticeDataTitle = convertView.findViewById(R.id.Notice_Title);
                TextView NoticeDataHit = convertView.findViewById(R.id.Notice_Hit_TextView);
                Log.i("zzzzzzzzzzzzzz",MyNoticeData.get(position).url);
                ToggleButton tb = convertView.findViewById(R.id.Notice_Toggle);




                tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked)//선택됨
                        {
                            //장바구니에 담겨야함
                        }
                        else
                        {
                            //장바구니에서 삭제해야함
                        }

                    }
                });

                Picasso.get().load(MyNoticeData.get(position).url).into(NoticeDataImage);
                NoticeDataTitle.setText(MyNoticeData.get(position).Title);
                NoticeDataCount.setText("수량 : "+MyNoticeData.get(position).Count);
                NoticeDataPersonNum.setText("입찰자 수 : "+MyNoticeData.get(position).PersonNum);
                NoticeDataHit.setText("조회수 : "+MyNoticeData.get(position).Hit);
            }
            else
            {
                if (convertView == null) {
                    final Context context = parent.getContext();
                    if (inflater == null) {
                        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    }
                    convertView = inflater.inflate(R.layout.notice, parent, false);
                }

                //

                ImageView NoticeDataImage = convertView.findViewById(R.id.Notice1_imageView);
                TextView NoticeDataCount = convertView.findViewById(R.id.Notice1_Count);
                TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice1_BidCount);
                TextView NoticeDataDate = convertView.findViewById(R.id.Notice1_Title);
                TextView NoticeTitle = convertView.findViewById(R.id.Notice1_Title);
                Log.i("zzzzzzzzzzzzzz",MyNoticeData.get(position).url);


                Picasso.get().load(MyNoticeData.get(position).url).into(NoticeDataImage);
                NoticeDataCount.setText("수량 : "+MyNoticeData.get(position).Count);
                NoticeDataPersonNum.setText("입찰자 수 : "+MyNoticeData.get(position).PersonNum);
                NoticeTitle.setText("제목 : "+MyNoticeData.get(position).Title);

            }


            return convertView;
        }



    }



}
