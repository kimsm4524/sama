package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Basket extends AppCompatActivity
{
    private ListView Basketlv = null;
    String idd;
    ArrayList<BuyData> BData;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Intent intent = getIntent();
        idd = intent.getStringExtra("id");
        BData = new ArrayList<BuyData>();
        ListingBd bd = new ListingBd();
        bd.execute();

    }
    @Override
public void onBackPressed() {
    super.onBackPressed();
    finish();

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
    class ListingBd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Basketlv = (ListView)findViewById(R.id.BasketList);
            BuyListAdapter sadapter = new BuyListAdapter(BData) ;
            Basketlv.setAdapter(sadapter);
            Basketlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BuyData temp = (BuyData)BData.get(position);

                    Intent i = new Intent(Basket.this,TradeBidderYet.class);
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",temp.postnum);
                    i.putExtra("id",idd);
                    Log.d("chcheck","gogo"+temp.seller);

                    startActivity(i);
                }
            });
            TextView bar = findViewById(R.id.barbasket);
            bar.setText("   "+BData.size()+"개의 글이 있습니다.");
            setListViewHeightBasedOnChildren(Basketlv);

            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/preference.jsp?id="+idd;
            Document xml = null;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            BData.clear();
            for (Element e : result) {
                BuyData bd = new BuyData();
                bd.Title = e.select("title").text().toString();
                bd.Count = e.select("count").text().toString();
                bd.seller = e.select("seller").text().toString();
                bd.postnum = e.select("postnum").text().toString();
                bd.hit= e.select("hit").text().toString();
                bd.url = "http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+bd.postnum+"/image0.png";
                BData.add(bd);
                Log.d("chcheck",bd.Title+bd.Count+bd.seller+bd.postnum);
            }
            publishProgress();
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListingBd bd = new ListingBd();
        bd.execute();
    }
    public void backPress(View view)
    {
        finish();
    }
}
