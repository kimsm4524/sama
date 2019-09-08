package com.example.wlgusdn.myapplication;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Category extends AppCompatActivity
{

    ListView lv;
    ArrayList<String> al;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        al = new ArrayList<>();
        al.add("가구");
        al.add("식기");
        al.add("식료품");
        al.add("미지정");
        al.add("미지정");
        ArrayAdapter la = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al) ;


        lv = findViewById(R.id.Category_list);

        lv.setAdapter(la);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0) {
                    Intent in = new Intent(Category.this, SortPopup.class);
                    in.putExtra("type", "furniture");

                    startActivityForResult(in, 2);
                }
                else if(position==1)
                {
                    Intent in = new Intent(Category.this, SortPopup.class);
                    in.putExtra("type", "dish");

                    startActivityForResult(in, 2);
                }
                else
                {

                    Intent in = new Intent(Category.this, SortPopup.class);
                    in.putExtra("type", "기타");

                    startActivityForResult(in, 2);

                }


            }
        });

        setListViewHeightBasedOnChildren(lv);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       if(requestCode==2)
        {
            if (resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("result");

                Intent intent = new Intent();
                intent.putExtra("result", result);
                setResult(RESULT_OK, intent);

                finish();

            }
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
