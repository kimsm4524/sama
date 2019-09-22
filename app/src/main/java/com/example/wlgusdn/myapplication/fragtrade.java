package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class fragtrade extends Fragment {
    public ListView Tradinglv;
    ArrayList<TradeData> TData;
    TextView bar;
    String idd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragtrade,container,false);
        Tradinglv = view.findViewById(R.id.list_trade);
        TData = new ArrayList<TradeData>();
        bar = view.findViewById(R.id.bartrade);
        idd = getArguments().getString("id");
        ListingTd td = new ListingTd();
        td.execute();
        return view;
    }
    class ListingTd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TradeListAdapter tadapter = new TradeListAdapter(TData) ;
            Tradinglv.setAdapter(tadapter);
            Tradinglv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity(),TradeAlready.class);
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",TData.get(position).postnum);
                    Log.d("qweasdid",idd);
                    // i에 postid넣어줘야함
                    i.putExtra("Postid",TData.get(position).postid);
                    i.putExtra("Sellid",TData.get(position).sellid);

                    Log.d("qweasdid",TData.get(position).postid+ "   "+TData.get(position).sellid);

                    startActivity(i);
                }
            });
            bar.setText("   "+TData.size()+"개의 글이 있습니다.");
            setListViewHeightBasedOnChildren(Tradinglv);
            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/mytrading.jsp?id="+idd;
            Document xml = null;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            TData.clear();
            for (Element e : result) {
                TradeData td = new TradeData();
                td.Count = e.select("count").text().toString();
                td.Date = e.select("s_date").text().toString();
                td.Price = e.select("price").text().toString();
                td.postnum = e.select("postnum").text().toString();
                td.postid = e.select("postid").text().toString();
                td.sellid = e.select("sellerid").text().toString();
                td.url = "http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/auction/"+td.postnum+"/"+td.sellid+"/image0.png";
                TData.add(td);
            }
            publishProgress();
            return null;
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

}
