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

public class fragfinish extends Fragment {
    public ListView Completelv;
    ArrayList<CompleteData> CData;
    String idd;
    TextView bar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragfinish,container,false);
        Completelv = view.findViewById(R.id.list_finish);
        CData = new ArrayList<CompleteData>();
        bar = view.findViewById(R.id.barfinish);

        idd = getArguments().getString("id");
        ListingCd cd = new ListingCd();
        cd.execute();
        return view;
    }
    class ListingCd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            CompleteListAdapter cadapter = new CompleteListAdapter(CData) ;
            Completelv.setAdapter(cadapter);

            Completelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent in = new Intent(MyTrade.this,Trade.class);
                    // startActivity(in);
                    //완료된 거래 정보 보여주기
                    Intent in = new Intent(getActivity(),Complete.class);
                    in.putExtra("postnum",CData.get(position).postnum);
                    in.putExtra("seller",CData.get(position).seller);
                    startActivity(in);
                }
            });
            bar.setText("   "+CData.size()+"개의 글이 있습니다.");

            setListViewHeightBasedOnChildren(Completelv);

            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/mycomplete.jsp?id="+idd;
            Document xml = null;
            Log.i("zzzzzzzz",url);
            try {
                xml = Jsoup.connect(url).get();//url에 접속해서 xml파일을 받아옴
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements result = xml.select("data");
            CData.clear();
            for (Element e : result) {
                CompleteData cd = new CompleteData();
                cd.Title = e.select("title").text().toString();
                cd.Count = e.select("count").text().toString();
                cd.seller = e.select("seller").text().toString();
                cd.postnum = e.select("postnum").text().toString();
                cd.price = e.select("price").text().toString();
                cd.Star = e.select("grade").text().toString();
                cd.url = "http://samaimage.s3.ap-northeast-2.amazonaws.com/thumbnail/post/"+cd.postnum+"/image0.png";
                CData.add(cd);
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
