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

public class fragsell extends Fragment {
    public ListView Selllv;
    ArrayList<SellData> SData;
    TextView bar;
    String idd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag,container,false);
        Selllv = view.findViewById(R.id.list_sell);
        SData = new ArrayList<SellData>();
        bar = view.findViewById(R.id.barsell);

        idd = getArguments().getString("id");
        ListingSd sd = new ListingSd();
        sd.execute();
        return view;
    }
    class ListingSd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            SellListAdapter sadapter = new SellListAdapter(SData) ;
            Selllv.setAdapter(sadapter);
            Selllv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity(),TradeSelect.class);
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",SData.get(position).postnum);
                    Log.d("chcheck",idd);
                    // i에 postid넣어줘야함
                    i.putExtra("Postid",SData.get(position).postid);
                    i.putExtra("Sellid",MainActivity.Myname);
                    startActivity(i);
                }
            });
            bar.setText("   "+SData.size()+"개의 글이 있습니다.");

            setListViewHeightBasedOnChildren(Selllv);
            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/myauction.jsp?id="+idd;
            Document xml = null;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            SData.clear();
            for (Element e : result) {
                SellData sd = new SellData();
                sd.Count = e.select("count").text().toString();
                sd.Title = e.select("title").text().toString();
                sd.Price = e.select("price").text().toString();
                sd.postnum = e.select("postnum").text().toString();
                sd.postid = e.select("postid").text().toString();
                sd.url = "http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/auction/"+sd.postnum+"/"+idd+"/image0.png";
                SData.add(sd);
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
