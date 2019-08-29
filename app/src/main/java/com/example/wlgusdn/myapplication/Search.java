package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    TextView tv,tv1;
    NestedScrollView scv;
    boolean lastitemVisibleFlag ;
    ArrayList<NoticeData> NData;
    ArrayList<NoticeData> NData1;
    String Login = "login";
    SharedPreferences sf;
    int posts=0;
    int nowcount=0;
    String id;
    NoticeListAdapter nadapter;
    SwipeRefreshLayout ReFresh;
    String sort = "postnum";
    String categori,categori2;
    GridView gl;
    android.support.v7.widget.GridLayout one;
    LinearLayout two;
    String str;
    EditText Title;
    int myLastVisiblePos;
    ListingAsync list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searching);
        sf = getSharedPreferences(Login, 0);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        Title = findViewById(R.id.Search_Edit_Title);
        id = sf.getString("id", "");
        gl = findViewById(R.id.Search_Grid);
        ReFresh=findViewById(R.id.Searching_ReFresh);
        ReFresh.setOnRefreshListener(Search.this);
        lastitemVisibleFlag=false;
        scv=findViewById(R.id.Scv);
        tv1 = findViewById(R.id.Searching_Category_Textview);
        tv = findViewById(R.id.SortText);
        NData = new ArrayList<>();
        NData1 = new ArrayList<>();

        gl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("asdasdasd", String.valueOf(position));
                Toast.makeText(Search.this,"aa"+position,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list = new ListingAsync();
        list.execute();
        scv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override
          public void onScrollChanged()
           {

                       int _scrollViewPos = scv.getScrollY();
                int _TextView_lines = scv.getChildAt(0).getBottom() - scv.getHeight();
                Log.i("zzzzzzscroll",_scrollViewPos+"");
                Log.i("zzzzzText",_TextView_lines+"");

                if (_TextView_lines == _scrollViewPos&&(posts>nowcount)) {
                    Log.i("truetruetrue","true");
                    for (int i = nowcount; i < Math.min(nowcount + 20, posts); i++) {
                        NData1.add(NData.get(i));
                    }
                    nowcount = Math.min(nowcount + 20, posts);
                    nadapter = new NoticeListAdapter(NData1, false);
                    nadapter.notifyDataSetChanged();
                    gl.setAdapter(nadapter);
                    gl.requestDisallowInterceptTouchEvent(true);
                    int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,View.MeasureSpec.AT_MOST);
                    gl.measure(0,expandSpec);
                    gl.getLayoutParams().height = gl.getMeasuredHeight();
                }
            }
        });
        setGridViewHeightBasedOnChildren(gl,gl.getNumColumns());
    }
    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    public void SelectCategory(View view) {
        if(view.getId()==R.id.SearchButton1) {


            Intent in = new Intent(Search.this, SortPopup.class);
            in.putExtra("type", "furniture");
            categori="가구";
            startActivityForResult(in, 2);
        }
        else if (view.getId()==R.id.SearchButton2)
        {
            Intent in = new Intent(Search.this, SortPopup.class);
            in.putExtra("type", "dish");
            categori="식기";
            startActivityForResult(in, 2);

        }
        else
        {
            Intent in = new Intent(Search.this, SortPopup.class);
            in.putExtra("type", "etc");
            categori="기타";
            startActivityForResult(in, 2);

        }


    }
    public void SelectSort(View view) {
        Intent in = new Intent(Search.this, SortPopup.class);
        in.putExtra("type", "sort");
        startActivityForResult(in, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK) {
                //데이터 받기

                String result1 = data.getStringExtra("result1");

                if(result1.equals("닫기"))
                {
                    //do nothing
                }
                else {

                    tv.setText(result1 + "▼");
                    if(result1.equals("최신순"))
                        sort = "postnum";
                    else if(result1.equals("구매수량순"))
                        sort="quantity";
                    nowcount=0;
                    new ListingAsync().execute();
                    //여기가 정렬기준을 선택했을시 SortPopup.java에서 finish()하고 돌아옴
                    //이곳에서 화면의 나오는 글들을 갱신해주어야함
                }
            }


        }
        else if(requestCode==2)
        {
            if (resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("result");
                tv1.setText(result);
                categori2=result;
                if(categori2.equals("전체")){
                    nowcount=0;
                    new categoriAsync().execute();
                }
                else{
                    nowcount=0;
                    new categori2Async().execute();
                }

            }
        }
    }


    @Override
    public void onRefresh() {
        Toast.makeText(Search.this,"새로고침 완료",Toast.LENGTH_SHORT).show();
        nowcount=0;
        new ListingAsync().execute();
        ReFresh.setRefreshing(false);
    }

    public void TitleSearch(View view)
    {
        str = Title.getText().toString();
        //제목검색   str = 제목
        nowcount=0;
        new searchAsync().execute();




    }


    class ListingAsync extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            for(int i=nowcount;i<Math.min(20,posts);i++)
            {
                NData1.add(NData.get(i));
            }
            nowcount= Math.min(20,posts);
            Log.i("zzzzzzzzzzz","posts:"+posts+"nowcount:"+nowcount);
            nadapter = new NoticeListAdapter(NData1, false);
            gl.setAdapter(nadapter);
            gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("asdasdasd1", String.valueOf(position));
                    Toast.makeText(Search.this,"aaa"+position,Toast.LENGTH_LONG).show();
                }
            });
            gl.requestDisallowInterceptTouchEvent(true);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,View.MeasureSpec.AT_MOST);
            gl.measure(0,expandSpec);
            gl.getLayoutParams().height = gl.getMeasuredHeight();


        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/searchmain.jsp?sort="+sort;
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
            NData1.clear();
            for (Element e : result) {
                NoticeData nd = new NoticeData();
                nd.Count = e.select("count").text().toString();
                nd.Date = e.select("s_date").text().toString();
                nd.PersonNum = e.select("seller").text().toString();
                nd.postnum = e.select("postnum").text().toString();
                nd.Hit = e.select("hits").text().toString();
                nd.id = e.select("postid").text();
                if(e.select("true").text().equals(id))
                    nd.basket=1;
                else
                    nd.basket=0;
                nd.url="http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+nd.postnum+"/image0.png";
                NData.add(nd);
            }
            posts=result.size();
            publishProgress();
            return null;
        }
    }
    class categoriAsync extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            for(int i=nowcount;i<Math.min(20,posts);i++)
            {
                NData1.add(NData.get(i));
            }
            nowcount= Math.min(20,posts);
            Log.i("zzzzzzzzzzz","posts:"+posts+"nowcount:"+nowcount);
            nadapter = new NoticeListAdapter(NData1, false);
            gl.setAdapter(nadapter);
            gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("asdasdasd1", String.valueOf(position));
                    Toast.makeText(Search.this,"aaa"+position,Toast.LENGTH_LONG).show();
                }
            });
            gl.requestDisallowInterceptTouchEvent(true);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,View.MeasureSpec.AT_MOST);
            gl.measure(0,expandSpec);
            gl.getLayoutParams().height = gl.getMeasuredHeight();


        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/categori.jsp?sort="+sort+"&categori="+categori;
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
            NData1.clear();
            for (Element e : result) {
                NoticeData nd = new NoticeData();
                nd.Count = e.select("count").text().toString();
                nd.Date = e.select("s_date").text().toString();
                nd.PersonNum = e.select("seller").text().toString();
                nd.postnum = e.select("postnum").text().toString();
                nd.Hit = e.select("hits").text().toString();
                nd.id = e.select("postid").text();
                nd.url="http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+nd.postnum+"/image0.png";
                NData.add(nd);
            }
            posts=result.size();
            publishProgress();
            return null;
        }
    }
    class categori2Async extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            for(int i=nowcount;i<Math.min(20,posts);i++)
            {
                NData1.add(NData.get(i));
            }
            nowcount= Math.min(20,posts);
            Log.i("zzzzzzzzzzz","posts:"+posts+"nowcount:"+nowcount);
            nadapter = new NoticeListAdapter(NData1, false);
            gl.setAdapter(nadapter);
            gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("asdasdasd1", String.valueOf(position));
                    Toast.makeText(Search.this,"aaa"+position,Toast.LENGTH_LONG).show();
                }
            });
            gl.requestDisallowInterceptTouchEvent(true);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,View.MeasureSpec.AT_MOST);
            gl.measure(0,expandSpec);
            gl.getLayoutParams().height = gl.getMeasuredHeight();


        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/categori2.jsp?sort="+sort+"&categori="+categori2;
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
            NData1.clear();
            for (Element e : result) {
                NoticeData nd = new NoticeData();
                nd.Count = e.select("count").text().toString();
                nd.Date = e.select("s_date").text().toString();
                nd.PersonNum = e.select("seller").text().toString();
                nd.postnum = e.select("postnum").text().toString();
                nd.Hit = e.select("hits").text().toString();
                nd.id = e.select("postid").text();
                nd.url="http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+nd.postnum+"/image0.png";
                NData.add(nd);
            }
            posts=result.size();
            publishProgress();
            return null;
        }
    }

    class searchAsync extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            for(int i=nowcount;i<Math.min(20,posts);i++)
            {
                NData1.add(NData.get(i));
            }
            nowcount= Math.min(20,posts);
            Log.i("zzzzzzzzzzz","posts:"+posts+"nowcount:"+nowcount);
            nadapter = new NoticeListAdapter(NData1, false);
            gl.setAdapter(nadapter);
            gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("asdasdasd1", String.valueOf(position));
                    Toast.makeText(Search.this,"aaa"+position,Toast.LENGTH_LONG).show();
                }
            });
            gl.requestDisallowInterceptTouchEvent(true);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,View.MeasureSpec.AT_MOST);
            gl.measure(0,expandSpec);
            gl.getLayoutParams().height = gl.getMeasuredHeight();


        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/searchlike.jsp?search="+str;
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
            NData1.clear();
            for (Element e : result) {
                NoticeData nd = new NoticeData();
                nd.Count = e.select("count").text().toString();
                nd.Date = e.select("s_date").text().toString();
                nd.PersonNum = e.select("seller").text().toString();
                nd.postnum = e.select("postnum").text().toString();
                nd.Hit = e.select("hits").text().toString();
                nd.id = e.select("postid").text();
                nd.url="http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+nd.postnum+"/image0.png";
                NData.add(nd);
            }
            posts=result.size();
            publishProgress();
            return null;
        }
    }


    class NoticeListAdapter extends BaseAdapter
    {


        private ArrayList<NoticeData> MyNoticeData = new ArrayList<NoticeData>();
        private int NoticeListCount=0;
        LayoutInflater inflater = null;
        private boolean Check;
        public int intcheck;
        SharedPreferences sf;

        public NoticeListAdapter(ArrayList<NoticeData> myNoticeData,boolean check)
        {
            intcheck=myNoticeData.size();
            MyNoticeData = myNoticeData;
            NoticeListCount = myNoticeData.size();
            Check=check;
            sf = getSharedPreferences(Login, 0);



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
                Log.i("zzzzzzzzzzzzzz",MyNoticeData.get(position).url);

                Picasso.get().load(MyNoticeData.get(position).url).into(NoticeDataImage);
                NoticeDataCount.setText("수량 : "+MyNoticeData.get(position).Count);
                NoticeDataPersonNum.setText("입찰자 수 : "+MyNoticeData.get(position).PersonNum);
                NoticeDataDate.setText("희망배송일 : "+MyNoticeData.get(position).Date);

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
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("asdasdasd2", String.valueOf(position)+" "+MyNoticeData.get(position).Count);
                        Intent i = null;
                        NoticeData temp = (NoticeData)MyNoticeData.get(position);

                        if(!temp.id.equals(sf.getString("id","")))
                        {
                            i = new Intent(Search.this, TradeBidderYet.class);
                        }
                        else
                        {
                            i = new Intent(Search.this, TradeBuyerYet.class);
                        }
                        //여기서 Trade로 들어감
                        i.putExtra("postnum",temp.postnum);

                        startActivity(i);


                    }
                });
                ImageView NoticeDataImage = convertView.findViewById(R.id.Notice1_imageView);
                TextView NoticeDataCount = convertView.findViewById(R.id.Notice1_Count);
                TextView NoticeDataPersonNum = convertView.findViewById(R.id.Notice1_BidCount);
                TextView NoticeDataDate = convertView.findViewById(R.id.Notice1_Title);
                Log.i("zzzzzzzzzzzzzz",MyNoticeData.get(position).url);


                Picasso.get().load(MyNoticeData.get(position).url).into(NoticeDataImage);
                NoticeDataCount.setText("수량 : "+MyNoticeData.get(position).Count);
                NoticeDataPersonNum.setText("입찰자 수 : "+MyNoticeData.get(position).PersonNum);
                NoticeDataDate.setText("희망배송일 : "+MyNoticeData.get(position).Date);

            }


            return convertView;
        }



    }


}