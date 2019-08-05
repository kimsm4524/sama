package com.example.wlgusdn.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class TradeSelect  extends Trade implements View.OnClickListener
        {

            SharedPreferences sf;
    private static final int RC_SIGN_IN = 1001;
    static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAA2j7nEs4:APA91bEVEE2yEeR8aiaZ8CQcErx0EGlJeFSiOejjBjtgZGYAJ8k80_r_iEP0_QYCSLoPtoAG1w4cOInqx3bbpthWMIi-PvYEpYEGOvFGENhoz_7D5LqivNwk2QKQh1b8J-xrcpn9_2WK";
    // Firebase - Realtime Database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    // Firebase - Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    // Views
    private ListView mListView;
    private EditText mEdtMessage;
    private SignInButton mBtnGoogleSignIn; // 로그인 버튼
    private Button mBtnGoogleSignOut; // 로그아웃 버튼
    private TextView mTxtProfileInfo; // 사용자 정보 표시
    private ImageView mImgProfile; // 사용자 프로필 이미지 표시

    // Values
    private ChatAdapter mAdapter;
    private String userName;
    private String Postid,Sellid;
    ViewPager Buyer_Vp,Bidder_Vp;
    TextView Buyer_Count,Buyer_Date,Buyer_Character,Buyer_Address;
    TextView Bidder_Count,Bidder_Price,Bidder_Date,Bidder_Character;
    RadioButton Bidder_RadioButon;
    Button button;

    UserData Buyer,Bidder;

    ListView Buyerlv,Bidderlv;

    public ImageView imgAndroid,back;
    public Animation anim;

    int picturenum;
    String postnum;
    File f;
    TransferUtility transferUtility;
    File[] fso;
    Bitmap[] bmo;
    Intent in;
    String OppositeId;
    AmazonS3 s3;
    private Loading l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_select);



        l = new Loading(TradeSelect.this);

        Intent intent = getIntent();
        postnum = intent.getStringExtra("postnum");
       Postid = intent.getStringExtra("Postid");
       Sellid = intent.getStringExtra("Sellid");

        picturenum=0;



        sf = getSharedPreferences("login", 0);

        Log.d("chcheck","Myname = "+MainActivity.Myname);

        Log.d("chcheck","sf = "+sf.getString("id",""));




        mListView = (ListView) findViewById(R.id.list_message1);

        Buyer_Vp = findViewById(R.id.Select_Buyer_ViewPager);
        VpAdapter Buyadapter = new VpAdapter(getLayoutInflater(), 3, 1);//숫자는 글의 사진의 수
        Buyer_Vp.setAdapter(Buyadapter);

        Buyer_Count = findViewById(R.id.Select_Buyer_Count);
        Buyer_Address = findViewById(R.id.Select_Buyer_Address);
        Buyer_Date = findViewById(R.id.Select_Buyer_Date);
        Buyer_Character = findViewById(R.id.Select_Buyer_Character);

        Bidder_Vp = findViewById(R.id.Select_Bidder_ViewPager);
        VpAdapter Bidadapter = new VpAdapter(getLayoutInflater(), 3, 1);//숫자는 글의 사진의 수
        Bidder_Vp.setAdapter(Bidadapter);
        Bidder_RadioButon = findViewById(R.id.Select_Bidder_RadioButton);
        Bidder_Count = findViewById(R.id.Select_Bidder_Count);
        Bidder_Price = findViewById(R.id.Select_Bidder_Price);
        Bidder_Date = findViewById(R.id.Select_Bidder_Date);
        Bidder_Character = findViewById(R.id.Select_Bidder_Character);

        button = findViewById(R.id.Select_Button);

        if(Postid.equals(MainActivity.Myname))
        {
           button.setText("거래 결정하기");
        }
        else
        {
            button.setText("입찰 취소하기");
        }

        ArrayList<UserData> BuyerData;
        ArrayList<UserData> BidderData;

            BuyerData = new ArrayList<>();

            Buyer = new UserData();

            Buyer.Id = Postid;

            Buyer.Name = Postid;
            Buyer.Phone = "거래진행시 공개";
            Buyer.Profile = null;
            BuyerData.add(Buyer);

            BidderData = new ArrayList<>();

            Bidder = new UserData();
            Bidder.Id = Sellid;
            Bidder.Name = Sellid;
            Bidder.Phone = "거래진행시 공개";
            Bidder.Profile = null;
            BidderData.add(Bidder);



        Buyerlv = (ListView) findViewById(R.id.Select_Buyer_Listview);
        UserAdapter buyeradapter = new UserAdapter(BuyerData);
        Buyerlv.setAdapter(buyeradapter);

        Bidderlv = (ListView) findViewById(R.id.Select_Bidder_Listview);
        UserAdapter bidderadapter = new UserAdapter(BidderData);
        Bidderlv.setAdapter(bidderadapter);



        Buyerlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(TradeSelect.this, OpponentInfoPopup.class);
                in.putExtra("Id", Postid);

                startActivity(in);
            }
        });



        Bidderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(TradeSelect.this, OpponentInfoPopup.class);
                in.putExtra("Id", Sellid);
                startActivity(in);
            }
        });

        setListViewHeightBasedOnChildren(Buyerlv);
        setListViewHeightBasedOnChildren(Bidderlv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().equals("거래 결정하기"))
                {
                    Toast.makeText(TradeSelect.this, "거래 결정하기", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(TradeSelect.this);
                    // 다이얼로그 메세지
                    alertdialog.setMessage("이 입찰자와 거래를 진행하시겠습니까?");

                    // 확인버튼
                    alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            Intent in = new Intent(TradeSelect.this,TradeAlready.class);
                            in.putExtra("Postid",Postid);
                            in.putExtra("Sellid",Sellid);
                            in.putExtra("Postnum",postnum);
                            startActivity(in);

                            Toast.makeText(TradeSelect.this, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                            //구매글, 글에 대한 입찰글 삭제

                        }
                    });

                    // 취소버튼
                    alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            //Do Nothing

                        }
                    });
                    // 메인 다이얼로그 생성
                    AlertDialog alert = alertdialog.create();
                    // 아이콘 설정
                    alert.setIcon(R.drawable.logo);
                    // 타이틀
                    // alert.setTitle("");
                    // 다이얼로그 보기
                    alert.show();




                } else
                {


                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(TradeSelect.this);
                    // 다이얼로그 메세지
                    alertdialog.setMessage("입찰글을 삭제하시겠습니까?");

                    // 확인버튼
                    alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            Toast.makeText(TradeSelect.this, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                            //구매글, 글에 대한 입찰글 삭제

                        }
                    });

                    // 취소버튼
                    alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            //Do Nothing

                        }
                    });
                    // 메인 다이얼로그 생성
                    AlertDialog alert = alertdialog.create();
                    // 아이콘 설정
                    alert.setIcon(R.drawable.logo);
                    // 타이틀
                    // alert.setTitle("");
                    // 다이얼로그 보기
                    alert.show();

                }

            }
        });


        ListingAsync list = new ListingAsync();
        list.execute();


        initViews();
        initFirebaseDatabase();





    }

    private void initViews() {
        mAdapter = new ChatAdapter(this, 0);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ChatData chatData = mAdapter.getItem(position);
                if (!TextUtils.isEmpty(chatData.userEmail)) {

                }
            }
        });

        mEdtMessage = (EditText) findViewById(R.id.edit_message1);
        findViewById(R.id.btn_send1).setOnClickListener(this);



    }

    private void initFirebaseDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
                mAdapter.add(chatData);
                mListView.smoothScrollToPosition(mAdapter.getCount());
                setListViewHeightBasedOnChildren(mListView);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                        mAdapter.remove(mAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabaseReference.child(postnum).addChildEventListener(mChildEventListener);

    }









    private void sendPostToFCM( final String message) {



        mFirebaseDatabase.getReference("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final UserData1 userData = dataSnapshot.getValue(UserData1.class);
                        final String token = dataSnapshot.child(OppositeId).child("fcmToken").getValue(true).toString();
                        Log.d("checkkk",dataSnapshot.toString());

                        if(dataSnapshot.child(OppositeId).child("MessageOnOff").getValue(true).toString().equals("On")) {

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {

                                        // FMC 메시지 생성 start
                                        JSONObject root = new JSONObject();
                                        JSONObject notification = new JSONObject();
                                        notification.put("body", message);
                                        notification.put("title", getString(R.string.app_name));
                                        root.put("notification", notification);

                                        Log.d("chcheck", "내 아이디 : " + sf.getString("id", ""));
                                        Log.d("chcheck", "상대 아이디 : " + OppositeId + "상대 토큰 : " + token);


                                        root.put("to", token);   // FMC 메시지 생성 end

                                        URL Url = new URL(FCM_MESSAGE_URL);
                                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                        conn.setRequestMethod("POST");
                                        conn.setDoOutput(true);
                                        conn.setDoInput(true);
                                        conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                                        conn.setRequestProperty("Accept", "application/json");
                                        conn.setRequestProperty("Content-type", "application/json");
                                        OutputStream os = conn.getOutputStream();
                                        os.write(root.toString().getBytes("utf-8"));
                                        os.flush();
                                        conn.getResponseCode();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mChildEventListener);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send1:
                String message = mEdtMessage.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    mEdtMessage.setText("");
                    ChatData chatData = new ChatData();
                    chatData.userName = sf.getString("id","");
                    chatData.message = message;
                    chatData.time = System.currentTimeMillis();

                     mDatabaseReference.child(postnum).push().setValue(chatData);


                     sendPostToFCM(chatData.message);

                }
                break;

        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void GoAttacher(View view) {
        Intent in = new Intent(TradeSelect.this,PictureAttacher.class);
        in.putExtra("PictureCount",picturenum);
        startActivity(in);
    }



    public void Send_Message(View view) {



    }

    public void SelectBidder(View view) {



    }


    class ListingAsync extends AsyncTask<Void, String, Void> {
        String count;
        String day;
        String content;

        @Override
        protected void onPreExecute() {
            l.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Buyer_Count.setText(count);
            Buyer_Date.setText(day);
            Buyer_Character.setText(content);



            if(Postid.equals(MainActivity.Myname))
            {
                //게시글 등록 아이디(구매자)와 현재 내 아이디가 같으면
                OppositeId = Sellid;
            }
            else
            {
                //내가 판매자일때
                OppositeId = Postid;
            }



            Log.d("rkwkwha","Main.Myname"+MainActivity.Myname);
            Log.d("rkwkwha","Postid"+Postid);
            Log.d("rkwkwha","Sellid"+Sellid);
            Log.d("rkwkwha","Opposite"+OppositeId);


            Log.i("picturenumzz",picturenum+"");
            TradeSelect.ListImageAsync listimage = new TradeSelect.ListImageAsync();
            listimage.execute();
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/getpost.jsp";
            Document xml = null;
            String param = "?postnum="+postnum;
            String postid;
            String u = url + param;
            try {
                xml = Jsoup.connect(u).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");

            for (Element e : result) {


                count = e.select("quantity").text().toString();
                day = e.select("shipping_date").text().toString();
                content = e.select("contents").text().toString();
                picturenum = Integer.parseInt(e.select("picturenum").text().toString());

            }
            publishProgress();
            return null;
        }
    }
    class ListImageAsync extends AsyncTask<File, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            VpAdapter adapter = new VpAdapter(getLayoutInflater(),picturenum,1,bmo);//숫자는 글의 사진의 수
            Buyer_Vp.setAdapter(adapter);

            // Loading.Up();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Handler hd = new Handler();
            hd.postDelayed(new TradeSelect.splashhandler(), 5000);



        }

        protected Void doInBackground(File... values) {//user thread


            fso = new File[picturenum];
            bmo = new Bitmap[picturenum];
            for (int i = 0; i < picturenum; i++) {
                fso[i] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/trade" + i);
            }
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "ap-northeast-2:6fb92d56-fccc-4470-af83-af13c271a5b1", // 자격 증명 풀 ID
                    Regions.AP_NORTHEAST_2 // 리전
            );
            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
            s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
            for(int i=0;i<picturenum;i++) {
                transferUtility = new TransferUtility(s3, getApplicationContext());
                TransferObserver observer = transferUtility.download(
                        "samaimage",
                        "post/" + postnum + "/" + "image"+i+".png",
                        fso[i]);
                observer.setTransferListener(new TransferListener() {
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        // update progress bar
                        //progressBar.setProgress(bytesCurrent);
                        Log.i("zzzzzzzzzzzz", "progress changed");

                    }

                    public void onStateChanged(int id, TransferState state) {
                        Bitmap myBitmap[] = new Bitmap[picturenum];
                        for(int i=0;i<picturenum;i++)
                        {
                            myBitmap[i]= BitmapFactory.decodeFile(fso[i].getAbsolutePath());
                        }
                        //Bitmap fin = rotateBitmap(myBitmap, 90);
                        //portraitView.setImageBitmap(myBitmap);
                        bmo = myBitmap;
                        publishProgress();
                    }

                    public void onError(int id, Exception ex) {
                        Log.e("ERROR", ex.getMessage(), ex);
                        Log.i("ERROR", "189");
                        Log.i("ERROR", "image is:");
                        Log.i("ERROR", "iFile is:");
                    }
                });
            }
            publishProgress();
            return null;
        }
    }
    private class splashhandler implements Runnable{
        public void run(){

            l.dismiss();
        }
    }
}
