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
import android.widget.Adapter;
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

public class fragbuy extends Fragment {
    public ListView Buylv;
    ArrayList<BuyData> BData;
    TextView bar;
    String idd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragbuy,container,false);
        Buylv = view.findViewById(R.id.list_buy);
        bar = view.findViewById(R.id.barbuy);
        BData = new ArrayList<BuyData>();
        idd = getArguments().getString("id");
        ListingBd bd = new ListingBd();
        bd.execute();
        return view;
    }
    class ListingBd extends AsyncTask<Void, String, Void> {
        String name;
        String point;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            BuyListAdapter badapter = new BuyListAdapter(BData) ;
            Buylv.setAdapter(badapter);
            Buylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BuyData temp = (BuyData)BData.get(position);

                    Intent i = new Intent(getActivity(),TradeBuyerYet.class);
                    //여기서 Trade로 들어감
                    i.putExtra("postnum",temp.postnum);
                    Log.d("chcheck","gogo"+temp.seller);

                    startActivity(i);
                }
            });
            bar.setText("   "+BData.size()+"개의 글이 있습니다.");
            listViewHeightSet(badapter,Buylv);

            Log.d("checkzzz","Listing");
        }

        protected Void doInBackground(Void... voids) {//user thread
            // 70.12.244.133
            String url = "http://52.79.255.160:8080/mytrade.jsp?id="+idd;
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

    private void listViewHeightSet(Adapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
